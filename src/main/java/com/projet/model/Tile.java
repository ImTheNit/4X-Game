package com.projet.model;

public abstract class Tile {
	
    private int x; 
    private int y;
    private TileType type;
    private String image;
    private Soldier unit;

    /**
     * @name Constructor with all parameters
     * 
     * @param x : x position of the tile
     * @param y : y position of the tile
     * @param type : type of the tile (Plain, Mountain, Forest, City)
     * @param image : URL of the image related to the tile
     */
    public Tile(int x, int y, TileType type,String image) {
        this.x = x;
        this.y = y;
        this.type=type;
        this.image=image;
        
    }

    // Getters

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public TileType getType() {
        return type;
    }
    public String getImage() {
        return image;
    }
    public Soldier getUnit() {
		return unit;
	}

    // Setters

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    public void setType(TileType type) {
        this.type = type;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setUnit(Soldier unit) {
		this.unit = unit;
	}
    
    
    public String toString2() {
    	switch (type) {
    		case CITY:
    			return ("City    ");
    			
    		case PLAIN:
    			return ("Plain   ");
    			
    		case FOREST:    			
    			return ("Forest  ");
    			
    		case MOUTAIN:
    			return ("Mountain");
    			
    		default:
    			return ("Unknow  ");
    	}
    }

    /*
     * methods 
     */
    
    
    /**
     * 
     * @param map : the map in witch the tile is placed
     * @return a boolean corresponding to true/false if the Tile is a corner in the current map
     */
    boolean isCorner(MapGame map) {
    	if((x==map.getTiles().length-1 && y==map.getTiles()[0].length-1)
    			|| (x==0 && y==map.getTiles()[0].length-1)
    			|| (x==map.getTiles().length-1 && y==0)
    			|| (x==0 && y==0)){
    		return true;
    	}else {
    		return false;
    	}
    }

    
    
    /**
     * 
     * @param s : the soldier to add to the Tile
     */
    void addUnit(Soldier s) {
    	if(unit==null		// not already something
    			&& s != null
    			//&& s.getPositionX() == x
    			//&& s.getPositionY() == y
    			) {
    		this.setUnit(s);
    		MapGame.getMap().getTile(x, y).setUnit(unit);
    	}else {
    	}
    	
    }
    
    /**
     * 
     * @return the unit remove from the Tile
     */
    Soldier removeUnit() {
    	if (unit != null) {
	    	Soldier ret = this.getUnit();
	    	this.setUnit(null);
	    	return(ret);
    	}
    	return null;
    }
	
    /**
     * 
     * @return the owner of the Tile
     */
    public Player getOwnerTile() {
    	
    	//City
    	if (getType()==TileType.CITY) {
    		return ((City) this).getOwner();
    	}else if(getUnit()!=null && getUnit().getOwner()!=null) { //Owner found
    		return getUnit().getOwner();
    	}
    	return new Player();
    }
    
    
    @Override
	public boolean equals(Object o) {
    	if (o instanceof Tile) {
    		Tile t = (Tile)o;
    		return (this.getX()==t.getX())  && (this.getY()==t.getY());
    		
    	}else {
    		return false;
    	}
    	
    }
    
    @Override
	public String toString() {
		String ret;
		ret = "x :"+x;
		ret += " y :"+y;
		ret += " type :"+type;
		ret += " image :"+image;
		ret += " unit :"+unit;
		return ret;
	}
}
