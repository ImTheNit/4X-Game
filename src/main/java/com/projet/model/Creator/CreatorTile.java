package com.projet.model.Creator;

import com.projet.model.Tile;

public abstract class CreatorTile {

	/**
	 * @name Create a Tile
	 * 
	 * @param x : position x of the Tile
	 * @param y : position y of the Tile
	 * @return	null
	 */
	public abstract Tile create(int x, int y);
}
