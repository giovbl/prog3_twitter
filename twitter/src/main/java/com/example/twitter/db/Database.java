package com.example.twitter.db;

import java.sql.*;
import java.util.ArrayList;

import com.example.twitter.db.data.post.Post;
import com.example.twitter.db.data.post.RawPost;
import com.example.twitter.db.data.user.LoginInfo;
import com.example.twitter.db.data.user.User;
import com.example.twitter.db.data.user.UserPost;
import com.example.twitter.db.data.user.UserStatistic;
import com.example.twitter.db.utils.HashtagExtractor;
import org.jetbrains.annotations.NotNull;

/**
 * Classe utilizzata per gestire le operazioni sul database
 */
public class Database {

    /**
     * Istanza del database
     */
    private static Database instance;

    /**
     * Connessione per effettuare le operazioni sul database
     */
    private final Connection conn;

    /**
     *  Stringa per permettere la connessione al database
     */
    private final static String CONN_STRING = "jdbc:sqlite:twitter.db";

    /**
     * Costruttore. Crea una connessione con il database e
     * lo configura se non fatto precedentemente
     * @throws SQLException Se si è verificato un problema relativo alla connessione
     * o al codice eseguito sul database
     */
    private Database() throws SQLException {
        try {
            /*
                Si verifica l'esistenza (e ci si assicura che sia caricata)
                 la classe utilizzata per la connessione con il database SQLite.
                L'assenza di questa verifica può causare una fallita connessione
                 per via del driver non trovato
             */
            Class.forName("org.sqlite.JDBC");
        }
        catch(ClassNotFoundException ex) {
            //la classe non è stata trovata
            throw new RuntimeException("Database driver not found: "+ ex.toString());
        }

        //Creazione della connessione con il database
        conn = DriverManager.getConnection(CONN_STRING);

        //Creazione della struttura del database se non esistente
        this.handleStructure();

        //Creazione dell'account amministratore se non esistente
        this.handleAdmin();
    }

    /**
     * Metodo per ottenere l'istanza della classe
     * @return L'istanza della classe
     * @throws SQLException Se si è verificato un problema relativo alla connessione
     * o al codice eseguito sul database
     */
    @NotNull
    public static Database getInstance() throws SQLException {
        if(instance == null)
            instance = new Database();

        return instance;
    }

    /**
     * Configura la struttura del database se non presente
     * @throws SQLException Se si è verificato un problema relativo alla connessione
     * o al codice eseguito sul database
     */
    private void handleStructure() throws SQLException {
        Statement st = conn.createStatement();

        /*
            Creazione codice sql da eseguire
            Questo codice sql permette di creare le tabelle richieste solo
            se non create precedentemente
         */
        String ddl = String.join(

                "CREATE TABLE IF NOT EXISTS User(username TEXT PRIMARY KEY," +
                        "password TEXT NOT NULL,fullname TEXT NOT NULL,is_admin INTEGER NOT NULL);",

                "CREATE TABLE IF NOT EXISTS Following(follower TEXT,followed TEXT," +
                        "PRIMARY KEY(follower,followed)," +
                        "FOREIGN KEY(follower,followed) REFERENCES User(username,username));",

                        "CREATE TABLE IF NOT EXISTS Post(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "message TEXT NOT NULL,owner TEXT NOT NULL,FOREIGN KEY(owner) REFERENCES User(username));",

                        "CREATE TABLE IF NOT EXISTS Hashtag(name TEXT,post INTEGER,PRIMARY KEY(name,post)," +
                        "FOREIGN KEY(post) REFERENCES Post(id));"
        );


