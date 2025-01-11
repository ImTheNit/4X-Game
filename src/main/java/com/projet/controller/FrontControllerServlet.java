package com.projet.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.projet.model.Player;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/MainController", configurator = HttpSessionConfigurator.class)
public class FrontControllerServlet {
	private static Map<String, Session> clients = new ConcurrentHashMap<>();
    private static ArrayList<String> ListUsername = new ArrayList<String>();


    @OnOpen
    public void onOpen(Session session) {
	    //clients.add(session);
    	
    	HttpSession httpSession = (HttpSession) session.getUserProperties().get(HttpSession.class.getName());
        String username = (String) httpSession.getAttribute("user");
        
    	session.getUserProperties().put("usernameD", "NomUtilisateur");
	    getClients().put(session.getId(), session);
	    
	    addListUserName(username);
	    System.out.println("Connexion ouverte, client : " + session.getId() + " info supp :" + username);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("Message reçu : " + message);
        JSONObject type = new JSONObject(message);

        // Récupérer la valeur de la clé "type"
        String typeValue = type.getString("type");
        
        System.out.println("type : " + typeValue);
 
        if (typeValue.equals("join")
        		|| typeValue.equals("refresh")) {
        	try {
    	        Thread.sleep(50); // wait 50 millisecond
    	    } catch (InterruptedException e) {
    	        e.printStackTrace();
    	    }
            System.out.println("join or refresh");
            for (Map.Entry<String, Session> entry : getClients().entrySet()) {
                Session clientSession = entry.getValue();
                int clientId = Integer.parseInt(entry.getKey());
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode jsonObject = objectMapper.createObjectNode();
                

                if (checkEnd()) {
                	
                	jsonObject.put("redirection", 0);
                	String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                	clientSession.getAsyncRemote().sendText(jsonString);
                	System.out.println("test Fin de partie");
                	
                }else {
                	String player = getListUsername(clientId);
                    Player p = Player.getPlayerByLogin(player);
                    
                    jsonObject = MapController.onMessage(message, session,clientId,p,jsonObject);
                    jsonObject = FightController.onMessage(message, session, jsonObject);
                    String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                    clientSession.getAsyncRemote().sendText(jsonString);
                }
                

            }
        }else if (typeValue.equals("finalScore")){
        	System.out.println("finalScore");
        	for (Map.Entry<String, Session> entry : getClients().entrySet()) {
                Session clientSession = entry.getValue();
                int clientId = Integer.parseInt(entry.getKey());
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode jsonObject = objectMapper.createObjectNode();
                

                
            	String player = getListUsername(clientId);
                Player p = Player.getPlayerByLogin(player);
                
                jsonObject = ScoreController.onMessage(message, session,clientId,p,jsonObject);
                String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                System.out.println(jsonString);
                clientSession.getAsyncRemote().sendText(jsonString);
                
                

            }
        }
        
    }

    @OnClose
    public void onClose(Session session) {
        getClients().remove(session.getId());
        System.out.println("session:" +getClients());
        System.out.println("session fermée : " + session.getId());
        System.out.println("session:" +getClients());
    }

    private String parseAction(String message) {
        // Implémente la logique pour extraire l'action du message JSON
        // Par exemple, utiliser une bibliothèque JSON pour parser le message
        // et retourner la valeur de l'attribut "action"
        return "map"; // Remplace par la logique réelle
    }
    
    
    
    
    public static String getListUsername(int index) {
    	if (ListUsername!= null && ListUsername.size()>=index) {
    		return ListUsername.get(index);
    	}
    	return "Void";
    }
    
    
    private void addListUserName(String s) {
    	ListUsername.add(s);
    }
    /**
	 * @return the clients
	 */
	public static Map<String, Session> getClients() {
		return clients;
	}

	/**
	 * @param clients the clients to set
	 */
	public static void setClients(Map<String, Session> clients) {
		FrontControllerServlet.clients = clients;
	}
	
	/**
	 * 
	 * @return true if the game is over
	 * else return false;
	 */
	private boolean checkEnd() {
		int numberOfLiving = 0;
		int numberOfNonConnected = 0;
		for (int i = 0 ; i<Player.getPlayerList().size();i++) {
			//System.out.println("Player.getPlayerList(i).isDead() : "+Player.getPlayerList(i).isDead()  );
			if ( ! Player.getPlayerList(i).isDead() ) {  // player not dead
				numberOfLiving++;
				System.out.println(i+": numberOfLiving : " + numberOfLiving);
			}
			if ( Player.getPlayerList(i).getLogin()=="") {// player connected
				numberOfNonConnected++;
				System.out.println(i+": numberOfNonConnected : " + numberOfNonConnected);
			}
		}
		System.out.println("\nnumberOfLiving : " + numberOfLiving + "\nnumberOfNonConnected : " + numberOfNonConnected);
		
		if (numberOfLiving <= 1 && numberOfNonConnected < 3) { // un joueur ou moins et moins de 3 déco
			return true;
		}
		return false;
		/*
		if (numberOfLiving >= 2 
				|| numberOfNonConnected <3) {
			
			return false ;
		}
		return true;*/
	}
	
	
}