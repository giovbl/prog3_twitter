package com.example.twitter.db.data.user;

import org.jetbrains.annotations.NotNull;

/**
 * Classe astratta contenente le informazioni essenziali di un utente
 */
public abstract class UserCore {

    /**
     * Nome utente dell'utente
     */
    private final String username;

    /**
     * Costruttore. Inizializza la classe
     * @param username Nome utente
     * @throws IllegalArgumentException Il parametro del costruttore non deve essere nullo
     * o una stringa vuota
     */
    protected UserCore(@NotNull String username) throws IllegalArgumentException {

        if(username == null)
            throw new IllegalArgumentException("Il nome utente non deve essere nullo");
        else if(username.isBlank())
            throw new IllegalArgumentException("Il nome utente non deve essere una stringa vuota");

        this.username = username;
    }

    /**
     * Getter per ottenere il nome utente dell'utente
     * @return Il nome utente dell'utente
     */
    @NotNull
    public String getUsername() {
        return this.username;
    }
}
