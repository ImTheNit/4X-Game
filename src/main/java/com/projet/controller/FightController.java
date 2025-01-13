package com.projet.controller;

import java.io.IOException;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.projet.model.Soldier;
import com.projet.model.attackable;

import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

/**
 * Servlet implementation class CombatController
 * This class mainly generate html code and send it back to the global controller
 */
@ServerEndpoint(value = "/fight" , configurator = HttpSessionConfigurator.class)
public class FightController {

	public static int lastDamageDealt =0;
	public static int remainingHp = -1;
	public static Soldier attacker ;
	public static attackable defenser ; // using interface
	public static boolean isFight = false;
    
    
    public static ObjectNode onMessage(String message, Session session,ObjectNode jsonObject) throws IOException {
	    	    
	    String fightSummary = new String();
	    if (isFight) {
	    	fightSummary = "<div class='fight-summary'>\n" +
		            "    <h2>Fight summary</h2>\n";
		    if (remainingHp == 0) {
		    	fightSummary+="<div class='result'>'\n"
			    		+ "            <img src='ressources/images/victory.png' alt='Victoire'>\n"
			    		+ "            <p>Victoire</p>\n"
			    		+ "        </div>";
		    }
		    
		    
		    fightSummary +="    <div class='stats-fight'>\n" +
		            "        <div>\n" +
		            "            <p>Attackers </p> "+
		            "			 <p>Owner : " +attacker.getOwner().getLogin()+" Soldier#"+attacker.getIndex()+"</p>\n" +
		            "            <span>" +lastDamageDealt + "</span>\n" +
		            "            </div>\n" +
		            "        <div>\n" +
		            "            <p>Target</p>\n";
		            
		    if (defenser.getOwnerName()==null) {
		    	fightSummary += "<p>Neutral City</p>\n";
		    }else {
		    	fightSummary += "<p>Owner : " +defenser.getOwnerName()+" "+ defenser.getTypeAttackable()+"#"+defenser.getIndex()+"</p>\n";
		    }
		    fightSummary +="            <span class='hp'>" +remainingHp + "</span>\n" +
		            "        </div>\n" +
		            "    </div>\n" +
		            "</div>";
		    
		    
	    }
	    jsonObject.put("fightSummary", fightSummary);
		return jsonObject;

	}

    /*
     * setters
     */
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
    public static void setAttacker(Soldier x) {
    	attacker = x;
    }
    public static void setDefenser(attackable x) {
    	defenser = x;
    }
    
}
