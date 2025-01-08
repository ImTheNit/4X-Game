<%@page import="java.util.ArrayList"%>
<%@page import="com.projet.model.Player"%>
<%@page import="com.projet.model.TileType"%>
<%@page import="com.projet.model.Soldier"%>
<%@page import="com.projet.model.Tile"%>
<%@page import="com.projet.model.City"%>
<%@page import="org.hibernate.internal.build.AllowSysOut"%>
<%@page import="com.projet.model.MapGame"%>
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
<link rel="stylesheet" href="StyleGame.css">

</head>
<body>
	<h1>Welcome, <%=session.getAttribute("user")%> to the game</h1>
		<p>The current time is  <%=new java.util.Date()%> </p>
		<h2>carte : </h2>
		<div class="stats-container">
		<div class="stat">
            <h2>Combats Gagnés</h2>
            <p id="battles-won"></p>
        </div>
        <div class="stat">
            <h2>Nombre de Villes</h2>
            <p id="cities"></p>
        </div>
        <div class="stat">
            <h2>Nombre de Soldats</h2>
            <p id="soldiers"></p>
        </div>
        <div class="stat">
            <h2>Points de ressources</h2>
            <p id="ressources"></p>
        </div>
        <div class="stat">
            <h2>Score</h2>
            <p id="score"></p>
        </div>
        
    </div>
		
		<div class="Container">
		<div class="box map">
		
		<script>
		const wsMap = new WebSocket('ws://'+window.location.host+"/4X-Game/map");
		wsMap.onopen = () => {
            console.log('Connexion WebSocket établie');
            wsMap.send(JSON.stringify({ type: 'join', pseudonyme: "pseudo",session: "<%=session.getAttribute("user")%>" }));// remplacer par login
        };

        wsMap.onmessage = (event) => {
        	console.log('message recu ' );
            const message = JSON.parse(event.data);
            console.log("message : " + message);
            replaceContent(event.data)
        };

        wsMap.onclose = () => {
            console.log('Connexion WebSocket fermée');
        };
		
        
        
        function sendMessage() {
            const messageText = "refresh";
            if (messageText) {
                const message = {
                    type: 'message',
                    pseudonyme: "pseudo",
                    text: "texte",
                    session: "<%=session.getAttribute("user")%>"
                };
                wsMap.send(JSON.stringify(message));
                //messageInput.value = '';
            }
        }
        
        function replaceContent(newHtml) {
        	const jsonObject = JSON.parse(newHtml);
            const htmlContent = jsonObject.html;
			const battlesWonContent = jsonObject.battlesWon
			const soldiersContent = jsonObject.soldiers
			const citiesContent = jsonObject.cities
			const scoreContent = jsonObject.score
			const ressourcesContent = jsonObject.ressources
			const buttonContent = jsonObject.button
			
			
        	replaceContentMap(htmlContent);
			replaceContentBattleWon(battlesWonContent);
        	replaceContentSoldiers(soldiersContent);
        	replaceContentCities(citiesContent);
        	replaceContentScore(scoreContent);
        	replaceContentRessources(ressourcesContent);
        	replaceContentButton(buttonContent);
        }
        
        function replaceContentMap(NewContent) {
            const contentDiv = document.getElementById('map');
            contentDiv.innerHTML = NewContent;
            
        }
        function replaceContentBattleWon(battlesWon) {
            const contentDiv = document.getElementById('battles-won');
            contentDiv.innerHTML = battlesWon;
        }
        function replaceContentSoldiers(Soldiers) {
            const contentDiv = document.getElementById('soldiers');
            contentDiv.innerHTML = Soldiers;
        }
        function replaceContentCities(cities) {
            const contentDiv = document.getElementById('cities');
            contentDiv.innerHTML = cities;
        }
        function replaceContentScore(score) {
            const contentDiv = document.getElementById('score');
            contentDiv.innerHTML = score;
        }
        function replaceContentRessources(ressources) {
            const contentDiv = document.getElementById('ressources');
            contentDiv.innerHTML = ressources;
        }
        function replaceContentButton(button) {
            const contentDiv = document.getElementById('buttons');
            contentDiv.innerHTML = button;
        }
        
		</script>
		
		<table id='map'>
		
		
		
		
		<% /*
										Player player = new Player("Joueur1",0,0,new ArrayList<Soldier>(),new ArrayList<City>());
												Player player2 = new Player("Joueur2",0,0,new ArrayList<Soldier>(),new ArrayList<City>());
												Player player3 = new Player("Joueur3",0,0,new ArrayList<Soldier>(),new ArrayList<City>());
												
												Soldier s = new Soldier(9, 9, 0, player, MapGame.getMap());
												Tile t = MapGame.getMap().getTile(0, 0);
												t.setUnit(s);
												City c = (City)t;
												City c2 = (City)MapGame.getMap().getTile(9, 0);
												City c3 = (City)MapGame.getMap().getTile(9, 9);
												
												
												c.newOwner(player);
												c2.newOwner(player2);
												c3.newOwner(player3);
												
												
												String display = MapGame.getMap().printJSP(player,MapGame.getMap().getTile(1, 1)); 
												//out.println(display);*/
										%>

		</table></div>
		
		<div class="box buttons" id="buttons">
		<br>Test
		</div>

		<script>
			
			
			function moveNorth(){
				<% //Player.getPlayerByLogin(((String)activeSession.getAttribute("user"))).getUnits().get(0).moveNorth(); %>
				alert("deplacement");
				alert("<% //Player.getPlayerByLogin(((String)activeSession.getAttribute("user"))).getUnits().get(0) %>");
				sendMessage();
			}
			function moveSouth(unit){
				
				alert("test");
				<%
				System.out.println("Taille de la liste des unités : " + Player.getPlayerByLogin(((String)activeSession.getAttribute("user"))).getUnits().size());
				if (Player.getPlayerByLogin(((String)activeSession.getAttribute("user"))).getUnits().size()>0){
					Player.getPlayerByLogin(((String)activeSession.getAttribute("user"))).getUnits().get(0).moveSouth();
				}
				//Soldier s = new Soldier(4, 4, 7, Player.getPlayerByLogin(((String)activeSession.getAttribute("user"))), MapGame.getMap());
				//Soldier s1 = new Soldier(5, 5, 7, Player.getPlayerByLogin(((String)activeSession.getAttribute("user"))), MapGame.getMap());
				%>
				alert("deplacement au sud");
				alert("<%=Player.getPlayerByLogin(((String)activeSession.getAttribute("user"))).getUnits().size()   %>");
				sendMessage();
				alert("Fin de fonction<%=((String)activeSession.getAttribute("user"))%>");
			}
		</script>
		<!-- button class="button" onclick="moveNorth()">Move To North</button>
		<button class="button" onclick="moveWest()">Move To West</button-->
		<button class="button" onclick="moveSouth(<%=Player.getPlayerByLogin(((String)activeSession.getAttribute("user"))).getUnits()%>)">Move To South FromGame</button>
		<!--button class="button" onclick="sendMessage()">Move To East</button> -->
		</div>

		
		

</body>
</html>