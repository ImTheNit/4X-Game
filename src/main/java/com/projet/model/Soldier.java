package com.projet.model;

public class Soldier {
	
	/**
	 * position X,Y on the map
	 * remaining defence
	 */
	private int X;
	private int Y;
	private int defence =10;
	private Player owner;
	
	/**
	 * @name Constructor with all parameters
	 * 
	 * @param x : position x of the soldier
	 * @param y : position y of the soldier
	 * @param def : defence of the soldier
	 * @param owner : owner of the soldier
	 */
	public Soldier(int x, int y, int def,Player owner) {
		setPositionX(x);
		setPositionY(y);
		setDefence(def);
		setOwner(owner);
	}
	/**
	 * @name Constructor with position as parameters
	 * 
	 * @param x : position x of the soldier
	 * @param y : position y of the soldier
	 * @param owner : owner of the soldier
	 */
	public Soldier(int x, int y,Player owner) {
		setPositionX(x);
		setPositionY(y);
		setOwner(owner);
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
	public int getDefence() {
		return defence;
	}
	public Player getOwner() {
		return owner;
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
	public void setDefence(int def) {
		defence = def;
	}
	public void setOwner(Player owner) {
		this.owner = owner;
	}
}
