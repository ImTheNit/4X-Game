package com.projet.model.Creator;

import com.projet.model.Forest;
import com.projet.model.Tile;

public class CreatorForest extends CreatorTile{

	@Override
	public Tile create(int x, int y) {
		return new Forest(x,y);
	}

}
