package com.projet.controller;

import java.util.ArrayList;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;

public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator {
	
	private static ArrayList<HttpSession> sessions = new ArrayList<HttpSession>();
	private static ArrayList<String> username = new ArrayList<String>();
	
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        config.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }

	/**
	 * @return the sessions
	 */
	public static ArrayList<HttpSession> getSessions() {
		return sessions;
	}
	/**
	 * @return the username
	 */
	public static ArrayList<String> getUsername() {
		return username;
	}
	
	
	/**
	 * @param sessions the sessions to set
	 */
	public static void setSessions(ArrayList<HttpSession> sessions) {
		HttpSessionConfigurator.sessions = sessions;
	}
	public static void setSessions(HttpSession sessions, int index) {
		//HttpSessionConfigurator.sessions.set(index) = sessions;
	}
	/**
	 * @param username the username to set
	 */
	public static void setUsername(ArrayList<String> username) {
		HttpSessionConfigurator.username = username;
	}
	
	
	
	public static void addSession(HttpSession s) {
		if (s!=null
				&& s.getAttribute("user")!=null) {
			int j=0;
			for (int i = 0 ; i<4;i++) {
				if (i <getUsername().size()) { // L'index existe deja, 
					//on vérifie que la session n'existe pas déja
					if(s.getAttribute("user")==getUsername().get(i) ) {// il est déjà connecté
						sessions.remove(i);
						username.remove(i);
						j = 5; //break the loop
					}
						
				}else{	//l'index n'existe pas
					j = 5;
					break;
				}
			}
			//End For
			if ( j == 5 ) {
				sessions.add(s);
				username.add((String) s.getAttribute("user"));
				System.out.println("session add to the index "+getSessions().size());
				System.out.println("username add to the index "+getUsername().size());
			}
			
			
		}
	}
	
	
}
