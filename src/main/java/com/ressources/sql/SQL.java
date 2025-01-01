package com.ressources.sql;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {
	private static final String pathToDrop = "/com/ressources/sql/drop.sql";
	private static final String pathToCreate = "/com/ressources/sql/schema.sql";
	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/";
    private static final String USERNAME = "newuser";
    private static final String PASSWORD = "Mypwd123"; 
	
	
	
	

	
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

	
	
	/*
	 * methods
	 */

	
	private String getScriptFromFile(String PathToFile) {
		String content = "";
		InputStream inputStream = SQL.class.getResourceAsStream(PathToFile);
        if (inputStream == null) {
            System.out.println("Fichier non trouvé !");
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
		executeQuery(getScriptCreate());
		return true;
	}
	public boolean dropDatabase() throws SQLException {
		executeQuery(getScriptDrop());
		return true;
	}
	public boolean executeQuery(String query) throws SQLException {
		try (Connection connection = DriverManager.getConnection(getJdbcUrl(), getUsername(), getPassword())) {
	        System.out.println("Successfully connected to database");
	        Statement statement = connection.createStatement();
	        //content = "DROP DATABASE IF EXISTS 4XGame;";
	        System.out.println(query);
	        statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
		
        return true;
	}
	
}
