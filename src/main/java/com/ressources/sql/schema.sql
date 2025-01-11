-- Create the database with IF NOT EXISTS condition
CREATE DATABASE IF NOT EXISTS FourXGame;

-- Use the database
USE FourXGame;
-- Create the Player table with IF NOT EXISTS condition
CREATE TABLE IF NOT EXISTS Player (
    login VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    salt VARCHAR(30) not NULL,
    score INT DEFAULT 0,
    ressources INT DEFAULT 0
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
    player1_login VARCHAR(50),
    player2_login VARCHAR(50),
    player3_login VARCHAR(50),
    player4_login VARCHAR(50),
    turn INT DEFAULT 0,
    FOREIGN KEY (map_id) REFERENCES Map(id),
    FOREIGN KEY (player1_login) REFERENCES Player(login),
    FOREIGN KEY (player2_login) REFERENCES Player(login),
    FOREIGN KEY (player3_login) REFERENCES Player(login),
    FOREIGN KEY (player4_login) REFERENCES Player(login)
);

INSERT INTO Player (login, password, salt)
VALUES 
('Admin', 'sDNihjPVksXxBIeLMFojGg==', 'dsiBmu5zIwCnPAiioheL6g=='),
('Admin2','A6NJfFbjOYezqnJHfvmQwQ==', 'NlDyd1RP3upT3lptI7vJYA=='),
('Admin3','/VZ+2UhyYYx3kcAXO5mncA==', 'xPrKpmQgDajcg+AxWMwX6A=='),
('Admin4','Lrm6HraKtVRsVWS6EtEeMg==', '61nOwX4Gea/XqFtsB4xYsA==');
