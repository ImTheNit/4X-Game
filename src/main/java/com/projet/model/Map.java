package com.projet.model;

import java.util.Random;

import com.projet.model.Creator.CreatorCity;
import com.projet.model.Creator.CreatorForest;
import com.projet.model.Creator.CreatorMountain;
import com.projet.model.Creator.CreatorPlain;

public class Map {
	private Tile[][] tiles;
	private static Map map = Map.initialiseMap();

    /**
     * @name Constructor without parameters, generate a 10x10 map
     * 
     * 
     */
    public Map() {
        this(10,10);
        
    }
    private static Map initialiseMap() {
		Map carte = new Map();
		Soldier s = new Soldier(9, 9, 0, Player.getPlayerList(0), Map.getMap());
		Soldier s2 = new Soldier(9, 9, 0, Player.getPlayerList(1), Map.getMap());
		Soldier s3 = new Soldier(9, 9, 0, Player.getPlayerList(2), Map.getMap());
		Soldier s4 = new Soldier(9, 9, 0, Player.getPlayerList(3), Map.getMap());
		
		City c = (City)Map.getMap().getTile(0, 0);;
		City c2 = (City)Map.getMap().getTile(9, 0);
		City c3 = (City)Map.getMap().getTile(9, 9);
		City c4 = (City)Map.getMap().getTile(0, 9);
		
		c.setUnit(s);
		c2.setUnit(s2);
		c3.setUnit(s3);
		c4.setUnit(s4);
		
		c.newOwner(Player.getPlayerList(0));
		c2.newOwner(Player.getPlayerList(1));
		c3.newOwner(Player.getPlayerList(2));
		c4.newOwner(Player.getPlayerList(3));
		return carte;
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
    
    
    
    
    
    // TODO
    public String printJSP(Player player, Tile selection) {
    	String repoImage = new String("ressources/images/");
    	if(tiles==null) {
    		return null;
    	}else {
    		String ret = "";
    		for(int i = 0; i<tiles.length;i++) {
    			ret += "<tr>";
        		for(int j = 0; j<tiles[i].length;j++) {
        			ret += "<td> <div class='image-container'>  ";
        			if (tiles[i][j]!=null) {
        				
        				//background
        				ret += "<img src=" + repoImage + "plain.png alt = background/Plain width=100 height=100  class=img1>";
        				
        				//image of the Tile
        				ret += "<img src=" + repoImage + tiles[i][j].getImage() +" alt = " + tiles[i][j].toString() + " width=100 height=100  class=img1>";
        				
        				//Soldier
        				if (tiles[i][j].getUnit()!= null) {
        					//ret += tiles[i][j].getUnit().getImage(); //convertir en html pour incruster l'image
        					ret += " <img src=" + repoImage + tiles[i][j].getUnit().getImage() +" alt = " + tiles[i][j].getUnit().toString() + " width=100 height=100  class=img2>";
        				}
        				
        				//border
        				
        				//selection
        				if( (selection != null) && (tiles[i][j]==selection) ) {
        					ret += "<img src=" + repoImage + "borderSelectedTile.png alt = borderSelectedTile width=100 height=100  class=img3>";
        				
        				//Current Player 
        				}else if(tiles[i][j].getOwnerTile().equals(player)){
        					ret += "<img src=" + repoImage + "borderActivePlayer.png alt = borderActivePlayer width=100 height=100  class=img3>";
        				
        					//player 0
        				}else if (tiles[i][j].getOwnerTile().equals(Player.getPlayerList(0))){
            				ret += "<img src=" + repoImage + "borderPlayer0.png alt = borderPlayer0 width=100 height=100  class=img3>";
            				
        					
        					//Player 1
        				}else if (tiles[i][j].getOwnerTile().equals(Player.getPlayerList(1))){
        					ret += "<img src=" + repoImage + "borderPlayer1.png alt = borderPlayer1 width=100 height=100  class=img3>";
        					
        					//Player 2
        				}else if (tiles[i][j].getOwnerTile().equals(Player.getPlayerList(2))){
        					ret += "<img src=" + repoImage + "borderPlayer2.png alt = borderPlayer2 width=100 height=100  class=img3>";
        					
        					//Player 3
        				}else if (tiles[i][j].getOwnerTile().equals(Player.getPlayerList(3))){
        					ret += "<img src=" + repoImage + "borderPlayer3.png alt = borderPlayer3 width=100 height=100  class=img3>";	
        				}
        				
        				
        			}else {
        				ret += "NONE";
        			}
        			ret += "</div></td>";
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
