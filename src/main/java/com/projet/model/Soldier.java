package com.projet.model;

import java.util.Random;

import com.projet.controller.FightController;

public class Soldier {
	
	/**
	 * position X,Y on the map
	 * remaining defense
	 */
	private int X;
	private int Y;
	private int maxDefence = 10;
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
	public Soldier(int x, int y, int def,Player owner) {
		setPositionX(x);
		setPositionY(y);
		setDefence(def);
		setMaxDefence(def);
		setOwner(owner);
		MapGame.getMap().getTile(X, Y).addUnit(this);
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
		MapGame.getMap().getTile(X, Y).addUnit(this);
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
	public int getMaxDefence() {
		return maxDefence;
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
	public String getImage() {
		return "soldier.png";
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

	public void setMaxDefence(int maxDefence) {
		this.maxDefence = maxDefence;
	}
	public void setDefence(int def) {
		defence = def;
	}
	public void setOwner(Player owner) {
		if (this.getOwner()!=null) {//case the soldier already have an owner
			this.getOwner().getUnits().remove(this);
		}
		this.owner = owner;
		
		this.getOwner().addUnits(this);
		
	}

	
	/*
	 * action methods
	 */
	//deplacement 
	public boolean moveNorth() {
		return move(X - 1, Y );
	}
	public boolean moveSouth() {
		return move(X + 1, Y );
	}
	public boolean moveWest() {
		return move(X ,Y - 1);
	}
	public boolean moveEast() {
		return move(X, Y + 1);
	}
	
	//attack 
	public boolean attackNorth() {
		boolean ret = attack(X - 1, Y );
		moveNorth();
		return ret;
	}
	public boolean attackSouth() {
		boolean ret = attack(X + 1, Y );
		moveSouth();
		return ret;
	}
	public boolean attackWest() {
		boolean ret = attack(X ,Y - 1);
		moveWest();
		return ret;
	}
	public boolean attackEast() {
		boolean ret = attack(X, Y + 1);
		moveEast();
		return ret;
	}
	
	/**
	 * heal the unit of 30 % of it's max HP
	 */
	public boolean heal() {
		int valueHeal = (int) Math.ceil(getMaxDefence()*0.3);
		if (getDefence() + valueHeal > getMaxDefence()) {
			setDefence(getMaxDefence());
		}else {
			setDefence(getDefence() + valueHeal);
		}
		return true;
	}
	
	//collect ressource (Forest)

	/** 
	 * @return the amount of collected ressources from the forest
	 * @return 0 if the tile is not a Forest or no ressources collectable
	 */
	public int amountRessources() {
		if (MapGame.getMap().getTile(X, Y).getType()==TileType.FOREST 
				&& ((Forest) MapGame.getMap().getTile(X, Y)).getProductionRessources() > 0 ){
			 return ((Forest) MapGame.getMap().getTile(X, Y)).collectRessources();
			}else {
				return 0;
			}
	}
	
	public boolean collectRessource() {
		int r = amountRessources();
		if (r!=0) {
			getOwner().setProductionPoints(getOwner().getProductionPoints() + r);
			return true;
		}else {
			return false;
		}
	}
	
	
	/*
	 * deplacement methods
	 */

	/**
	 * move to the target tile
	 * 
	 * @param x : position x of the target tile 
	 * @param y : position y of the target tile
	 * @return true if successfully move, false else
	 */
	private boolean move(int x,int y) {
		//out of MapGame.getMap(), mountain, not ally city or not free Tile -> return false 
		if (!validTarget(x, y)		// not a mountain and in the range of the MapGame.getMap()
				|| (MapGame.getMap().getTile(x, y) instanceof City && ((City) MapGame.getMap().getTile(x, y)).getOwner() != this.getOwner()) //destination tile is a City occuped by someone else (or nobody)
				|| (MapGame.getMap().getTile(x,y).getUnit()!= null ) //no unit on the destination tile
				) {
			return false;
		}else {// deplacement
			
			MapGame.getMap().getTile(x, y).addUnit(this);
			
			MapGame.getMap().getTile(X, Y).removeUnit();
			setPositionX(x);
			setPositionY(y);
			System.out.println("deplacement vers [" + x + "][" + y +"]");
			return true;
		}
	}
	
	/**
	 * 
	 * @return true if the tile [x][y] is reachable and false if not (for deplacement or action)
	 */
	private boolean validTarget(int x, int y) {
		//case of non reachable tile :out of MapGame.getMap(), mountain 
		
		if ( x < 0 || y < 0 								// case of out of MapGame.getMap()
				|| x > MapGame.getMap().getTiles().length -1				//
				|| y > MapGame.getMap().getTiles()[0].length -1			//
				|| MapGame.getMap().getTile(x, y).getType()== TileType.MOUTAIN	// case of moutain
				) {

			return false;	//out of MapGame.getMap() or moutain
		}
		// if there, then reachable tile
		return true;
	}
	
	
	
	
	/*
	 * fights methods
	 */
	
	/**
	 * 
	 * @param x : Position x of the target 
	 * @param y : Position y of the target 
	 */
	private boolean attack (int x, int y) {
		// ennemy unit
		// enemy city
		Tile target = MapGame.getMap().getTile(x, y);
		if (!validTarget(x, y)) {
			return false;
		}
		if (target.getType()==TileType.CITY 
				&& ((((City)target).getOwner() == null)
					||(((City)target).getOwner() != owner))){ // ennemy or neutral city
			return attack((City)target);
		}else if (target.getUnit() != null						// unit on the target Tile
				&& target.getUnit().getOwner()!= owner) {		// from another player
			return attack(target.getUnit());
			
		}
		return false;
	}
	/**
	 * 
	 * @param target : Soldier or City to target 
	 */
	private boolean attack(Object target) {
		if (target instanceof Soldier) {
			return attackSoldier((Soldier)target);
		}else if(target instanceof City){
			return attackCity((City)target);
		}else {
			//wrong type for target
			System.out.println("Wrong type for target");
			return false;
		}
	}
	
	//TODO add damaged dealt to score
	/**
	 * deal damage and kill the target if enough damage is done
	 * @param target : the soldier target by the attack
	 */
	private boolean attackSoldier(Soldier target) {
		if (target != null) {
			FightController.setIsFight(true);
			Random random = new Random();
			int powerOfHit = random.nextInt(getSizeDice()); // generate a number between 0 and sizeDice
			FightController.setLastDamageDealt(powerOfHit);
			if (powerOfHit>=target.getDefence()) {
				target.kill();
				FightController.setRemainingHp(0);
				winFight();
			}else {
				target.setDefence(target.getDefence()-powerOfHit); // deal damage
				FightController.setRemainingHp(target.getDefence());
			}
			return true;
		}
		return false;
	}
	
	/**
	 * deal damage and capture the city if enough damage is done
	 * @param target : city target by the attack
	 */
	private boolean attackCity(City target) {
		System.out.println("attackCity");
		if (target != null) {
			FightController.setIsFight(true);
			Random random = new Random();
			int powerOfHit = random.nextInt(getSizeDice()); // generate a number between 0 and sizeDice
			
			if (target.getUnit() == null) { //no defensive unit
				FightController.setLastDamageDealt(powerOfHit);
				System.out.println("No Defense hit : "+powerOfHit + " City : "+target.getDefensePoints());
				if (powerOfHit>=target.getDefensePoints()) {
					target.newOwner(owner); 
					FightController.setRemainingHp(0);
					winFight();
				}else {
					target.setDefensePoints(target.getDefensePoints()-powerOfHit); // deal damage
					FightController.setRemainingHp(target.getDefensePoints());
				}
			}else { //defensive unit
				attackSoldier(target.getUnit());
			}
			return true;
		}
		return false;
	}
	
	/**
	 * remove unit from his player and the tile it's placed on
	 */
	private void kill() {
		if (owner!=null && MapGame.getMap()!=null) {
			owner.removeUnit(this);
			MapGame.getMap().getTile(X, Y).removeUnit();
		}
	}
	
	public String toString() {
		String ret;
		ret = "X : "+ X;
		ret += " Y : "+ Y;
		ret += " maxDefence : "+ maxDefence;
		ret += " defence : "+ defence;
		ret += " owner : "+ owner;
		ret += " sizeDice : "+ sizeDice;
		
		return ret;
	}
	
	private void winFight() {
		getOwner().setFightsWon(getOwner().getFightsWon()+1);
	}
	
}
