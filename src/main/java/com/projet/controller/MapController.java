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

@ServerEndpoint(value = "/map" , configurator = HttpSessionConfigurator.class)
public class MapController {
    //private static final Set<Session> clients = new CopyOnWriteArraySet<>();
    private static Map<String, Session> clients = new ConcurrentHashMap<>();
    private static ArrayList<String> ListUsername = new ArrayList<String>();
    @OnOpen
    public void onOpen(Session session) {
	    //clients.add(session);
    	
    	HttpSession httpSession = (HttpSession) session.getUserProperties().get(HttpSession.class.getName());
        String username = (String) httpSession.getAttribute("user");
        
    	session.getUserProperties().put("usernameD", "NomUtilisateur");
	    clients.put(session.getId(), session);
	    
	    addListUserName(username);
	    System.out.println("Connexion ouverte, client : " + session.getId() + "info supp :" + username);
    }
    
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
	    System.out.println("Message reçu de " + message);
	    try {
	        Thread.sleep(50); // wait 50 millisecond
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	    
	    for (Map.Entry<String, Session> entry : clients.entrySet()) {
            Session clientSession = entry.getValue();
            int clientId = Integer.parseInt(entry.getKey());
            

            String player = getListUsername(clientId);
            Player p = Player.getPlayerByLogin(player);
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
	    	String button = getButtonHTML(p);
	    	
	    	
	    	String escapedStringMap = html.replace("\"", "\\\"");
	    	//String escapedStringButton = button.replace("\"", "\\\"");
	    	
	    	ObjectMapper objectMapper = new ObjectMapper();
	        ObjectNode jsonObject = objectMapper.createObjectNode();
	    	
	        jsonObject.put("html", escapedStringMap);
	        
	        //ajouter les stats pour les actualiser
	        jsonObject.put("battlesWon",p.getFightsWon());
	        jsonObject.put("soldiers",p.getUnits().size());
	        jsonObject.put("cities",p.getCities().size());
	        jsonObject.put("score",p.getScore());
	        jsonObject.put("ressources",p.getProductionPoints());
	        
	        jsonObject.put("button", button);
	        try {
	            String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
	            
	            clientSession.getAsyncRemote().sendText(jsonString);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }  
        }
	    
	}

    
    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
        System.out.println("session fermée : " + session.getId());
    }
    
    
    
    
    public String getListUsername(int index) {
    	if (ListUsername!= null && ListUsername.size()>=index) {
    		return ListUsername.get(index);
    	}
    	return "Void";
    }
    
    
    private void addListUserName(String s) {
    	ListUsername.add(s);
    }
    
    private String getButtonHTML(Player p ) {
    	// fonction générique
    	// stocker en session le type (Ville/soldat) ainsi que son index
    	// modifier la selection pour la cible de l'action
    	String ret = "";
    	if ( (p!= null)
    			&& (Player.getActivePlayerIndex()==Player.getPlayerIndexByLogin(p.getLogin()))
    			) {
    		
    		//case of a soldier
    		
    		if (p.getTargetActionType()==TargetActionType.SOLDIER) {

        		ret += "<button class='button' onclick='moveNorth()'>Move To North</button>"
        	    		+ "		<button class='button' onclick='moveWest()'>Move To West</button>"
        	    		+ "		<button class='button' onclick='moveSouth()'>Move To South</button>"
        	    		+ "		<button class='button' onclick='moveEast()'>Move To East</button>";
        		//position
        		int x = p.getUnits().get(p.getIndex()).getPositionX();
        		int y = p.getUnits().get(p.getIndex()).getPositionY();
        		
        		if (MapGame.getMap().getTile(x, y).getType()==TileType.FOREST 
        				&& ((Forest)MapGame.getMap().getTile(x, y)).getProductionRessources()>0) {
        			ret += "<button class='button' onclick='collect()'>Collect Ressources</button>";
        		}
        		if (p.getUnits().get(p.getIndex()).getDefence()<p.getUnits().get(p.getIndex()).getMaxDefence()) {
        			ret += "<button class='button' onclick='heal()'>Heal</button>";
        		}
        		
        	    ret += "<button class='button' onclick='pass()'>Pass</button>";
    		}
    		else if (p.getTargetActionType()==TargetActionType.CITY) {

    			if (p.getProductionPoints()>City.costRecruitement) {
    				ret += "<button class='button' onclick='recruitSoldier()'>Recruit a unit</button>";
    			}else {
    				ret += "<button class='button' onclick='pass()'>Pass</button>";
    			}
    			
    		}
    	}
    	
    	
    	return ret;
    }
    
    
    
}