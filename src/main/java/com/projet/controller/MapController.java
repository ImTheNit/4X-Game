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

@ServerEndpoint(value = "/map" , configurator = HttpSessionConfigurator.class)
public class MapController {
    //private static final Set<Session> clients = new CopyOnWriteArraySet<>();
    
    
    
    
    public static ObjectNode onMessage(String message, Session session,int clientId,Player p,ObjectNode jsonObject) throws IOException {
	    
	    
        Tile selection = MapGame.getMap().getTile(0, 0); // default value;

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

        
    	String html = MapGame.getMap().printJSP(p, selection);
    	String button = getButtonHTML(p,selection);
    	
    	
    	String escapedStringMap = html.replace("\"", "\\\"");
    	
        jsonObject.put("action","map");
        jsonObject.put("html", escapedStringMap);
        
        //ajouter les stats pour les actualiser
        jsonObject.put("battlesWon",p.getFightsWon());
        jsonObject.put("soldiers",p.getUnits().size());
        jsonObject.put("cities",p.getCities().size());
        jsonObject.put("score",p.getScore());
        jsonObject.put("ressources",p.getProductionPoints());
        
        jsonObject.put("button", button);

        
        return jsonObject; 
    }
	    

    
    
    
    
    
    
    
    
    private static String getButtonHTML(Player p ,Tile selection) {
    	// fonction générique
    	// stocker en session le type (Ville/soldat) ainsi que son index
    	// modifier la selection pour la cible de l'action
    	String ret = "";
    	if ( (p!= null)
    			&& (Player.getActivePlayerIndex()==Player.getPlayerIndexByLogin(p.getLogin()))
    			) {
    		
    		//case of a soldier
    		
    		if (p.getTargetActionType()==TargetActionType.SOLDIER ) { // remaining unit have top play

    			if (p.getUnits().size()>0) {
    				ret += "		<button class='button' onclick='action(\"moveNorth\")'>Move To North</button>"
            	    		+ "		<button class='button' onclick='action(\"moveWest\")'>Move To West</button>"
            	    		+ "		<button class='button' onclick='action(\"moveSouth\")'>Move To South</button>"
            	    		+ "		<button class='button' onclick='action(\"moveEast\")'>Move To East</button>";
            		//position
            		int x = p.getUnits().get(p.getIndex()).getPositionX();
            		int y = p.getUnits().get(p.getIndex()).getPositionY();
            		
            		if (MapGame.getMap().getTile(x, y).getType()==TileType.FOREST 
            				&& ((Forest)MapGame.getMap().getTile(x, y)).getProductionRessources()>0) {
            			ret += "<button class='button' onclick='action(\"collect\")'>Collect Ressources</button>";
            		}
            		if (p.getUnits().get(p.getIndex()).getDefence()<p.getUnits().get(p.getIndex()).getMaxDefence()) {
            			ret += "<button class='button' onclick='action(\"heal\")'>Heal</button>";
            		}
            		
            	    ret += "<button class='button' onclick='action(\"pass\")'>Pass</button>";
    			}else {
    				p.incrementAction();
    			}
        		
    		}
    		if (p.getTargetActionType()==TargetActionType.CITY && p.getCities().size()>0) {

    			if (p.getProductionPoints()>=City.costRecruitement 
    					&& selection.getUnit() == null) {
    				ret += "<button class='button' onclick='action(\"recruit\")'>Recruit a unit</button>";
    			}
    			ret += "<button class='button' onclick='action(\"pass\")'>Pass</button>";
    			
    			
    		}else {
    		}
    	}
    	
    	
    	return ret;
    }

	
    
	public void sendMessage(Session clientSession, String jsonString) {
	    // Fonction pour vérifier l'état de la session et envoyer le message
	    Runnable checkAndSend = new Runnable() {
	        @Override
	        public void run() {
	            if (clientSession != null && clientSession.isOpen()) {
	                clientSession.getAsyncRemote().sendText(jsonString);
	                System.out.println("Message envoyé : " + jsonString);
	            } else {
	                System.out.println("La session n'est pas encore ouverte, attente...");
	                try {
	                    Thread.sleep(100); // Attendre 100ms avant de réessayer
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	                run(); // Réessayer
	            }
	        }
	    };

	    // Lancer la vérification et l'envoi
	    checkAndSend.run();
	}

    
}