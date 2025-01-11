package com.projet.main;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.Base64;

import com.projet.controller.LoginController;
import com.projet.model.MapGame;
import com.projet.model.Player;
import com.projet.model.Soldier;
import com.ressources.sql.Password;
import com.ressources.sql.SQL;

public class App {

	public static void main(String[] args) throws SQLException {
		
		//System.out.println(m.toString());
		
		/*Player player = new Player();
		Soldier s = new Soldier(9, 9, 0, player);

		
		
		
		
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
        //sql.createDatabase();
        try {
        	Password mdp = sql.GetHashPassword("password");
			System.out.println(mdp.getPassword());
			String salt = new String(Base64.getEncoder().encodeToString(mdp.getSalt()));
			System.out.println(salt);
			mdp.setSalt(Base64.getDecoder().decode(salt));
			System.out.println(SQL.verifyPassword(mdp, "password"));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException f) {
			// TODO Auto-generated catch block
			f.printStackTrace();
		} catch (Exception g) {
			g.printStackTrace();
		}
        
		
	}
	
}
