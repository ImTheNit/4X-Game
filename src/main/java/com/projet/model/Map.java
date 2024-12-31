package com.projet.model;

import java.util.Random;

import com.projet.model.Creator.CreatorCity;
import com.projet.model.Creator.CreatorForest;
import com.projet.model.Creator.CreatorMountain;
import com.projet.model.Creator.CreatorPlain;

public class Map {
	private Tile[][] tiles;
	private static Map map;

    /**
     * @name Constructor without parameters, generate a 10x10 map
     * 
     * 
     */
    public Map() {
        this(10,10);
        
    }
    /**
     * @name Constructor with size of the map in parameter (square)
     * @param X
     */
    public Map(int X) {
        this(X,X);
        
    }
    /**
     * @name Constructor with size of the map in parameter
     * @param X : X size of the map
     * @param Y : Y size of the map
     */
    public Map(int X,int Y) {
        tiles = new Tile[X][Y];
        initialiseTiles();        
    }

    /**
     * @name initialise a map with given probabilities for each tiles and city in each corner 
     * 
     * @param probaPlain : probability of having a Plain
     * @param probaCity : probability of having a City
     * @param probMountain : probability of having a Mountain
     * @param probaForest : probability of having a Forest
     */
    private void initialiseTiles(int probaPlain, int probaCity, int probMountain, int probaForest) {
    	for(int i = 0; i<tiles.length;i++) {
    		for(int j = 0; j<tiles[i].length;j++) {
    			Random random = new Random();
    			tiles[i][j] = new CreatorPlain().create(i, j);
    			if(tiles[i][j].isCorner(this)) {
    				CreatorCity creator = new CreatorCity(); 
    	        	tiles[i][j] = creator.create(i,j);
	    			}else {
	    	        int randomValue = random.nextInt(100); // Generate a integer between 0 and 99
	
	    	        if (randomValue < probaPlain) { // Create a plain at this position
	    	        	
	    	        	CreatorPlain creator = new CreatorPlain(); 
	    	        	tiles[i][j] = creator.create(i,j);
	    	        	
	    	        } else if (randomValue < probaPlain + probaCity) { // Create a city at this position
	
	    	        	CreatorCity creator = new CreatorCity(); 
	    	        	tiles[i][j] = creator.create(i,j);
	    	        	
	    	        } else if (randomValue < probaPlain +probaCity + probMountain) { // Create a mountain at this position
	    	        	
	    	        	CreatorMountain creator = new CreatorMountain(); 
	    	        	tiles[i][j] = creator.create(i,j);
	    	        	
	    	        } else { // Create a forest at this position
	
	    	        	CreatorForest creator = new CreatorForest(); 
	    	        	tiles[i][j] = creator.create(i,j);
	    	        	
	    	        }
	    		}
        	}
    	}
    }
    
    /**
     * @name initialise a map with default probabilities for each tiles
     */
    private void initialiseTiles() {
    	initialiseTiles(70,10,10,10);
    }
    
    /*
     *  Getter for tiles
     */
    public Tile[][] getTiles() {
        return tiles;
    }
    public Tile getTile(int x, int y) {
        if (x >= 0 && x < 10 && y >= 0 && y < 10) {
            return tiles[x][y];
        }
        return null;
    }
    

    /*
     *  Setter for a specific tile
     */
    public void setTile(int x, int y, Tile tile) {
        if (x >= 0 && x < 10 && y >= 0 && y < 10) {
            tiles[x][y] = tile;
        }
    }

    

    /**
	 * @return the map
	 */
	public static Map getMap() {
		return map;
	}
	
	/**
	 * @param map the map to set
	 */
	public static void setMap(Map map) {
		Map.map = map;
	}
	
	public String toString() {
    	if(tiles==null) {
    		return null;
    	}else {
    		String ret = "";
    		for(int i = 0; i<tiles.length;i++) {
    			ret += "\n";
        		for(int j = 0; j<tiles[i].length;j++) {
        			if (tiles[i][j]!=null) {
        				ret += tiles[i][j].toString();
        			}else {
        				ret += "NONE";
        			}
        			ret += " | ";
        		}
        	}
    		return ret;
    	}
		
    }
    
    
    
    
    
    
    public String printJSP() {
    	if(tiles==null) {
    		return null;
    	}else {
    		String ret = "";
    		for(int i = 0; i<tiles.length;i++) {
    			ret += "<tr>";
        		for(int j = 0; j<tiles[i].length;j++) {
        			if (tiles[i][j]!=null) {
        				ret += "<td>"+tiles[i][j].getImage()+"</td>";
        			}else {
        				ret += "<td>NONE</td>";
        			}
        		}
        	}
    		return ret;
    	}
    }
    
    /**
     * @name return the vertical size of the map
     */
    public int XSize() {
    	return this.getTiles().length;
    }
    
    /**
     * @name return the horizontal size of the map
     */
    public int YSize() {
    	return this.getTiles()[0].length;
    }
}
