package com.example.twitter.db.data.post;

import com.example.twitter.db.data.user.UserPost;
import com.example.twitter.db.utils.UserPostFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Classe rappresentate un post
 */
public class Post extends PostCore {

    /**
     * Utente proprietario del post
     */
    private final UserPost owner;

    /**
     * Costruttore. Inizializza un post il cui id è inesistente
     * @param message Messaggio del post
     * @param owner Nome utente dell'utente proprietario del post
     * @throws IllegalArgumentException
     * Se uno o più parametri del costruttore sono nulli o stringhe vuote
     * oppure se il numero di caratteri del messaggio è maggiore di 140 caratteri
     */
    public Post(@NotNull String message, @NotNull UserPost owner) throws IllegalArgumentException {

        super(message);

        if(owner == null)
            throw new IllegalArgumentException("Il proprietario non deve essere nullo");

        this.owner = UserPostFactory.get(owner.getUsername(),owner.getFullName());
    }

    /**
     * Getter per il nome utente del proprietario
     * @return Il messaggio del post
     */
    @NotNull
    public UserPost getOwner() {
        return this.owner;
    }
}
