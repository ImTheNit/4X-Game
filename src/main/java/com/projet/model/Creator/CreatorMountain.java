package com.projet.model.Creator;

import com.projet.model.Mountain;
import com.projet.model.Tile;

public class CreatorMountain extends CreatorTile {

	@Override
	public Tile create(int x, int y) {
		return new Mountain(x,y);
	}

}
