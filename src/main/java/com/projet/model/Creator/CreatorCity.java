package com.projet.model.Creator;

import com.projet.model.City;
import com.projet.model.Tile;

public class CreatorCity extends CreatorTile{
	
	
	
	@Override
	public Tile create(int x, int y) {
		return new City(x,y);
	}

}
