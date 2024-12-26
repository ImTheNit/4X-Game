package com.projet.model.Creator;

import com.projet.model.Plain;
import com.projet.model.Tile;

public class CreatorPlain extends CreatorTile{

	@Override
	public Tile create(int x, int y) {
		return new Plain(x,y);
	}

}