        //Esecuzione codice sql
        st.executeUpdate(ddl);
    }

    /**
     * Crea l'utente amministratore se non presente
     * @throws SQLException Se si è verificato un problema relativo alla connessione
     * o al codice eseguito sul database
     */
    private void handleAdmin() throws SQLException {
        Statement st = conn.createStatement();

        //Verifica dell'esistenza dell'utente amministratore
        ResultSet res = st.executeQuery(
                "SELECT username FROM User WHERE username = 'admin'"
        );

        if(!res.next()) {
            /*
                L'utente amministratore non esiste.
                Bisogna crearlo
             */
            st.execute("INSERT INTO User VALUES('admin','admin','Twitter',1)");
        }
    }

    /**
     * Metodo per eseguire il login di un utente
     * @param loginInfo informazioni per effettuare il login
     * @return Se l'utente è autorizzato o meno
     * @throws SQLException Se si è verificato un problema relativo alla connessione
     * o al codice eseguito sul database
     * @throws IllegalArgumentException Se l'oggetto delle informazioni di login è nullo
     */
    public boolean login(@NotNull LoginInfo loginInfo) throws SQLException,IllegalArgumentException {

        if(loginInfo == null)
            throw new IllegalArgumentException("L'oggetto delle informazioni di login non può essere nullo");

        //Preparazione della query
        PreparedStatement pstmt  = conn.prepareStatement(
                "SELECT username FROM User WHERE username = ? AND password = ?"
        );
        pstmt.setString(1,loginInfo.getUsername());
        pstmt.setString(2,loginInfo.getPassword());

        //Ottenimento del risultato della query
        ResultSet res = pstmt.executeQuery();

        //Ritorno del valore indicante il successo del login
        return res.next();
    }

    /**
     * Metodo per creare un utente
     * @param data Dati necessari per la creazione dell'utente
     * @throws SQLException Se si è verificato un problema relativo alla connessione
     * o al codice eseguito sul database
     * @throws IllegalArgumentException Se il parametro è nullo o le sue informazioni sono incompleto
     */
    public void createUser(@NotNull User data) throws SQLException, IllegalArgumentException {

        if(data == null)
            throw new IllegalArgumentException("L'utente da creare non può essere nullo");

        //Preparazione del codice SQL per la creazione dell'utente
        PreparedStatement pstmt  = conn.prepareStatement("INSERT INTO User VALUES(?,?,?,?)");
        pstmt.setString(1, data.getUsername());
        pstmt.setString(2, data.getPassword());
        pstmt.setString(3, data.getFullName());
        pstmt.setBoolean(4, data.getAdminStatus());

        //Esecuzione del codice SQL
        if(pstmt.executeUpdate() != 1)
            throw new SQLException("Utente non creato");
    }

    /**
     * Metodo per aggiungere un post
     * @param data Dati necessari per creare un post
     * @throws SQLException Se si è verificato un problema relativo alla connessione
     * o al codice eseguito sul database
     * @throws IllegalArgumentException Se il parametro è nullo
     */
    public void createPost(@NotNull RawPost data) throws SQLException, IllegalArgumentException {

        if(data == null)
            throw new IllegalArgumentException("Il post da creare non può essere nullo");

        /*
            Preparazione del codice SQL per la creazione del
            e l'ottenimento del suo id
         */
        PreparedStatement stmt  = conn.prepareStatement(
                "INSERT INTO Post(message,owner) VALUES(?,?);"
        );
        stmt.setString(1, data.getMessage());
        stmt.setString(2, data.getOwnerUsername());

        //Esecuzione del codice SQL per inserire il post
        if(stmt.executeUpdate() != 1)
            throw new SQLException("Post non creato");

        //Ottenimento dell'id del post creato
        ResultSet ids = stmt.getGeneratedKeys();
        if(!ids.next())
            throw new RuntimeException("L'id assegnato al post non può essere inesistente");

        //Ottenimento degli hashtag presenti nel post
        ArrayList<String> hashtags = HashtagExtractor.extractHashtags(data.getMessage());

        //Inserimento nel database degli hashtag estratti dal post
        for(String hashtag : hashtags)
        {
            stmt  = conn.prepareStatement(
                    "INSERT INTO Hashtag VALUES(?,?);"
            );
            stmt.setString(1, hashtag);
            stmt.setInt(2, ids.getInt(1));

            if(stmt.executeUpdate() != 1)
                throw new SQLException("Hashtag non creato");
        }
    }

    /**
     * Metodo per ottenere le statistiche sugli utenti presenti nel database
     * @return Gli utenti presenti nel database con le loro statistiche
     * @throws SQLException Se si è verificato un problema relativo alla connessione
     * o al codice eseguito sul database
     * @throws IllegalArgumentException Se un problema interno causa la memorizzazione di un valore nullo
     */
    @NotNull
    public ArrayList<UserStatistic> getUsersStatistics() throws SQLException, IllegalArgumentException {
        //Preparazione della query
        Statement st  = conn.createStatement();

        //Ottenimento del risultato della query
        ResultSet res = st.executeQuery(
                "SELECT U.username," +
                        "(SELECT COUNT(ID) FROM Post WHERE owner = U.username" +
                        ") AS sent," +
                        "(SELECT COUNT(ID) FROM Post " +
                            "WHERE owner IN(" +
                                    "SELECT followed FROM Following " +
                                    "WHERE follower = U.username" +
                                    ")" +
                        ") AS received " +
                        "FROM User U ORDER BY sent,received DESC"
        );

        ArrayList<UserStatistic> ustats = new ArrayList<>();

        //Ottenimento statistiche ricavate
        while(res.next()) {
            ustats.add(new UserStatistic(
                    res.getString("username"),
                    res.getInt("sent"),
                    res.getInt("received")
            ));
        }

        return ustats;
    }

    /**
     * Metodo per ottenere tutti gli hashtag presenti nel database
     * @return Una lista contenente gli hashtag ottenuti
     * @throws SQLException Se si è verificato un problema relativo alla connessione
     * o al codice eseguito sul database
     */
    @NotNull
    public ArrayList<String> getHashtags() throws SQLException {
        //Esecuzione query
        Statement st = conn.createStatement();
        ResultSet res = st.executeQuery(
                "SELECT DISTINCT name FROM Hashtag"
        );

        ArrayList<String> hashtags = new ArrayList<>();

        //Ottenimento degli hashtag trovati
        while(res.next())
            hashtags.add(res.getString("name"));

        return hashtags;
    }

    /**
     * Metodo per ottenere i post il cui messaggio contiene una parola
     * @param word La parola da cercare
     * @return Lista di post che contengono la parola cercata
     * @throws SQLException Se si è verificato un problema relativo alla connessione
     * o al codice eseguito sul database
     * @throws IllegalArgumentException Se il parametro è nullo
     */
    @NotNull
    public ArrayList<Post> getPostsByWord(@NotNull String word) throws SQLException, IllegalArgumentException {

        if(word == null)
            throw new IllegalArgumentException("La parola da cercare non deve essere nulla");
        else if(word.isBlank())
            throw new IllegalArgumentException("La parola da cercare non deve essere una stringa vuota");

        //Preparazione della query
        PreparedStatement pstmt  = conn.prepareStatement(
                "SELECT DISTINCT P.ID,P.message,U.username as username,U.fullname AS fullname FROM Post P " +
                        "INNER JOIN User U ON P.owner = U.username " +
                        "WHERE P.message LIKE ?"
        );
        pstmt.setString(1,new String("%" + word + "%"));

        //Esecuzione e ottenimento del risultato della query
        ResultSet res = pstmt.executeQuery();

        ArrayList<Post> posts = new ArrayList<>();
        //Creazione dei post dal risultato della query
        while(res.next())
        {
            posts.add(
                    new Post(
                            res.getString("message"),
                            new UserPost(
                                    res.getString("username"),
                                    res.getString("fullname")
                            )
                    )
            );
        }

        return posts;
    }

    /**
     * Metodo per ottenere i post degli utenti seguiti da un utente
     * @param username Nome utente dell'utente a cui recuperare i post
     * @return I post recuperati
     * @throws SQLException Se si è verificato un problema relativo alla connessione
     * o al codice eseguito sul database
     * @throws IllegalArgumentException Se il parametro è nullo o una stringa vuota
     */
    @NotNull
    public ArrayList<Post> getFollowedPosts(@NotNull String username) throws SQLException, IllegalArgumentException {

        if(username == null)
            throw new IllegalArgumentException("Il nome utente non può essere nullo");
        else if(username.isBlank())
            throw new IllegalArgumentException("Il nome utente non può essere una stringa vuota");

        //Preparazione della query
        PreparedStatement pstmt  = conn.prepareStatement(
                "SELECT DISTINCT P.ID,P.message,U.username as username,U.fullname AS fullname FROM Post P " +
                        "INNER JOIN User U ON U.username = P.owner " +
                        "WHERE P.owner IN(" +
                            "SELECT followed FROM Following WHERE follower = ?" +
                        ") ORDER BY P.ID DESC"
        );
        pstmt.setString(1,username);

        //Esecuzione e ottenimento del risultato della query
        ResultSet res = pstmt.executeQuery();

        ArrayList<Post> posts = new ArrayList<>();
        //Creazione dei post dal risultato della query
        while(res.next())
        {
            posts.add(
                    new Post(
                            res.getString("message"),
                            new UserPost(
                                    res.getString("username"),
                                    res.getString("fullname")
                            )
                    )
            );
        }

        return posts;
    }

    /**
     * Metodo per ottenere i post il cui messaggio contiene una parola
     * @param hashtag L'hashtag da cercare
     * @return Lista di post che contengono l'hashtag cercato
     * @throws SQLException Se si è verificato un problema relativo alla connessione
     * o al codice eseguito sul database
     * @throws IllegalArgumentException Se il parametro è nullo o una stringa vuota
     */
    @NotNull
    public ArrayList<Post> getPostsByHashtag(@NotNull String hashtag) throws SQLException, IllegalArgumentException {
        if(hashtag == null)
            throw new IllegalArgumentException("L'hashtag da cercare non deve essere nullo'");
        else if(hashtag.isBlank())
            throw new IllegalArgumentException("L'hashtag da cercare non deve essere una stringa vuota'");

        //Preparazione della query
        PreparedStatement pstmt  = conn.prepareStatement(
                "SELECT DISTINCT P.ID,P.message,U.username as username,U.fullname AS fullname FROM Post P " +
                        "INNER JOIN User U ON U.username = P.owner " +
                        "WHERE P.ID IN(SELECT post FROM Hashtag WHERE name = ?)"
        );
        pstmt.setString(1,hashtag);

        //Esecuzione e ottenimento del risultato della query
        ResultSet res = pstmt.executeQuery();

        ArrayList<Post> posts = new ArrayList<>();
        //Creazione dei post dal risultato della query
        while(res.next())
        {
            posts.add(
                    new Post(
                            res.getString("message"),
                            new UserPost(
                                    res.getString("username"),
                                    res.getString("fullname")
                            )
                    )
            );
        }

        return posts;
    }

    /**
     * Metodo per aggiungere un utente ai seguiti di un altro
     * @param follower Nome utente dell'utente che segue l'altro
     * @param followed Nome utente dell'utente da seguire
     * @throws SQLException Se si è verificato un problema relativo alla connessione
     * o al codice eseguito sul database
     * @throws IllegalArgumentException Se uno o più parametri sono nulli o stringhe vuote
     */
    public void addFollower(@NotNull String follower, @NotNull String followed) throws SQLException, IllegalArgumentException {

        if(follower == null)
            throw new IllegalArgumentException("Il nome utente del follower non deve essere nullo");
        else if(follower.isBlank())
            throw new IllegalArgumentException("Il nome utente del follower non deve essere una stringa vuota");

        if(followed == null)
            throw new IllegalArgumentException("Il nome utente del followed non deve essere nullo");
        else if(followed.isBlank())
            throw new IllegalArgumentException("Il nome utente del followed non deve essere una stringa vuota");

        //Preparazione della query
        PreparedStatement pstmt  = conn.prepareStatement("INSERT INTO Following VALUES(?,?)");
        pstmt.setString(1,follower);
        pstmt.setString(2,followed);

        //Esecuzione e ottenimento del risultato della query
        pstmt.executeUpdate();
    }

    /**
     * Metodo per verificare l'esistenza di un utente
     * @param username Nome utente dell'utente da verificare
     * @return Se l'utente esiste
     * @throws SQLException Se si è verificato un problema relativo alla connessione
     * o al codice eseguito sul database
     * @throws IllegalArgumentException Se il parametro è nullo
     */
    public boolean userExists(@NotNull String username) throws SQLException, IllegalArgumentException {

        if(username == null)
            throw new IllegalArgumentException("Il nome utente non può essere nullo");
        else if(username.isBlank())
            return false;

        //Preparazione della query
        PreparedStatement pstmt  = conn.prepareStatement("SELECT username FROM User WHERE username = ?");
        pstmt.setString(1,username);

        //Esecuzione e ottenimento del risultato della query
        ResultSet res = pstmt.executeQuery();

        //Ritorno del valore indicante l'esistenza dell'utente
        return res.next();
    }

    /**
     * Metodo per verificare se un utente è amministratore
     * @param username Nome utente dell'utente da verificare
     * @return Se l'utente esiste ed è amministratore
     * @throws SQLException Se si è verificato un problema relativo alla connessione
     * o al codice eseguito sul database
     * @throws IllegalArgumentException Se il parametro è nullo
     */
    public boolean isUserAdmin(@NotNull String username) throws SQLException, IllegalArgumentException {

        if(username == null)
            throw new IllegalArgumentException("Il nome utente non può essere nullo");
        else if(username.isBlank())
            throw new IllegalArgumentException("Il nome utente non può essere una stringa vuota");

        //Preparazione della query
        PreparedStatement pstmnt  = conn.prepareStatement("SELECT username FROM User WHERE username = ? AND is_admin = true");
        pstmnt.setString(1,username);

        //Esecuzione e ottenimento del risultato della query
        ResultSet res = pstmnt.executeQuery();

        //Ritorno del valore indicante se l'utente esiste ed è amministratore
        return res.next();
    }
}
