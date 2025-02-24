package com.projet.controller;

import java.io.IOException;

import com.projet.model.Player;
import com.projet.model.TargetActionType;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * this Class manage actions when called by the client
 */
@WebServlet("/ActionServlet")
public class ActionsController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("Action");
        System.out.println("action : " + action + " pour " + Player.getPlayerList(Player.getActivePlayerIndex()).getTargetActionType() + "[" + Player.getPlayerList(Player.getActivePlayerIndex()).getIndex() + "] of Player "+Player.getActivePlayerIndex()) ;                 
        boolean success=false;
        switch(action) {
        case "moveNorth":
        	success=moveNorth();
        	break;
        case "moveSouth":
        	success=moveSouth();
        	break;
        case "moveWest":
        	success=moveWest();
        	break;
        case "moveEast":
        	success=moveEast();
        	break;
        case "recruit":
        	success=recruit();
        	break;
        case "collect":
        	success=collect();
        	break;
        case "heal":
        	success=heal();
        	break;
        case "pass":
        	// do nothing
        	success=true;
        	break;
        }
        
        if (success) {// action successfully ends
        	Player.getPlayerList(Player.getActivePlayerIndex()).incrementAction();
        }
        
        response.getWriter().write("action: " + action );
    }
	
	/**
	 * move the current Soldier to the north
	 * @return true if successfully move
	 * else return false
	 */
	private boolean moveNorth() {
		boolean ret ;
		Player p =Player.getPlayerList(Player.getActivePlayerIndex());
		if (p.getTargetActionType()==TargetActionType.SOLDIER) {
			ret = p.getUnits().get(p.getIndex()).moveNorth();
			if (!ret) {
				return p.getUnits().get(p.getIndex()).attackNorth();
			}else {
				return ret; //True
			}
			
		}
		return false;
    }
	
	/**
	 * move the current Soldier to the south
	 * @return true if successfully move
	 * else return false
	 */
	private boolean moveSouth() {
		boolean ret ;
		Player p =Player.getPlayerList(Player.getActivePlayerIndex());
		if (p.getTargetActionType()==TargetActionType.SOLDIER) {
			ret = p.getUnits().get(p.getIndex()).moveSouth();
			if (!ret) {
				return p.getUnits().get(p.getIndex()).attackSouth();
			}else {
				return ret; //True
			}
		}
		return false;
    }
	
	/**
	 * move the current Soldier to the west
	 * @return true if successfully move
	 * else return false
	 */
	private boolean moveWest() {
		boolean ret ;
		Player p =Player.getPlayerList(Player.getActivePlayerIndex());
		if (p.getTargetActionType()==TargetActionType.SOLDIER) {
			ret = p.getUnits().get(p.getIndex()).moveWest();
			if (!ret) {
				return p.getUnits().get(p.getIndex()).attackWest();
			}else {
				return ret; //True
			}
		}
		return false;
    }
	
	/**
	 * move the current Soldier to the east
	 * @return true if successfully move
	 * else return false
	 */
	private boolean moveEast() {
		boolean ret ;
		Player p =Player.getPlayerList(Player.getActivePlayerIndex());
		if (p.getTargetActionType()==TargetActionType.SOLDIER) {
			ret = p.getUnits().get(p.getIndex()).moveEast();
			if (!ret) {
				return p.getUnits().get(p.getIndex()).attackEast();
			}else {
				return ret; //True
			}
		}
		return false;
    }
	
	/**
	 * collect ressources of the forest
	 * @return true if successfully collect
	 * else return false
	 */
	private boolean collect() {
		Player p =Player.getPlayerList(Player.getActivePlayerIndex());
		if (p.getTargetActionType()==TargetActionType.SOLDIER) {
			return p.getUnits().get(p.getIndex()).collectRessource();
		}
		return false;
    }
	
	/**
	 * heal the current Soldier
	 * @return true if successfully heal
	 * else return false
	 */
	private boolean heal() {
		Player p =Player.getPlayerList(Player.getActivePlayerIndex());
		if (p.getTargetActionType()==TargetActionType.SOLDIER) {
			return p.getUnits().get(p.getIndex()).heal();
		}
		return false;
    }
	/**
	 * recruit a soldier on the current City if enough ressources and space
	 * @return true if successfully recruit
	 * else return false
	 */
	private boolean recruit() {
		Player p =Player.getPlayerList(Player.getActivePlayerIndex());
		if (p.getTargetActionType()==TargetActionType.CITY) {
			return p.getCities().get(p.getIndex()).recruitUnit();
		}
		return false;
    }
	
}
