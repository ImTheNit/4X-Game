package com.projet.controller;


import java.io.IOException;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.projet.model.City;
import com.projet.model.Forest;
import com.projet.model.MapGame;
import com.projet.model.Player;
import com.projet.model.TargetActionType;
import com.projet.model.Tile;
import com.projet.model.TileType;

import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/score" , configurator = HttpSessionConfigurator.class)
public class ScoreController {
    //private static final Set<Session> clients = new CopyOnWriteArraySet<>();
    
    
    
    
    public static ObjectNode onMessage(String message, Session session,int clientId,Player p,ObjectNode jsonObject) throws IOException {
	    
	    
        
    	String scorePlayer1 = ScorePlayerByIndex(0);
    	String scorePlayer2 = ScorePlayerByIndex(1);
    	String scorePlayer3 = ScorePlayerByIndex(2);
    	String scorePlayer4 = ScorePlayerByIndex(3);
    	
    	
    	jsonObject.put("htmlJoueur1", scorePlayer1);
    	jsonObject.put("htmlJoueur2", scorePlayer2);
    	jsonObject.put("htmlJoueur3", scorePlayer3);
    	jsonObject.put("htmlJoueur4", scorePlayer4);

        
        return jsonObject; 
    }
	    

    
    
    
    
    
    
    
    private static  String ScorePlayerByIndex(int index) {
    	Player p = Player.getPlayerList(index);
    	String ret ="";
    	if (p.getLogin()!="") {	// Connected
    		ret += "<h2>Joueur "+  index  +"</h2>\n" +
                    "<p>Nom: Joueur "+  p.getLogin()  +"</p>\n" +
                    "<p>Score: "+  p.getScore()  +"</p>\n" +
                    "<p>Production de Ressources: "+  p.getProductionPoints()  +"</p>\n" +
                    "<p>Combats Gagn�s: "+  p.getFightsWon()  +"</p>";
    	}else {
    		ret += "<p>Non rejoint</p>";
    	}
    	
    	return ret;
    }
   
    
	public void sendMessage(Session clientSession, String jsonString) {
	    // Fonction pour v�rifier l'�tat de la session et envoyer le message
	    Runnable checkAndSend = new Runnable() {
	        @Override
	        public void run() {
	            if (clientSession != null && clientSession.isOpen()) {
	                clientSession.getAsyncRemote().sendText(jsonString);
	                System.out.println("Message envoy� : " + jsonString);
	            } else {
	                System.out.println("La session n'est pas encore ouverte, attente...");
	                try {
	                    Thread.sleep(100); // Attendre 100ms avant de r�essayer
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	                run(); // R�essayer
	            }
	        }
	    };

	    // Lancer la v�rification et l'envoi
	    checkAndSend.run();
	}

    
}