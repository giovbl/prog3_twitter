package com.example.twitter.db.utils;

import com.example.twitter.db.data.user.UserPost;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Classe per la gestione degli UserPost
 */
public class UserPostFactory {

    /**
     * HashMap contenente gli UserPost creati
     */
    private static HashMap<String,UserPost> cache;


    /**
     * Metodo per ottenere l'utente contenete il nome completo
     * @param username Nome utente dell'utente
     * @param fullName Nome completo dell'utente.
     * @return L'oggetto dell'utente
     * @throws IllegalArgumentException Se uno o più parametri sono nulli o vuoti
     */
    @NotNull
    public static UserPost get(@NotNull String username, @NotNull String fullName) throws IllegalArgumentException {

        if(cache == null)
            cache = new HashMap<String,UserPost>();

        if(username == null)
            throw new IllegalArgumentException("Il nome utente non deve essere nullo");
        else if(username.isBlank())
            throw new IllegalArgumentException("Il nome utente non deve essere una stringa vuota");

        UserPost usr = cache.get(username);

        if(usr == null)
        {
            if(fullName == null)
                throw new IllegalArgumentException("Il nome completo non deve essere nullo");
            else if(fullName.isBlank())
                throw new IllegalArgumentException("Il nome completo non deve essere una stringa vuota");

            usr = new UserPost(username, fullName);
            cache.put(username,usr);
        }

        return usr;
    }
}
