package com.projet.controller;


import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.projet.model.Map;
import com.projet.model.Player;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/map")
public class MapController {
    private static final Set<Session> clients = new CopyOnWriteArraySet<>();
    
    @OnOpen
    public void onOpen(Session session) {
	    clients.add(session);
	    System.out.println("Connexion ouverte, client : " + session.getId());
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
	    System.out.println("Message re�u de " + message);
	    for (Session client : clients) {
	    	System.out.println(client);
	    	System.out.println(client.isOpen());
		    if (client.isOpen()) {
		    	System.out.println(Map.getMap().printJSP(Player.getPlayerList(0), Map.getMap().getTile(0, 0)));
		    	
		    	//client.getAsyncRemote().sendText(Map.getMap().printJSP(Player.getPlayerList(0), Map.getMap().getTile(0, 0)));
		    	client.getAsyncRemote().sendText(message);
		    }
	    }
    }
    
    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
        System.out.println("session ferm�e : " + session.getId());
    }
}