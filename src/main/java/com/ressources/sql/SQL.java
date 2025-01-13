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

import com.projet.model.Player;

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
	/*
	 * methods
	 */


	/**
	 * create the database from the script in pathToCreate
	 * @return true when executed
	 * @throws SQLException
	 */
	public boolean createDatabase() throws SQLException {
		executeSelect(getScriptCreate());
		return true;
	}
	/**
	 * Suppress the database with the script in pathToDrop
	 * @return true when executed
	 * @throws SQLException
	 */
	public boolean dropDatabase() throws SQLException {
		executeSelect(getScriptDrop());
		return true;
	}
	/**
	 * execute the SQL query in query
	 * @param query is the SQL code that gets executed in the database, the query as to be a select
	 * @return a resultSet corresponding to the request of query in the database
	 * @throws SQLException
	 */
	public static ResultSet executeSelect(String query) throws SQLException {
		try  {
			Connection connection = SQL.getConnection(getJdbcUrl());
	        Statement statement = connection.createStatement();
	        //content = "DROP DATABASE IF EXISTS 4XGame;";
	        ResultSet result = statement.executeQuery(query);
	        return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
		
        return null;
	}
	
	/**
	 * execute the SQL query in query
	 * @param query is the SQL code that gets executed in the database, the query as to be an insert or an update or a delete
	 * @throws SQLException
	 */
	public static void executeInsert(String query) throws SQLException {
		try (Connection connection = SQL.getConnection(getJdbcUrl())) {
	        Statement statement = connection.createStatement();
	        //content = "DROP DATABASE IF EXISTS 4XGame;";
	        statement.executeUpdate(query);
	        return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ;
	}
	
	/**
	 * 
	 * @param username corresponds to the username you test the existence in the database
	 * @return true if username in the database
	 * @throws SQLException
	 */
	public static boolean PlayerNameTaken(String username) throws SQLException {
		String chaine = "SELECT login"
					+" FROM fourxgame.player"
					+" WHERE LOWER(login) = LOWER('"+username+"')";
		ResultSet test = executeSelect(chaine);
		if(test ==null) {
			return false;
		}
		return test.next();//is false if the cursor is after the last row -> case of no row at all
	}
	
	/**
	 * 
	 * @param username login of the player in the database
	 * @param password contains the salt and the encrypted password in the database
	 * @return true if the password given correspond to the password encrypted in the database
	 * @throws SQLException
	 * @throws Exception
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
	
	/**
	 * 
	 * @param mdp contains a password to verify in the database
	 * @param inputPassword contains the password to compare with
	 * @return true if both passwords are the same
	 * @throws Exception
	 */
	public static boolean verifyPassword(Password mdp,String inputPassword) throws Exception {
        KeySpec spec = new PBEKeySpec(inputPassword.toCharArray(), mdp.getSalt(), iterationCount, keyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] hash = factory.generateSecret(spec).getEncoded();
        String newHash = Base64.getEncoder().encodeToString(hash);

        return newHash.equals(mdp.getPassword());
    }
	
	/**
	 * 
	 * @param password password to encrypt
	 * @return a password corresponding to the encrypted original password with his salt
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
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
	
	/**
	 * 
	 * @param username is the login of the player to create in the database
	 * @param Unencriptedpassword is the password received from the login request
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
	
	/**
	 * add in the database the game and add to the players score the score they got in the game
	 * @throws SQLException
	 */
	public static void SaveGame() throws SQLException {
		String query = new String("INSERT INTO FourXGame.Game (`player1_login`, `player2_login`, `player3_login`, `player4_login`)\nVALUES (");
		String queryUpdatePlayer = new String("");
		for(int i =0; i<Player.getPlayerList().size(); i++) {
			//Mise a jour du score du joueur dans la bdd
			if(Player.getPlayerList(i).getLogin()!="") {
				queryUpdatePlayer = "UPDATE FourXGame.Player\n"+
				"SET Score =";
				ResultSet test = SQL.executeSelect("SELECT Score FROM FourXGame.Player WHERE LOWER(login) = LOWER('"+Player.getPlayerList(i).getLogin()+"');");
				//System.out.println(test.getRow());
				if(test.next()) {
					//System.out.println(test.getInt("Score"));
					queryUpdatePlayer += (test.getInt("Score")+Player.getPlayerList(i).getScore())+"\n"+
					"WHERE Lower(login) = LOWER('"+Player.getPlayerList(i).getLogin()+"');";
					//System.out.println(queryUpdatePlayer);
					SQL.executeInsert(queryUpdatePlayer);
					if(i!=0) {
						query +=",";
					}
					query +="'"+Player.getPlayerList(i).getLogin()+"'";
				}
			}else {
				if(i!=0) {
					query +=",";
				}
				query +="NULL";
			}
			
		}
		query+=");";
		//System.out.println(query);
		SQL.executeInsert(query);
	}
}
