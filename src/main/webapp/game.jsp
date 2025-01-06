<%@page import="java.util.ArrayList"%>
<%@page import="com.projet.model.Player"%>
<%@page import="com.projet.model.TileType"%>
<%@page import="com.projet.model.Soldier"%>
<%@page import="com.projet.model.Tile"%>
<%@page import="com.projet.model.City"%>
<%@page import="org.hibernate.internal.build.AllowSysOut"%>
<%@page import="com.projet.model.Map"%>
<%@page import="org.apache.tomcat.util.descriptor.web.SessionConfig"%>
<%@page import="jakarta.websocket.Session"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
//Vérification de l'existence de la session
HttpSession activeSession = request.getSession(false);
if (activeSession == null || activeSession.getAttribute("user") == null) {
    // Redirection vers la page de connexion car la session n'existe pas
    response.sendRedirect("login.jsp");
    return;
}
%>
<html>
<head>
<meta charset="UTF-8">
<title>4X-Game</title>
<style>
.image-container {
	    position: relative;
	    width:  100px;
	    height : 100px; 
	}
.image-container img {
	    position: absolute;
	    top : 0;
	    left : 0;

	}

.table {
	background-color: 96937d
	}
	
</style>
</head>
<body>
	<h1>Welcome, <%= session.getAttribute("user") %> to the game</h1>
		<p>The current time is : <%= new java.util.Date() %> </p>
		<h2>carte : </h2>
		<table >
		
		<%
		
		Player player = new Player("Joueur1",0,0,new ArrayList<Soldier>(),new ArrayList<City>());
		Player player2 = new Player("Joueur2",0,0,new ArrayList<Soldier>(),new ArrayList<City>());
		Player player3 = new Player("Joueur3",0,0,new ArrayList<Soldier>(),new ArrayList<City>());
		Player player4 = new Player("Joueur4",0,0,new ArrayList<Soldier>(),new ArrayList<City>());
		Soldier s = new Soldier(9, 9, 0, player, Map.getMap());
		Tile t = Map.getMap().getTile(0, 0);
		t.setUnit(s);
		City c = (City)t;
		City c2 = (City)Map.getMap().getTile(9, 0);
		City c3 = (City)Map.getMap().getTile(9, 9);
		City c4 = (City)Map.getMap().getTile(0, 9);
		
		c.newOwner(player);
		c2.newOwner(player2);
		c3.newOwner(player3);
		c4.newOwner(player4);
		
		String display = Map.getMap().printJSP(player,Map.getMap().getTile(1, 1)); 
		out.println(display);
		%>
		</table>
		
</body>
</html>