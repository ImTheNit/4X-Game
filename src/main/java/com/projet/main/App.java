package com.projet.main;

import com.projet.model.Map;
import com.projet.model.Player;
import com.projet.model.Soldier;

public class App {

	public static void main(String[] args) {
		Map m = new Map();
		System.out.println(m.toString());
		
		Player player = new Player();
		Soldier s = new Soldier(9, 9, 0, player, m);

	}
	
}
