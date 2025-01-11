package com.projet.model;

import java.util.Random;

import com.projet.model.Creator.CreatorCity;
import com.projet.model.Creator.CreatorForest;
import com.projet.model.Creator.CreatorMountain;
import com.projet.model.Creator.CreatorPlain;

public class MapGame {
	private Tile[][] tiles;
	private static MapGame map;

    /**
     * @name Constructor without parameters, generate a 10x10 map
     * 
     * 
     */
    public MapGame() {
    	//this(4,4);
    	this(10,10);
        
        
    }
    
    /**
     * initialise map with a City and a Soldier in each corner 
     * @return the new Map
     */
    private static MapGame initialiseMap() {
		MapGame carte = new MapGame();

		//create Soldier
		Soldier s = new Soldier(0, 0, 10, Player.getPlayerList(0));
		Soldier s2 = new Soldier(9, 9, 10, Player.getPlayerList(1));
		Soldier s3 = new Soldier(9, 0, 10, Player.getPlayerList(2));
		Soldier s4 = new Soldier(0, 9, 10, Player.getPlayerList(3));

		//create Cities
		City c = (City)carte.getTile(0, 0);;
		City c2 = (City)carte.getTile(9, 9);
		City c3 = (City)carte.getTile(9, 0);
		City c4 = (City)carte.getTile(0, 9);
		
		//place Soldier on city
		c.setUnit(s);
		c2.setUnit(s2);
		c3.setUnit(s3);
		c4.setUnit(s4);
		
		//attribute new owner to the city
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
    public MapGame(int X) {
        this(X,X);
        
    }
    /**
     * @name Constructor with size of the map in parameter
     * @param X : X size of the map
     * @param Y : Y size of the map
     */
    public MapGame(int X,int Y) {
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
	public static MapGame getMap() {

		if (map==null) {
			map = new MapGame();
			MapGame.initialiseMap();
		}
		return map;
	}
	
	/**
	 * Singleton Pattern
	 * @param map the map to set
	 */
	public static void setMap(MapGame map) {
		MapGame.map = map;
	}
	
	
	@Override
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
    
    
    
    
    
    
    public String printJSP(Player player, Tile selection) {
    	String repoImage = new String("ressources/images/");
    	Tile[][] tile = getMap().getTiles();

    	if(tile==null) {
    		return null;
    	}else {
    		String ret = "";
    		for(int i = 0; i<tile.length;i++) {
    			ret += "<tr>";
        		for(int j = 0; j<tile[i].length;j++) {
        			Tile t = tile[i][j];
        			ret += "<td> <div class='image-container'>  ";
        			if (t!=null) {
        				
        				//background
        				ret += "<img src=" + repoImage + "plain.png alt = background/Plain width=100 height=100  class=img1>";
        				
        				//image of the Tile
        				ret += "<img src=" + repoImage + t.getImage() +" alt = Tile" /*+ tile[i][j].toString() */+ " width=100 height=100  class=img1>";
        				
        				//Soldier
        				if (t.getUnit()!= null) {
        					//ret += tiles[i][j].getUnit().getImage(); //convertir en html pour incruster l'image
        					ret += " <img src=" + repoImage + t.getUnit().getImage() +" alt = Soldier" + /*tile[i][j].getUnit().toString() + */" width=100 height=100  class=img2>";
        				}
        				
        				
        				//border
        				Player p = t.getOwnerTile();
        				
        				//selection
        				if( (selection != null) && (t==selection) ) {
        					ret += "<img src=" + repoImage + "borderSelectedTile.png alt = borderSelectedTile width=100 height=100  class=img3>";
        					
        					
        				//Current Player 
        				}else if(p.equals(player)){
        					ret += "<img src=" + repoImage + "borderActivePlayer.png alt = borderActivePlayer width=100 height=100  class=img3>";
        					
        					// Non connnected player
        				}else if (p.getLogin()==""){
        					//TODO cadre noir ? 
        					
        					//player 0
        				}else if (p.equals(Player.getPlayerList(0))){
            				ret += "<img src=" + repoImage + "borderPlayer0.png alt = borderPlayer0 width=100 height=100  class=img3>";
            				
        					
        					//Player 1
        				}else if (p.equals(Player.getPlayerList(1))){
        					ret += "<img src=" + repoImage + "borderPlayer1.png alt = borderPlayer1 width=100 height=100  class=img3>";
        					
        					
        					//Player 2
        				}else if (p.equals(Player.getPlayerList(2))){
        					ret += "<img src=" + repoImage + "borderPlayer2.png alt = borderPlayer2 width=100 height=100  class=img3>";
        					
        					
        					//Player 3
        				}else if (p.equals(Player.getPlayerList(3))){
        					ret += "<img src=" + repoImage + "borderPlayer3.png alt = borderPlayer3 width=100 height=100  class=img3>";	
        					
        				}
        				
        				
        				
        				//toolTip
        				String toolTip;
        				if (t.getUnit()!= null
        						|| t.getType()==TileType.CITY) {
        					toolTip = "<div class='tooltip'>";
        					
        					// Owner of the tile
        					if(p.getLogin()!=null		
        							&& p.getLogin()!="") {		// Non-Connected player
        						toolTip += "Owner : "+p.getLogin()+"<br>";
        					}
        					if (p.getLogin()=="") {
        						toolTip += "Owner : Not connected<br>";
        					}
        					
        					// HP of the Soldier
        					if (t.getUnit()!= null) {	
            					toolTip += "Soldier <br>HP : "+t.getUnit().getDefence()+"/"+t.getUnit().getMaxDefence()+"<br>";
            				}
        					
        					//HP of the City
            				if (t.getType()==TileType.CITY) {	
            					City c = (City) t;
            					toolTip += "City <br>HP : " +c.getDefensePoints()+"/"+c.getMaxDefensePoints();
            				}
            				toolTip += "</div>";
            				
            				ret += toolTip;
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
    
    /**
     * replace the Tile [x][y] by the Tile tile
     * @param x : position x of the Tile to replace
     * @param y	: position x of the Tile to replace
     * @param tile : new Tile to add
     */
    public static void refreshTile(int x, int y,Tile tile) {
    	if (tile!= null) {
    		MapGame.getMap().setTile(x, y, tile);
    	}
    }
}
