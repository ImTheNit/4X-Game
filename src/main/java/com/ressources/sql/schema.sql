-- Create the database with IF NOT EXISTS condition
CREATE DATABASE IF NOT EXISTS FourXGame;

-- Use the database
USE FourXGame;

-- Create the Player table with IF NOT EXISTS condition
CREATE TABLE IF NOT EXISTS Player (
    id INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(50) NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    score INT DEFAULT 0
);

-- Create the Map table with IF NOT EXISTS condition and add the turn attribute
CREATE TABLE IF NOT EXISTS Map (
    id INT AUTO_INCREMENT PRIMARY KEY,
    map_object BLOB NOT NULL, -- Use BLOB to store the Java object of type Map
    turn INT DEFAULT 0
);

-- Create the Game table with IF NOT EXISTS condition and add the turn attribute
CREATE TABLE IF NOT EXISTS Game (
    id INT AUTO_INCREMENT PRIMARY KEY,
    map_id INT,
    player1_id INT,
    player2_id INT,
    player3_id INT,
    player4_id INT,
    turn INT DEFAULT 0,
    FOREIGN KEY (map_id) REFERENCES Map(id),
    FOREIGN KEY (player1_id) REFERENCES Player(id),
    FOREIGN KEY (player2_id) REFERENCES Player(id),
    FOREIGN KEY (player3_id) REFERENCES Player(id),
    FOREIGN KEY (player4_id) REFERENCES Player(id)
);
