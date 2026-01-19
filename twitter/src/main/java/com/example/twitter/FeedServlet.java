package com.example.twitter;

import com.example.twitter.db.Database;
import com.example.twitter.db.data.post.RawPost;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Classe implementante il servlet per il feed di un utente
 */
@WebServlet(name = "FeedServlet", value = "/feed")
public class FeedServlet extends HttpServlet {

    /**
     * Gestisce una richiesta GET per visualizzare la pagina di feed con i relativi contenuti
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {

        String user = request.getParameter("user");

        try {

            Database db = Database.getInstance();


            if(user == null || !db.userExists(user)){
                //Redirezione alla pagina di login
                response.setContentType("text/html");
                response.sendRedirect("/login");
            }
            else{

                //Ottenimento e memorizzazione post da visualizzare
                request.setAttribute("posts",db.getFollowedPosts(user));

                //Visualizzazione della pagina di feed
                response.setContentType("text/html");
                request.getRequestDispatcher("feed.jsp").forward(request, response);
            }

        } catch(SQLException | IllegalArgumentException ex) {
            throw new ServletException("Errore interno del database: " + ex.toString());
        }

    }

    /**
     * Gestisce una richiesta POST per creare un post o aggiungere un utente ai seguiti
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {

        String user = request.getParameter("user");
        String action = request.getParameter("action");

        try {
            //Ottenimento dell'istanza del database
            Database db = Database.getInstance();

            if(user == null && !db.userExists(user)) {
                /*
                    L'utente non è stato specificato o non è esistente
                    La richiesta è da considerarsi come non autorizzata
                 */
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            if(action == null) {
                /*
                    L'azione non è presente
                    Si procede a inviare l'opportuna risposta di errore
                 */
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;

            } else if(action.equals("post")) {

                try {
                    //Creazione del post
                    db.createPost(new RawPost(
                            request.getParameter("message"),
                            user
                    ));
                }
                catch(IllegalArgumentException ex) {
                    /*
                        L'eccezione è avvenuta perchè il messaggio non è stato specificato,
                        oppure è vuoto oppure maggiore di 140 caratteri
                        Si procede a visualizzare la pagina feed con un messaggio di errore
                     */
                    response.sendRedirect("/feed?user=" + user + "&failed=post");
                    return;
                }

            } else if(action.equals("follow")) {

                try {
                    //Aggiunta dell'utente ai seguiti
                    db.addFollower(
                            user,
                            request.getParameter("to_follow")
                    );
                }
                catch(IllegalArgumentException ex) {
                    /*
                        L'eccezione è avvenuta poichè il parametro indicante l'utente
                        da seguire è nullo o vuoto.
                        Si procede a visualizzare la pagina feed con un messaggio di errore
                     */
                    response.sendRedirect("/feed?user=" + user + "&failed=follow");
                    return;
                }

            } else {

                /*
                    L'azione non è valida.
                    Si procede a inviare l'opportuna risposta di errore
                 */
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

        }
        catch(SQLException | IllegalArgumentException ex) {
            throw new ServletException("Errore interno del database: " + ex.toString());
        }

        /*
            L'operazione è andata a buon fine
            Si procede quindi a visualizzare la pagina feed con
            un messaggio di successo
         */
        response.sendRedirect("/feed?user=" + user + "&success=" + action);

    }

}
