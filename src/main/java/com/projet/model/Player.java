package com.projet.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Player {
    private static ArrayList<Player> playerList;
    private static int ActivePlayerIndex =0;
	
	private String login;
    private int score;
    private int productionPoints;
    private ArrayList<Soldier> units;
    private ArrayList<City> cities;
    private boolean dead = false;

    /**
     * @name Constructor with all parameters
     * 
     * @param login : name of the player
     * @param score : score in the current game
     * @param productionPoints : production in the current game
     * @param units : soldier owned by the player
     * @param cities : cities owned by the player
     */
    public Player(String login, int score, int productionPoints, ArrayList<Soldier> units, ArrayList<City> cities) {
        if (Player.getPlayerList().size()<4) {
        	this.login = login;
            this.score = score;
            this.productionPoints = productionPoints;
            this.units = units;
            this.cities = cities;
            addPlayerList(this);
        }else {
        	
        }
    	
    }
    private static ArrayList<Player> initialisePlayerList() {
    	ArrayList<Player> liste = new ArrayList<Player>();
    	Player player = new Player("",0,0,new ArrayList<Soldier>(),new ArrayList<City>());
		Player player2 = new Player("",0,0,new ArrayList<Soldier>(),new ArrayList<City>());
		Player player3 = new Player("",0,0,new ArrayList<Soldier>(),new ArrayList<City>());
		Player player4 = new Player("",0,0,new ArrayList<Soldier>(),new ArrayList<City>());
		liste.addAll(Arrays.asList(player, player2, player3, player4));
		return liste;
	}
    
	/**
     * @name Constructor with arrays as parameter
     * 
     * @param s : array of soldier
     * @param c : array of city
     */
    public Player(ArrayList<Soldier> s,ArrayList<City> c) {
    	this("Default",0,0,s,c);
    }
    /**
     * @name Constructor without parameters
     */
    public Player() {
    	this("Default",0,0,new ArrayList<Soldier>(),new ArrayList<City>());
    }
    /**
     * @name Constructor with login, search in database for other infos
     * 
     * @param login : name of the player
     */
    public Player(String login) {
    	//load data from database
    	
    }
    
    
    
    
    /*
     *  Getters
     */
    /**
	 * @return the playerList
	 */
	public static ArrayList<Player> getPlayerList() {
		if(playerList == null) {
			playerList= new ArrayList<Player>();
			playerList = Player.initialisePlayerList();
		}
		return playerList;
	}
	public static Player getPlayerList(int index) {
		if(!Player.getPlayerList().isEmpty()) {
			if (index>=0 && index < playerList.size()) {
				return playerList.get(index);
			}
		}
		return null;
	}
    public String getLogin() {
        return login;
    }

    public int getScore() {
        return score;
    }

    public int getProductionPoints() {
        return productionPoints;
    }

    public ArrayList<Soldier> getUnits() {
        return units;
    }

    public ArrayList<City> getCities() {
        return cities;
    }
    public boolean isDead() {
		return dead;
	}
    /*
     *  Setters
     */
    
    /**
	 * @param playerList the playerList to set
	 */
	public static void setPlayerList(ArrayList<Player> playerList) {
		Player.playerList = playerList;
	}
    public void setLogin(String login) {
        this.login = login;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setProductionPoints(int productionPoints) {
        this.productionPoints = productionPoints;
    }

    
    
    
    
    
    public void addPlayerList(Player player) {
    	for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i) == player || i>=4) {
            	return;
            }
        }
    	playerList.add(player);
    }
    
    public void addUnits(Soldier unit) {
    	if (units==null) {
    		units = new ArrayList<Soldier>();
    	}
    	for (int i = 0; i < units.size(); i++) {
            if (units.get(i) == unit) {
            	return;
            }
        }
        units.add(unit);
    }
    

    public void addCities(City city) {
    	if (cities==null) {
    		cities = new ArrayList<City>();
    	}
    	for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i) == city) {
            	return;
            }
        }
        cities.add(city);
    }
    
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	
	/*
	 * methods
	 */
	
	/**
	 * Refresh the value of the attribute dead
	 */
	private void refreshDead() {
		if (!dead) {
			if(cities.size()==0 && units.size()==0) {
				dead=true;
			}
		}
	}
	
	private void removeUnitByIndex(int index) {
		this.units.remove(index);
	}
	private void removeCityByIndex(int index) {
		this.cities.remove(index);
	}
	
	void removeUnit(Soldier unit) {
		for (int i = 0; i < units.size(); i++) {
            if (units.get(i) == unit) {
                this.removeUnitByIndex(i);
            	return;
            }
        }
	}
	void removeCity(City city) {
		if(cities!=null) {
			for (int i = 0; i < cities.size(); i++) {
	            if (cities.get(i) == city) {
	                this.removeCityByIndex(i);
	            	return;
	            }
			}
		}
	}
	
	@Override
	public String toString() {
		String ret;
		ret = "login :"+login;
		ret += " score :"+score;
		ret += " productionPoints :"+productionPoints;
		ret += " dead :"+dead;
		return ret;
	}
	
	@Override
    public boolean equals(Object obj) {
		//System.out.println(obj);
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        //System.out.println("test2");
        Player player = (Player) obj;
        return score == player.score &&
               productionPoints == player.productionPoints &&
               dead == player.dead &&
               login == player.login;
    }
	
	/*
	 * @return index du joueur dans l'arraylist de joueurs
	 * @param login du joueur
	 * retourne -1 si le login ne correspond pas a un joueur et l'index du joueur dans le tableau sinon
	 */
	public static int getPlayerIndexByLogin(String login) {
		if(!Player.getPlayerList().isEmpty()) {
			for (int i=0; i<Player.getPlayerList().size();i++) {
				if(Player.getPlayerList().get(i).getLogin().equals(login)) {
					return i;
				}
			}
		}
		return -1;
	}
	
	/*
	 * @return index du joueur dans l'arraylist de joueurs
	 * @param login du joueur
	 * retourne l'entité "Player" correspondant au login
	 */
	public static Player getPlayerByLogin(String login) {
		return Player.getPlayerList(Player.getPlayerIndexByLogin(login));
	}
	
	/*
	 * @return true if the player is already a player in the game
	 */
	public static boolean playerAlreadyConnected(String login) {
		Player joueur = Player.getPlayerByLogin(login);
		if(joueur == null) {
			return false;
		}
		return true;
	}
	
}
