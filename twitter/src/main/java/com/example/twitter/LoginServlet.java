package com.example.twitter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.example.twitter.db.Database;
import com.example.twitter.db.data.user.LoginInfo;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Classe implementante il servlet per il login di un utente
 */
@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    /**
     * Gestisce una richiesta GET per visualizzare la pagina di login
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {

            //Visualizzazione della pagina di login
            response.setContentType("text/html");
            request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    /**
     * Gestisce una richiesta POST per autenticare un utente
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            //Creazione delle informazioni di login
            LoginInfo loginInfo = new LoginInfo(
                    request.getParameter("username"),
                    request.getParameter("password")
            );

            //Ottenimento dell'istanza del database
            Database db = Database.getInstance();

            if(db.login(loginInfo)) {
                /*
                    Il login ha dato esito positivo.
                    Si procede a indirizzare l'utente alla pagina corretta
                 */

                if(db.isUserAdmin(loginInfo.getUsername())) //Si verifica se l'utente è amministratore
                    response.sendRedirect("/admin?user=" + loginInfo.getUsername());
                else
                    response.sendRedirect("/feed?user=" + loginInfo.getUsername());
            }
            else {
                /*
                    L'autenticazione ha dato esito negativo
                    Si ricarica nel modo opportuno la pagina di login
                 */
                request.setAttribute("failed","true");
                response.setContentType("text/html");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        }
        catch(SQLException | IllegalArgumentException ex){
            throw new ServletException("Errore interno del database: " + ex.toString());
        }

    }
}
