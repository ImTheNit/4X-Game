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


/**
 * this class manage all query from clients, and calls other controller to proceed to the right action
 */
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

        String typeValue = type.getString("type");
 
        if (typeValue.equals("join")
        		|| typeValue.equals("refresh")) { 	// new joiners or action
        	try {
    	        Thread.sleep(50); // wait 50 millisecond
    	    } catch (InterruptedException e) {
    	        e.printStackTrace();
    	    }
            for (Map.Entry<String, Session> entry : getClients().entrySet()) {	//for each clients
                Session clientSession = entry.getValue();
                int clientId = Integer.parseInt(entry.getKey());
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode jsonObject = objectMapper.createObjectNode();
                

                if (checkEnd()) { // end of the game
                	
                	jsonObject.put("redirection", 0);	// send a redirection signal
                	String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                	clientSession.getAsyncRemote().sendText(jsonString);
                	
                }else {		// game continue
                	String player = getListUsername(clientId);
                    Player p = Player.getPlayerByLogin(player);
                    
                    jsonObject = MapController.onMessage(message, session,clientId,p,jsonObject); // generate html code for map,buttons and stats
                    jsonObject = FightController.onMessage(message, session, jsonObject);	// generate html code for pop up fight
                    
                    String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                    
                    clientSession.getAsyncRemote().sendText(jsonString);	//	send to clients
                }
                

            }
        }else if (typeValue.equals("finalScore")){	// called by final score summary page
        	for (Map.Entry<String, Session> entry : getClients().entrySet()) {	//for each clients
                Session clientSession = entry.getValue();
                int clientId = Integer.parseInt(entry.getKey());
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode jsonObject = objectMapper.createObjectNode();
                

                
            	String player = getListUsername(clientId);
                Player p = Player.getPlayerByLogin(player);
                
                jsonObject = ScoreController.onMessage(message, session,clientId,p,jsonObject); //generate html code for scores
                String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                
                clientSession.getAsyncRemote().sendText(jsonString);	//	send to clients
                
                

            }
        }
        
    }

    @OnClose
    public void onClose(Session session) {
        getClients().remove(session.getId());
        System.out.println("session fermée : " + session.getId());
    }

    
    
    
    /**
     * 
     * @param index of the username to get
     * @return the name of the user
     */
    public static String getListUsername(int index) {
    	if (ListUsername!= null && ListUsername.size()>=index) {
    		return ListUsername.get(index);
    	}
    	return "Void";
    }
    
    /**
     * add new user to the game
     * @param s : username
     */
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
			if ( ! Player.getPlayerList(i).isDead() ) {  // player not dead
				numberOfLiving++;
			}
			if ( Player.getPlayerList(i).getLogin()=="") {// player not connected
				numberOfNonConnected++;		
			}
		}
		if (numberOfLiving <= 1 && numberOfNonConnected < (Player.getPlayerList().size()-1)) { // at least 1 living player and less than 3 non connected player
			return true;
		}
		return false;
	}
	
	
}