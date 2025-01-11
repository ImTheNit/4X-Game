package com.projet.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.projet.model.City;
import com.projet.model.Forest;
import com.projet.model.MapGame;
import com.projet.model.Player;
import com.projet.model.TargetActionType;
import com.projet.model.Tile;
import com.projet.model.TileType;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

/**
 * Servlet implementation class CombatController
 */
@ServerEndpoint(value = "/fight" , configurator = HttpSessionConfigurator.class)
public class FightController {

	public static int lastDamageDealt =0;
	public static int remainingHp = -1;
	public static boolean isFight = false;
    
    
    public static ObjectNode onMessage(String message, Session session,ObjectNode jsonObject) throws IOException {
	    	    
	    String fightSummary = new String();
	    if (isFight) {
	    	fightSummary = "<div class='fight-summary'>\n" +
		            "    <h2>Résumé de Combat</h2>\n";
		    if (remainingHp == 0) {
		    	fightSummary+="<div class='result'>'\n"
			    		+ "            <img src='ressources/images/victory.png' alt='Victoire'>\n"
			    		+ "            <p>Victoire</p>\n"
			    		+ "        </div>";
		    }
		    
		    
		    fightSummary +="    <div class='stats-fight'>\n" +
		            "        <div>\n" +
		            "            <p>Dégâts Infligés</p>\n" +
		            "            <span>" +lastDamageDealt + "</span>\n" +
		            "        </div>\n" +
		            "        <div>\n" +
		            "            <p>PV Restants</p>\n" +
		            "            <span class='hp'>" +remainingHp + "</span>\n" +
		            "        </div>\n" +
		            "    </div>\n" +
		            "</div>";
		    
		    
		    //setIsFight(false);
	    }
	    jsonObject.put("fightSummary", fightSummary);
		return jsonObject;

	}

    
	/**
	 * 
	 * @param x
	 */
    public static void setLastDamageDealt(int x) {
    	lastDamageDealt = x;
    }
    
    public static void setRemainingHp(int x) {
    	remainingHp = x;
    }
    public static void setIsFight(boolean x) {
    	isFight = x;
    }
    
    
    
}
