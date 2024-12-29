package com.projet.model;

import java.util.Random;

public class Soldier {
	
	/**
	 * position X,Y on the map
	 * remaining defence
	 */
	private int X;
	private int Y;
	private Map map;
	private int defence =10;
	private Player owner;
	private static final int sizeDice = 10;

	
	/**
	 * @name Constructor with all parameters
	 * 
	 * @param x : position x of the soldier
	 * @param y : position y of the soldier
	 * @param def : defence of the soldier
	 * @param owner : owner of the soldier
	 */
	public Soldier(int x, int y, int def,Player owner, Map map) {
		setPositionX(x);
		setPositionY(y);
		setDefence(def);
		setOwner(owner);
		setMap(map);
		map.getTile(X, Y).addUnit(this);
	}
	/**
	 * @name Constructor with position as parameters
	 * 
	 * @param x : position x of the soldier
	 * @param y : position y of the soldier
	 * @param owner : owner of the soldier
	 */
	public Soldier(int x, int y,Player owner, Map map) {
		setPositionX(x);
		setPositionY(y);
		setOwner(owner);
		setMap(map);
		map.getTile(X, Y).addUnit(this);
	}
	
	
	/**
	 * getters
	 */
	
	public int getPositionX() {
		return X;
	}
	public int getPositionY() {
		return Y;
	}
	public Map getMap() {
		return map;
	}
	public int getDefence() {
		return defence;
	}
	public Player getOwner() {
		return owner;
	}
	public static int getSizeDice() {
		return sizeDice;
	}
	/**
	 * setters
	 */
	
	public void setPositionX(int x) {
		X = x;
	}
	public void setPositionY(int y) {
		Y = y;
	}
	public void setMap(Map m) {
		map = m;;
	}
	public void setDefence(int def) {
		defence = def;
	}
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	
	/*
	 * deplacement methods
	 */
	
	/**
	 * TODO
	 * @return true if the tile [x][y] is reachable
	 */
	private boolean canReachXY(int x, int y) {
		//case of non reachable tile
		if ( x < 0 || y < 0 							// case of out of map
				|| x > map.getTiles().length -1			//
				|| y > map.getTiles()[0].length -1			//
				
				) {

			return false;
		}
		// if there, then reachable tile
		return true;
	}
	
	/**
	 * 
	 * @return true if the soldier can move North (up)
	 */
	private boolean canMoveNorth() {
		return canReachXY(X,Y-1);
	}
	/**
	 * move the soldier to the North (up) if possible
	 */
	private boolean moveNorth() {
		if (canMoveNorth()) {
			//add to the new tile
			map.getTile(X, Y-1).addUnit(this);
			//remove from the old one
			map.getTile(X, Y).removeUnit();
			Y = Y - 1;
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * 
	 * @return true if the soldier can move West (right)
	 */
	private boolean canMoveWest() {
		return canReachXY(X-1,Y);
	}
	/**
	 * move the soldier to the West (right)
	 */
	private void moveWest() {
		if (canMoveWest()) {
			map.getTile(X - 1, Y ).addUnit(this);
			map.getTile(X, Y).removeUnit();
			X = X - 1;
		}
	}
	
	//TODO
	private boolean canMoveSouth() {
		return canReachXY(X,Y+1);
	}
	private void moveSouth() {
		if (canMoveSouth()) {
			map.getTile(X, Y + 1).addUnit(this);
			map.getTile(X, Y).removeUnit();
			Y = Y + 1;
		}
	}
	
	
	
	//TODO
	private boolean canMoveEast() {
		return canReachXY(X-1,Y);
	}
	private void moveEast() {
		if (canMoveEast()) {
			map.getTile(X-1, Y).addUnit(this);
			map.getTile(X, Y).removeUnit();
			X = X - 1;
		}
	}
	
	
	/*
	 * fights methods
	 */
	
	private void attack(Object target) {
		if (target instanceof Soldier) {
			attackSoldier((Soldier)target);
		}else if(target instanceof City){
			attackCity((City)target);
		}
	}
	
	//TODO add damaged dealt to score
	/**
	 * deal damage and kill the target if enough damage is done
	 * @param target : the soldier target by the attack
	 */
	private void attackSoldier(Soldier target) {
		if (target != null) {
			Random random = new Random();
			int powerOfHit = random.nextInt(getSizeDice()); // generate a number between 0 and sizeDice
			
			if (powerOfHit>=target.getDefence()) {
				target.kill();
				
			}else {
				target.setDefence(target.getDefence()-powerOfHit); // deal damage
			}
		}
	}
	
	/**
	 * deal damage and capture the city if enough damage is done
	 * @param target : city target by the attack
	 */
	private void attackCity(City target) {
		if (target != null) {
			Random random = new Random();
			int powerOfHit = random.nextInt(getSizeDice()); // generate a number between 0 and sizeDice
			
			if (powerOfHit>=target.getDefensePoints()) {
				target.newOwner(owner); 
								
			}else {
				target.setDefensePoints(target.getDefensePoints()-powerOfHit); // deal damage
			}
		}
	}
	
	/**
	 * remove unit from his player and the tile it's placed on
	 */
	private void kill() {
		if (owner!=null && map!=null) {
			owner.removeUnit(this);
			map.getTile(X, Y).removeUnit();
		}
	}
	
}
