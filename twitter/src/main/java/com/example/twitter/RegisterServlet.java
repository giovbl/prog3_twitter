package com.example.twitter;

import com.example.twitter.db.Database;
import com.example.twitter.db.data.user.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Classe per gestire la pagina e le funzionalità di registrazione
 */
@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {

    /**
     * Gestisce una richiesta GET per visualizzare la pagina di registrazione
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {

            //Visualizzazione della pagina di registrazione
            response.setContentType("text/html");
            request.getRequestDispatcher("sign-up.jsp").forward(request, response);
    }

    /**
     * Gestisce una richiesta POST per permettere la registrazione di un utente
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            //Ottenimento dell'istanza del database
            Database db = Database.getInstance();

            try {
                String username = request.getParameter("username");
                //Creazione dell'utente
                db.createUser(
                        new User(
                                username,
                                request.getParameter("password"),
                                request.getParameter("fullname"),
                                false
                        )
                );

                /*
                    Si fà in modo che l'utente segua se stesso in modo
                    da far apparire nel feed i suoi post
                 */
                db.addFollower(username,username);
            }
            catch(SQLException ex) {
                /*
                    Non è stato possibile creare l'account
                    Si ricarica nel modo appropriato la pagina di registrazione
                 */
                request.setAttribute("failed","true");
                response.setContentType("text/html");
                request.getRequestDispatcher("sign-up.jsp").forward(request, response);
            }
            catch (IllegalArgumentException ex){
                throw new ServletException("Errore interno del database: " + ex.toString());
            }

            //Redirezione alla pagina di login
            response.sendRedirect("/login");
        }
        catch(SQLException ex) {
            throw new ServletException("Errore interno del database: " + ex.toString());
        }

    }
}
