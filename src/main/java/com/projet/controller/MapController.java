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




/**
 * This class mainly generate html code and send it back to the global controller
 */
@ServerEndpoint(value = "/map" , configurator = HttpSessionConfigurator.class)
public class MapController {
    //private static final Set<Session> clients = new CopyOnWriteArraySet<>();
    
    
    
    
    public static ObjectNode onMessage(String message, Session session,int clientId,Player p,ObjectNode jsonObject) throws IOException {
	    
	    
        Tile selection = MapGame.getMap().getTile(0, 0); // default value;
        //load selection Tile
        
        switch (p.getTargetActionType()) {
        
        case CITY:
        	if(p.getCities().size()>0) {
        		int x =p.getCities(p.getIndex()).getX();
            	int y =p.getCities(p.getIndex()).getY();
            	selection = MapGame.getMap().getTile(x, y);
        	}
        	break;
        	
        case SOLDIER:
        	if (p.getUnits().size()>0) {
        		int x1 = p.getUnits().get(p.getIndex()).getPositionX();
            	int y1 = p.getUnits().get(p.getIndex()).getPositionY();
            	selection = MapGame.getMap().getTile(x1, y1);
        	}
        	break;
        	
        default :
        	System.out.println("default value for selection");
        	selection = MapGame.getMap().getTile(0, 0); // default value
        	break;
        }

        // load map and button content
    	String html = MapGame.getMap().printJSP(p, selection);
    	String button = getButtonHTML(p,selection);
    	
    	//convert because of quote
    	String escapedStringMap = html.replace("\"", "\\\"");
    	
    	// add to json object
        jsonObject.put("action","map");
        jsonObject.put("html", escapedStringMap);
        
        //stats
        jsonObject.put("battlesWon",p.getFightsWon());
        jsonObject.put("soldiers",p.getUnits().size());
        jsonObject.put("cities",p.getCities().size());
        jsonObject.put("score",p.getScore());
        jsonObject.put("ressources",p.getProductionPoints());
        
        jsonObject.put("button", button);

        
        return jsonObject; 
    }
	    

    
    
    
    
    
    
    
    /**
     * return html code for button to display, depending of the player
     * @param p : current player
     * @param selection : tile of the next action
     * @return
     */
    private static String getButtonHTML(Player p ,Tile selection) {
    	String ret = "";
    	
    	if ( (p!= null)
    			&& ( Player.getActivePlayerIndex()==Player.getPlayerIndexByLogin(p.getLogin()) )	) {
 
    		//action for soldier
    		if (p.getTargetActionType()==TargetActionType.SOLDIER ) { // remaining unit have top play

    			
    			if (p.getUnits().size()>0) {
    				
    				//move buttons 
    				ret += "		<button class='button' onclick='action(\"moveNorth\")'>Move To North</button>"
            	    		+ "		<button class='button' onclick='action(\"moveWest\")'>Move To West</button>"
            	    		+ "		<button class='button' onclick='action(\"moveSouth\")'>Move To South</button>"
            	    		+ "		<button class='button' onclick='action(\"moveEast\")'>Move To East</button>";
            		//position
            		int x = p.getUnits().get(p.getIndex()).getPositionX();
            		int y = p.getUnits().get(p.getIndex()).getPositionY();
            		
            		//collect button
            		if (MapGame.getMap().getTile(x, y).getType()==TileType.FOREST 
            				&& ((Forest)MapGame.getMap().getTile(x, y)).getProductionRessources()>0) {
            			ret += "<button class='button' onclick='action(\"collect\")'>Collect Ressources</button>";
            		}
            		//heal button
            		if (p.getUnits().get(p.getIndex()).getDefence()<p.getUnits().get(p.getIndex()).getMaxDefence()) {
            			ret += "<button class='button' onclick='action(\"heal\")'>Heal</button>";
            		}
            		//skip button
            	    ret += "<button class='button' onclick='action(\"pass\")'>Pass</button>";
    			}else {
    				p.incrementAction();
    			}
        		
    		}
    		//action for city
    		if (p.getTargetActionType()==TargetActionType.CITY && p.getCities().size()>0) { 

    			if (p.getProductionPoints()>=City.costRecruitement 
    					&& selection.getUnit() == null) {
    				//recruit button
    				ret += "<button class='button' onclick='action(\"recruit\")'>Recruit a unit</button>";
    			}
    			//skip button
    			ret += "<button class='button' onclick='action(\"pass\")'>Pass</button>";	
    		}
    	}
    	return ret;
    }    
}