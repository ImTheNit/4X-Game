package com.projet.main;

import java.sql.SQLException;

import com.projet.model.Map;
import com.projet.model.Player;
import com.projet.model.Soldier;
import com.ressources.sql.SQL;

public class App {

	public static void main(String[] args) throws SQLException {
		Map m = new Map();
		System.out.println(m.toString());
		
		Player player = new Player();
		Soldier s = new Soldier(9, 9, 0, player, m);

		
		
		
		
		/*String jdbcURL = "jdbc:mysql://localhost:3306/";
        String username = "newuser";
        String password = "Mypwd123";
        String fileName = "/com/ressources/sql/drop.sql";
        
        
        System.out.println("Connection connection = DriverManager.getConnection(jdbcURL, username, password)");
        System.out.println("jdbcURL = "+jdbcURL);
        System.out.println("username = "+username);
        System.out.println("password = "+password);*/
        SQL sql = new SQL();
        //sql.dropDatabase();
        sql.createDatabase();
	}
	
}
