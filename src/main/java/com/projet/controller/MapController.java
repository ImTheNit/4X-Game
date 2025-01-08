package com.projet.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.projet.model.MapGame;
import com.projet.model.Player;
import com.projet.model.Soldier;

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
	    System.out.println("Taille de la liste des unités en java : " + Player.getPlayerByLogin(username).getUnits().size());
    }
    
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
	    System.out.println("Message reçu de " + message);
	    
        
	    for (Map.Entry<String, Session> entry : clients.entrySet()) {
            Session clientSession = entry.getValue();
            int clientId = Integer.parseInt(entry.getKey());
            String username = getListUsername(clientId);
            

            String player = getListUsername(clientId);
            Player p = Player.getPlayerByLogin(player);
	    	String html = MapGame.getMap().printJSP(p, MapGame.getMap().getTile(2, 2));
	    	String button = getButtonHTML(p);
	    	
	    	System.out.println(p);
	    	
	    	String escapedStringMap = html.replace("\"", "\\\"");
	    	String escapedStringButton = button.replace("\"", "\\\"");
	    	
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
	            System.out.println(jsonString);
	            
	            clientSession.getAsyncRemote().sendText(jsonString);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
           System.out.println("Unité "+ Player.getPlayerList(0).getUnits());
           System.out.println(Player.getPlayerByLogin("admin").getUnits().size());
           System.out.println("map :" +MapGame.getMap().getTile(0, 0).getUnit());
           
           System.out.println("Taille de la liste des unités en java sur récéption d'un message : " + Player.getPlayerByLogin(username).getUnits().size());
        }
	    
	    
		    /*
		    	//System.out.println(Map.getMap().printJSP(Player.getPlayerList(0), Map.getMap().getTile(0, 0)));
		    	
		    	JsonObject jsonObjectMessage = JsonParser.parseString(message).getAsJsonObject();
		    	
		    	
		    	//String player = jsonObjectMessage.get("session").getAsString();
		    	String player = getListUsername(clientId);
		    	//System.out.println(player);
		    	Player p = Player.getPlayerByLogin(player);
		    	String html = MapGame.getMap().printJSP(p, MapGame.getMap().getTile(0, 0));
		    	System.out.println(p);
		    	
		    	String escapedString = html.replace("\"", "\\\"");
		    	
		    	ObjectMapper objectMapper = new ObjectMapper();
		        ObjectNode jsonObject = objectMapper.createObjectNode();
		    	
		        jsonObject.put("html", escapedString);
		        
		        try {
		            String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
		            System.out.println(jsonString);
		            
		            //client.getAsyncRemote().sendText(jsonString);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    	//client.getAsyncRemote().sendText(Map.getMap().printJSP(Player.getPlayerList(0), Map.getMap().getTile(0, 0)));
		    	//client.getAsyncRemote().sendText(message);
		  */  
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
    	
    	String ret = "";
    	if (p!= null && Player.getActivePlayerIndex()==Player.getPlayerIndexByLogin(p.getLogin())) {
    		Soldier unit = Player.getPlayerList(Player.getActivePlayerIndex()).getUnits().get(0);
    		ret += "<button class='button' onclick='moveNorth()'>Move To North</button>"
    	    		+ "		<button class='button' onclick='moveWest()'>Move To West</button>"
    	    		+ "		<button class='button' onclick='moveSouth()'>Move To South</button>"
    	    		+ "		<button class='button' onclick='sendMessage()'>Move To East</button>";
    	}
    	
    	
    	return ret;
    }
    
}