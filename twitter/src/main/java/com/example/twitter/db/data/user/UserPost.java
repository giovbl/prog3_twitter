package com.example.twitter.db.data.user;

import org.jetbrains.annotations.NotNull;

/**
 * Classe rappresentante un utente utilizzato in un post
 */
public class UserPost extends UserCore {

    /**
     * Nome completo dell'utente
     */
    private String fullName;

    /**
     * Costruttore. Inizializza un utente di cui si necessita sapere
     * il nome utente
     * @param username Nome utente da assegnare
     * @param fullName Nome completo dell'utente
     * @throws IllegalArgumentException Se uno o più parametri del costruttore sono nulli o vuoti
     */
    public UserPost(@NotNull String username,@NotNull String fullName) throws IllegalArgumentException {
        super(username);

        if(fullName == null)
            throw new IllegalArgumentException("Il nome completo dell'utente non può essere nullo");
        else if(fullName.isBlank())
            throw new IllegalArgumentException("Il nome completo dell'utente non può essere vuoto");

        this.fullName = fullName;
    }

    /**
     * Getter per il nome completo dell'utente
     * @return Nome completo dell'utente
     */
    @NotNull
    public String getFullName() {
        return this.fullName;
    }

}
