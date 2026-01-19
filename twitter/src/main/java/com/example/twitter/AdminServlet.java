package com.example.twitter;

import com.example.twitter.db.Database;
import com.example.twitter.db.data.post.Post;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe implementante il servlet per la pagina dell'amministratore
 */
@WebServlet(name = "AdminServlet", value = "/admin")
public class AdminServlet extends HttpServlet {

    /**
     * Gestisce una richiesta GET per visualizzare la pagina per l'amministratore con
     * gli eventuali contenuti richiesti
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        boolean failed = false;

        try {

            String username = request.getParameter("user");

            if(username != null && !username.isBlank()) {

                //Ottenimento dell'istanza del database
                Database db = Database.getInstance();

                if(db.isUserAdmin(username)) {

                    String action = request.getParameter("action");

                    if(action != null) {

                        if(action.equals("hashtag")) {

                            HashMap<String, ArrayList<Post>> htgs_posts = new HashMap<String, ArrayList<Post>>();

                            //Ottenimento hashtag presenti nel database
                            ArrayList<String> hashtags = db.getHashtags();

                            for(String hashtag : hashtags) {

                                //Ottenimento e memorizzazione post contenenti l'hashtag
                                htgs_posts.put(hashtag,db.getPostsByHashtag(hashtag));
                            }

                            //Memorizzazione risultati ottenuti
                            request.setAttribute("result",htgs_posts);

                        } else if(action.equals("word")) {

                            //Ottenimento parola da cercare
                            String word = request.getParameter("word");

                            if(word != null && !word.isBlank()) {

                                //Ottenimento e memorizzazione post contenenti la parola
                                request.setAttribute("result",db.getPostsByWord(word));
                            }
                            else {

                                /*
                                    La parola da cercare non è presente
                                    Si procede a inviare l'opportuna risposta di errore
                                 */
                                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                                return;

                            }

                        } else if(action.equals("stats")) {

                            //Ottenimento e memorizzazione statistiche sugli utenti
                            request.setAttribute("result",db.getUsersStatistics());

                        }
                    }

                    /**
                     * L'utente esiste ed è amministratore.
                     * Si fà quindi visualizzare la pagina dell'amministratore
                     */
                    response.setContentType("text/html");
                    request.getRequestDispatcher("admin.jsp").forward(request, response);
                }
                else
                    failed = true;
            }
            else
                failed = true;
        }
        catch(SQLException | IllegalArgumentException ex){
            throw new ServletException("Errore interno del database: " + ex.toString());
        }

        if(failed) {
            /**
             * L'utente non è amministratore, non è esistente o
             * non è stato passato come parametro.
             * Si redirige quindi l'utente alla pagina di login
             */
            response.sendRedirect("/login");
        }

    }
}