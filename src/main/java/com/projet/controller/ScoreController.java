package com.projet.controller;


import java.io.IOException;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.projet.model.Player;

import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;


/**
 * this class mainly generate Html code the score of post game
 * All player will see the same thing
 */

@ServerEndpoint(value = "/score" , configurator = HttpSessionConfigurator.class)
public class ScoreController {  
    

    public static ObjectNode onMessage(String message, Session session,int clientId,Player p,ObjectNode jsonObject) throws IOException {
	    
        //load html code for each player 
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
	/**
	 * generate html score of the player at the index 'index'
	 * @param index of the player to generate the score
	 * @return html code 
	 */
    private static  String ScorePlayerByIndex(int index) {
    	Player p = Player.getPlayerList(index);
    	String ret ="";
    	if (p.getLogin()!="") {	// Connected
    		ret += "<h2>Joueur "+  (index+1)  +"</h2>\n" +
                    "<p>Nom: Joueur "+  p.getLogin()  +"</p>\n" +
                    "<p>Score: "+  p.getScore()  +"</p>\n" +
                    "<p>Production de Ressources: "+  p.getProductionPoints()  +"</p>\n" +
                    "<p>Combats Gagnés: "+  p.getFightsWon()  +"</p>";
    	}else {
    		ret += "<p>No player</p>";
    	}
    	
    	return ret;
    }
       
}