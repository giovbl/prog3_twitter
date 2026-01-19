package com.example.twitter.db.data.post;

import org.jetbrains.annotations.NotNull;

/**
 * Classe rappresentate un post nel database
 */
public class RawPost extends PostCore {

    /**
     * Nome utente del proprietario del post
     */
    private final String owner;

    /**
     * Costruttore. Inizializza un post
     * @param message Messaggio del post
     * @param ownerUsername Nome utente dell'utente proprietario del post
     * @throws IllegalArgumentException
     * Se uno o più parametri del costruttore sono nulli o stringhe vuote
     * oppure se il numero di caratteri del messaggio è maggiore di 140 caratteri
     */
    public RawPost(@NotNull String message, @NotNull String ownerUsername) throws IllegalArgumentException {

        super(message);

        if(ownerUsername == null)
            throw new IllegalArgumentException("Il nome utente del proprietario non deve essere nullo");
        else if(ownerUsername.isBlank())
            throw new IllegalArgumentException("Il nome utente del proprietario non deve essere vuoto");

        this.owner = ownerUsername;
    }

    /**
     * Getter per il nome utente del proprietario
     * @return Il nome utente del proprietario
     */
    @NotNull
    public String getOwnerUsername() {
        return this.owner;
    }
}
