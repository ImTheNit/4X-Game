<%@page import="com.projet.controller.ActionsController"%>
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
		
        
        
        function Refresh() {
            const messageText = "refresh";
            if (messageText) {
                const message = {
                    type: 'message',
                    pseudonyme: "pseudo",
                    text: "texte",
                    session: "<%=session.getAttribute("user")%>"
                };
                wsMap.send(JSON.stringify(message));
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

		</table></div>
		
		<div class="box buttons" id="buttons">
		</div>

		<script>
			
			
			function moveNorth(){
				fetch('/4X-Game/ActionServlet', {
	                method: 'POST',
	                headers: {
	                    'Content-Type': 'application/x-www-form-urlencoded'
	                },
	                body: new URLSearchParams({
	                    'Action': 'moveNorth',
	                })
	            });
				Refresh();
			}
			function moveSouth(unit){
				fetch('/4X-Game/ActionServlet', {
	                method: 'POST',
	                headers: {
	                    'Content-Type': 'application/x-www-form-urlencoded'
	                },
	                body: new URLSearchParams({
	                    'Action': 'moveSouth',
	                })
	            });
	            Refresh();
			}

			function moveWest(){
				fetch('/4X-Game/ActionServlet', {
	                method: 'POST',
	                headers: {
	                    'Content-Type': 'application/x-www-form-urlencoded'
	                },
	                body: new URLSearchParams({
	                    'Action': 'moveWest',
	                })
	            });
	            Refresh();
			}
			function moveEast(){
				fetch('/4X-Game/ActionServlet', {
	                method: 'POST',
	                headers: {
	                    'Content-Type': 'application/x-www-form-urlencoded'
	                },
	                body: new URLSearchParams({
	                    'Action': 'moveEast',
	                })
	            });
	            Refresh();
			}
			function collect(){
				fetch('/4X-Game/ActionServlet', {
	                method: 'POST',
	                headers: {
	                    'Content-Type': 'application/x-www-form-urlencoded'
	                },
	                body: new URLSearchParams({
	                    'Action': 'collect',
	                })
	            });
	            Refresh();
			}
			function heal(){
				fetch('/4X-Game/ActionServlet', {
	                method: 'POST',
	                headers: {
	                    'Content-Type': 'application/x-www-form-urlencoded'
	                },
	                body: new URLSearchParams({
	                    'Action': 'heal',
	                })
	            });
	            Refresh();
			}
			function pass(){
				fetch('/4X-Game/ActionServlet', {
	                method: 'POST',
	                headers: {
	                    'Content-Type': 'application/x-www-form-urlencoded'
	                },
	                body: new URLSearchParams({
	                    'Action': 'pass',
	                })
	            });
	            Refresh();
			}
			function recruitSoldier(){
				fetch('/4X-Game/ActionServlet', {
	                method: 'POST',
	                headers: {
	                    'Content-Type': 'application/x-www-form-urlencoded'
	                },
	                body: new URLSearchParams({
	                    'Action': 'recruit',
	                })
	            });
	            Refresh();
			}
		</script>

		</div>

		
		

</body>
</html>