package com.projet.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.projet.model.Player;
import com.ressources.sql.SQL;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginController extends HttpServlet{
    private static final long serialVersionUID = 1L;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        //TODO changer verification pour la bdd
        try {
			if (/*("admin".equals(username) && "password".equals(password))
					|| ("admin2".equals(username) && "password".equals(password))
					|| ("admin3".equals(username) && "password".equals(password))
					|| ("admin4".equals(username) && "password".equals(password))
					*/
					SQL.PlayerNameTaken(username) 
					){
				if( !SQL.PlayerLoginVerification(username, password)) {
					// Redirection vers la page de connexion avec un message d'erreur, mot de passe incorrect
					response.sendRedirect("login.jsp?error=true");
					return;
				}
			} else {
			    SQL.CreatePlayer(username, password);
			}
			// Création de la session
		    HttpSession session = request.getSession();
		    session.setAttribute("user", username);
		    if(!Player.playerAlreadyConnected(username)) {
		    	Player.initPlayerFromLogin(username);
		    	System.out.println("Joueur initialis�");
		    	}
		    response.sendRedirect("game.jsp");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException f) {
			// TODO Auto-generated catch block
			f.printStackTrace();
		} catch (Exception g) {
			// TODO Auto-generated catch block
			g.printStackTrace();
		}
    }
}

