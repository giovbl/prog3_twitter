package com.example.twitter.db.data.user;

import com.example.twitter.db.data.user.UserCore;
import org.jetbrains.annotations.NotNull;

/**
 * Classe contenente le informazioni necessarie per effettuare
 * il login di un utente
 */
public class LoginInfo extends UserCore {

    /**
     * Password dell'utente
     */
    private final String password;

    /**
     * Costruttore. Inizializza un oggetto contenente le informazioni
     * necessarie per effettuare un login
     * @param username Nome utente
     * @param password Password
     * @throws IllegalArgumentException I parametri del costruttore non devono essere nulli
     */
    public LoginInfo(String username,String password) throws IllegalArgumentException {

        super(username);

        if(password == null)
            throw new IllegalArgumentException("La password non deve essere nulla");
        else if(password.isBlank())
            throw new IllegalArgumentException("La password non deve essere una stringa vuota");

        this.password = password;
    }

    /**
     * Getter per ottenere la password dell'utente
     * @return La password dell'utente
     */
    @NotNull
    public String getPassword() {
        return this.password;
    }
}
