package com.projet.model;

public class City extends Tile implements attackable{

    private int defensePoints;
    private int maxDefensePoints;
    private static final int productionRessources = 1;
    public static final int costRecruitement = 6;
    private Player owner;


    /**
     * @name Constructor with all parameters
     * 
     * @param x : position x of the city
     * @param y : position y of the city
     * @param defensePoints : defensive point of the city
     * @param owner : name of the owner of the city
     */
    public City(int x, int y, int maxDefensePoints, Player owner) {
        super(x,y,TileType.CITY,"city.png");
        this.maxDefensePoints = maxDefensePoints;
        this.defensePoints = maxDefensePoints;
        this.owner = owner;
    }
    /**
     * @name Constructor with position and player as parameters
     * 
     * @param x : position x of the city
     * @param y : position y of the city
     * @default defensePoints 10 : defensive point of the city
     * @param owner : name of the owner of the city
     */
    public City(int x, int y, Player owner) {
    	this(x,y,10,owner);
    }
    /**
     * @name Constructor with position as parameters
     * 
     * @param x : position x of the city
     * @param y : position y of the city
     */
    public City(int x, int y) {
    	this(x,y,new Player());
    }
    /**
     * @name Constructor with position and defense point as parameters
     * 
     * @param x : position x of the city
     * @param y : position y of the city
     * @para defensePoints : defensive point of the city
     */
    public City(int x,int y,int defensePoints) {
    	this(x,y,defensePoints,new Player());
    }


    /*
     *  Getters
     */

    public int getDefensePoints() {
        return defensePoints;
    }

    public Player getOwner() {
        return owner;
    }
    public int getMaxDefensePoints() {
		return maxDefensePoints;
	}
    @Override
    public int getIndex() {	// in the owner arraylist
		if (owner != null 
				&& owner.getCities() != null
				&& owner.getCities().size() > 0) {
			return owner.getCities().indexOf(this);
		}else {
			return -1;
		}	
	}
	@Override
	public String getOwnerName() {
		if (owner != null) {
			return owner.getLogin();
		}else {
			return "";
		}
	}
	@Override
	public String getTypeAttackable() {
		return "City";
	}
    /*
     *  Setters
     */

    public void setDefensePoints(int defensePoints) {
        this.defensePoints = defensePoints;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
	public void setMaxDefensePoints(int maxDefensePoints) {
		this.maxDefensePoints = maxDefensePoints;
	}
    
    /*
     * methods
     */
    
    /**
     * remove reference from old owner, add reference to the new one and change owner of the city
     * 
     * @param player : new owner of the city
     */
    public void newOwner(Player player) {
    	//control that new owner is not the same as the old one
    	if (player==owner) {
    		return ;
    	}
    	//remove city from owner ArrayList 
    	if (owner!=null) {
    		owner.removeCity(this);
    	}
    	

    	//add city to new owner
    	player.addCities(this);
    	//add new player as owner of the city
    	this.setOwner(player);
    	//restore the defensePoint of the city
    	this.restoreDefensePoint();
    	//refresh map
    	MapGame.refreshTile(getX(), getY(), this);	
    }
    
    /**
     * restore DefensePoint of the city to it's maximum
     */
    private void restoreDefensePoint() {
    	defensePoints = maxDefensePoints;
    }
    
    /**
     * give ressource to the owner of the city
     */
    void earnRessource() {
    	if(owner!=null) {
    		owner.setProductionPoints(owner.getProductionPoints()+productionRessources);
    	}
    }

    
    /**
     * Recruit a soldier and place it into the city
     * @return true if successfully recruited
     * else return false
     */
    public boolean recruitUnit() {
    	if (MapGame.getMap().getTile(getX(), getY()).getUnit() == null
    			&& getOwner()!= null
    			&& getOwner().getProductionPoints()>=City.costRecruitement) {
    		//assign soldier
    		Soldier s = new Soldier(getX(), getY(), getOwner());
    		this.setUnit(s);
    		//pay the cost to recruit
    		getOwner().setProductionPoints(getOwner().getProductionPoints() - City.costRecruitement);
    		return true;
    	}
    	return false;
    		
    }
    @Override
	public String toString() {
		String ret = super.toString();
		ret += "defensePoints :"+defensePoints;
		ret += " maxDefensePoints :"+maxDefensePoints;
		ret += " productionRessources :"+productionRessources;
		ret += " owner :"+owner;
		return ret;
	}


   
}
