package com.projet.controller;

import java.io.IOException;

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

        // Vérification des informations de connexion (à adapter selon votre logique)
        if ("admin".equals(username) && "password".equals(password)) {
            // Création de la session
            HttpSession session = request.getSession();
            session.setAttribute("user", username);
            response.sendRedirect("game.jsp");
        } else {
            // Redirection vers la page de connexion avec un message d'erreur
            response.sendRedirect("login.jsp?error=true");
        }
    }
}

