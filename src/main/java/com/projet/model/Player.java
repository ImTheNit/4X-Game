package com.projet.model;

import java.util.ArrayList;

public class Player {

	
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
        this.login = login;
        this.score = score;
        this.productionPoints = productionPoints;
        this.units = units;
        this.cities = cities;
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
    public void setLogin(String login) {
        this.login = login;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setProductionPoints(int productionPoints) {
        this.productionPoints = productionPoints;
    }

    public void addUnits(Soldier unit) {
    	for (int i = 0; i < units.size(); i++) {
            if (units.get(i) == unit) {
            	return;
            }
        }
        units.add(unit);
    }
    

    public void addCities(City city) {
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
		for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i) == city) {
                this.removeCityByIndex(i);
            	return;
            }
        }
	}
	
	
	
	
	
	
}
