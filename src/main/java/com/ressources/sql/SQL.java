package com.ressources.sql;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.tomcat.dbcp.dbcp2.DriverManagerConnectionFactory;

public class SQL {
	private static final String pathToDrop = "/com/ressources/sql/drop.sql";
	private static final String pathToCreate = "/com/ressources/sql/schema.sql";
	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/FourXGame";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "cytech0001"; 
    private static final int iterationCount=65536; 
    private static final int keyLength = 128;
	
	
	
	

	
	/*
	 * getters
	 */
	public static String getPathToDrop() {
		return pathToDrop;
	}
	public static String getPathToCreate() {
		return pathToCreate;
	}
	public static String getJdbcUrl() {
        return JDBC_URL;
    }

    public static String getUsername() {
        return USERNAME;
    }

    public static String getPassword() {
        return PASSWORD;
    }

    public static Connection getConnection(String url)throws SQLException, ClassNotFoundException {
    	Class.forName("com.mysql.cj.jdbc.Driver");
    	return DriverManager.getConnection(getJdbcUrl(), getUsername(), getPassword());
    }
	
	/*
	 * methods
	 */

	
	private String getScriptFromFile(String PathToFile) {
		String content = "";
		InputStream inputStream = SQL.class.getResourceAsStream(PathToFile);
        if (inputStream == null) {
            System.out.println("Fichier non trouv� !");
            return "";
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
        	
            String line;
            while ((line = reader.readLine()) != null ) {
            	
            	if (!line.startsWith("-- ")) {
            		int commentIndex = line.indexOf(" -- ");
                    if (commentIndex != -1) {
                        line = line.substring(0, commentIndex);
                    }
            		System.out.println(line);
            		content += "\n"+line;
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
	}
	
	private String getScriptCreate() {
		return getScriptFromFile(pathToCreate);
	}
	private String getScriptDrop() {
		return getScriptFromFile(pathToDrop);
	}
	
	public boolean createDatabase() throws SQLException {
		executeSelect(getScriptCreate());
		return true;
	}
	public boolean dropDatabase() throws SQLException {
		executeSelect(getScriptDrop());
		return true;
	}
	public static ResultSet executeSelect(String query) throws SQLException {
		try  {
			Connection connection = SQL.getConnection(getJdbcUrl());
	        System.out.println("Successfully connected to database");
	        Statement statement = connection.createStatement();
	        //content = "DROP DATABASE IF EXISTS 4XGame;";
	        System.out.println(query);
	        ResultSet result = statement.executeQuery(query);
	        return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
		
        return null;
	}
	
	public static void executeInsert(String query) throws SQLException {
		try (Connection connection = SQL.getConnection(getJdbcUrl())) {
	        System.out.println("Successfully connected to database");
	        Statement statement = connection.createStatement();
	        //content = "DROP DATABASE IF EXISTS 4XGame;";
	        System.out.println(query);
	        statement.executeUpdate(query);
	        return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ;
	}
	
	public static boolean PlayerNameTaken(String username) throws SQLException {
		String chaine = "SELECT login"
					+" FROM fourxgame.player"
					+" WHERE LOWER(login) = LOWER('"+username+"')";
		ResultSet test = executeSelect(chaine);
		if(test ==null) {
			System.out.println("test null");
			return false;
		}
		System.out.println(test.next());
		while(test.next()) {
			System.out.println(test.toString());
			
		}
		
		return test.next()?false:true;//is false if the cursor is after the last row -> case of no row at all
	}
	
	/*
	 * @return true if the player with the username has the same password
	 */
	public static boolean PlayerLoginVerification(String username, String password) throws SQLException, Exception {
		String chaine = "SELECT salt,password \n"
					+" FROM FourXGame.player \n"
					+" WHERE LOWER(login) = LOWER('"+username+"');";
		ResultSet test = executeSelect(chaine);
		if( test!=null && test.next()){//is false if the cursor is after the last row -> case of no row at all
			Password mdp = new Password(test.getNString("password"),Base64.getDecoder().decode(test.getNString("salt")));
			return SQL.verifyPassword(mdp, password);
		}
		return false;
	}
	
	
	public static boolean verifyPassword(Password mdp,String inputPassword) throws Exception {
        KeySpec spec = new PBEKeySpec(inputPassword.toCharArray(), mdp.getSalt(), iterationCount, keyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] hash = factory.generateSecret(spec).getEncoded();
        String newHash = Base64.getEncoder().encodeToString(hash);

        return newHash.equals(mdp.getPassword());
    }
	
	public static Password GetHashPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException
    {
        SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);

		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] hash = factory.generateSecret(spec).getEncoded();
        String hashedPass = Base64.getEncoder().encodeToString(hash);
        return new Password(hashedPass,salt);
    }
	
	/*
	 * %comment% Create a player to be stocked in the database
	 */
	public static void CreatePlayer(String username, String Unencriptedpassword) {
		String query = new String("");
		Password mdp = new Password("", null);
		try {
			mdp = SQL.GetHashPassword(Unencriptedpassword);
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String salt = Base64.getEncoder().encodeToString(mdp.getSalt());
		query = "INSERT INTO FourXGame.Player (`login`, `password`, `salt`)\n"
				+ "VALUES ('"+username+"', '"+mdp.getPassword()+"', '"+salt+"');";
		try {
			SQL.executeInsert(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
