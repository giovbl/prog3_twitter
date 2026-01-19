package com.example.twitter.db.data.post;

import org.jetbrains.annotations.NotNull;

/**
 * Classe astratta contenente le informazioni essenziali di un post
 */
public abstract class PostCore {

    /**
     * Numero massimo di caratteri del messaggio del post
     */
    private static final int messageLength = 140;

    /**
     * Messaggio del post
     */
    private final String message;

    /**
     * Costruttore. Inizializza le informazioni essenziali di un post
     * @param message Messaggio del post
     * @throws IllegalArgumentException
     * Se il parametro del costruttore è nullo o una stringa vuota
     * oppure se il numero di caratteri del messaggio è maggiore di 140 caratteri
     */
    protected PostCore(@NotNull String message) throws IllegalArgumentException {

        if(message == null)
            throw new IllegalArgumentException("Il messaggio non deve essere nullo");
        else if(message.isBlank())
            throw new IllegalArgumentException("Il messaggio non deve essere una stringa vuota");
        else if(message.length() > this.messageLength)
            throw new IllegalArgumentException("Il messaggio non può contenere più di 140 caratteri");

        this.message = message;
    }

    /**
     * Getter per il messaggio del post
     * @return Il messaggio del post
     */
    @NotNull
    public String getMessage() {
        return this.message;
    }

}
