package com.example.twitter.db.data.user;

/**
 * Classe contenente le informazioni statistiche di un'utente
 */
public class UserStatistic extends UserCore {

    /**
     * Numero di messaggi inviati
     */
    private final int messagesSent;

    /**
     * Numero di messaggi ricevuti
     */
    private final int messagesReceived;

    /**
     * Costruttore. Inizializza la classe
     * @param username Nome utente
     * @param numSent Numero di messaggi inviati
     * @param numReceived Numero di messaggi ricevuti
     * @throws IllegalArgumentException Se il nome utente è nullo oppure
     * se il numero di messaggi inviati o il numero di messaggi ricevuti è minore di 0
     */
    public UserStatistic(String username, int numSent, int numReceived) throws IllegalArgumentException {

        super(username);

        if(numSent < 0)
            throw new IllegalArgumentException("Il numero di messaggi inviati non può essere minore di 0");

        if(numReceived < 0)
            throw new IllegalArgumentException("Il numero di messaggi ricevuti non può essere minore di 0");

        this.messagesSent = numSent;
        this.messagesReceived = numReceived;
    }

    /**
     * Getter per il numero di messaggi inviati dall'utente
     * @return Il numero di messaggi inviati
     */
    public int getMessagesSent() {
        return this.messagesSent;
    }

    /**
     * Getter per il numero di messaggi ricevuti dall'utente
     * @return Il numero di messaggi ricevuti
     */
    public int getMessagesReceived() {
        return this.messagesReceived;
    }

}
