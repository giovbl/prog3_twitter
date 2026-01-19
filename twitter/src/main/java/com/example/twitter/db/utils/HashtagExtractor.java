package com.example.twitter.db.utils;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

/**
 * Classe implementante un'estrattore di hashtag
 */
public class HashtagExtractor {

    /**
     * Carattere iniziale di un hashtag
     */
    private static final char START = '#';

    /**
     * Caratteri che segnano la fine di un hashtag
     */
    private static final String END = " .:,;'#?!\n{}[]";

    /**
     * Metodo per estrarre degli hashtag da una stringa
     * @param str Stringa da cui estrarre gli hashtag
     * @return Gli hashtag ottenuti
     */
    @NotNull
    public static ArrayList<String> extractHashtags(@NotNull String str) {

        ArrayList<String> hashtags = new ArrayList<>();
        String hashtag = new String();
        boolean get = false;

        //Ottenimento degli hashtag
        for(char c : str.toCharArray()) {

            if(c == START) {
                /*
                    Il carattere corrisponde a quello d'inizio.
                    Si procede quindi a catturare i successivi caratteri
                 */
                get = true;
                continue;
            }
            else if(END.indexOf(c) >= 0 && get) {
                /*
                    Il carattere corrisponde a un carattere di fine
                    Si salva quindi l'hashtag ottenuto e ci si prepara
                    per ottenerne un'eventuale successivo
                 */
                get = false;

                //Inserimento dell'hashtag ottenuto nella lista
                hashtags.add(hashtag);

                //Reinizializzazione della stringa per il futuro hashtag
                hashtag = new String();
            }

            if(get)
                hashtag += c;
        }

        if(get && !hashtag.isEmpty()) {
            /*
                Esiste un hashtag terminante alla fine della stringa
                Si provvede quindi a inserirlo alla lista
             */
            hashtags.add(hashtag);
        }

        //Ritorno degli hashtag ottenuti
        return hashtags;
    }
}
