package com.example.twitter.db.data.user;

import org.jetbrains.annotations.NotNull;

/**
 * Classe rappresentate un utente memorizzato nel database
 */
public class User extends LoginInfo {

    /**
     * Indica se l'utente è un amministratore. Se non inizializzato false
     */
    private final boolean isAdmin;

    /**
     * Nome completo dell'utente
     */
    private final String fullName;

    /**
     * Costruttore. Inizializza un utente di cui si necessita
     * sapere tutte le informazioni
     * @param username Nome utente da assegnare
     * @param password Password dell'utente
     * @param fullName Nome completo dell'utente
     * @param isAdmin Se l'utente è amministratore
     * @throws IllegalArgumentException Se uno o più parametri del costruttore sono nulli o vuoti
     */
    public User(@NotNull String username, @NotNull String password,
                @NotNull String fullName, boolean isAdmin) throws IllegalArgumentException {
        super(username,password);

        if(fullName == null)
            throw new IllegalArgumentException("Il nome completo dell'utente non può essere nullo");
        else if(fullName.isBlank())
            throw new IllegalArgumentException("Il nome completo dell'utente non può essere vuoto");

        this.fullName = fullName;
        this.isAdmin = isAdmin;
    }

    /**
     * Getter per l'indicatore dello stato di amministratore dell'utente
     * @return Valore booleano descrivente se l'utente è amministratore
     */
    public boolean getAdminStatus() {
        return this.isAdmin;
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
