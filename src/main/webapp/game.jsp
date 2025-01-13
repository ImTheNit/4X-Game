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
<link rel="icon" href="ressources/images/soldier.png" type="image/x-icon">

</head>
<body>
	<div class="welcome-container">
        <h1>Welcome, <%=session.getAttribute("user")%> to the game</h1>
    </div>
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
		
		<div class="container">
		<div class="box map">
		
		<script>
	
		
		
		const ws = new WebSocket('ws://'+window.location.host+"/4X-Game/MainController");
		ws.onopen = () => {
            console.log('Connexion WebSocket établie ');
            ws.send(JSON.stringify({ type : "join",session: "<%=session.getAttribute("user")%>" }));
        };

        ws.onmessage = (event) => {
        	console.log('message recu de FrontController' );
            const message = JSON.parse(event.data);
            console.log("message : " + message);
            replaceContent(event.data);
            replaceFightContent(event.data);
            checkEnd(event.data);
        };

        ws.onclose = () => {
            console.log('Connexion WebSocket fermée ');
        };
		
        
        
        function Refresh() {
            const messageText = "refresh";
            if (messageText) {
                const message = {
                    type: 'refresh',
                    session: "<%=session.getAttribute("user")%>"
                };
                ws.send(JSON.stringify(message));
            }
        }
        
        function replaceContent(newHtml) {
        	const jsonObject = JSON.parse(newHtml);
        	           
            const htmlContent = jsonObject.html;
			const battlesWonContent = jsonObject.battlesWon;
			const soldiersContent = jsonObject.soldiers;
			const citiesContent = jsonObject.cities;
			const scoreContent = jsonObject.score;
			const ressourcesContent = jsonObject.ressources;
			const buttonContent = jsonObject.button;
			
			
			replaceContentHtml(htmlContent,"map");
			replaceContentHtml(battlesWonContent,"battles-won");
			replaceContentHtml(soldiersContent,"soldiers");
			replaceContentHtml(citiesContent,"cities");
			replaceContentHtml(scoreContent,"score");
			replaceContentHtml(ressourcesContent,"ressources");
			replaceContentHtml(buttonContent,"buttons");
        }
        
        function checkEnd(newHtml){
        	const jsonObject = JSON.parse(newHtml);
        	const redirection = jsonObject.redirection;
        	if (redirection === 0){
        		console.log("Fin de partie");
        		window.location.replace('/4X-Game/score.jsp');

        	}
        	
        	
        }

        
        function replaceContentHtml(NewContent,chaine) {
            const contentDiv = document.getElementById(chaine);
            contentDiv.innerHTML = NewContent;
            
        }
        function action(actionName){
			fetch('/4X-Game/ActionServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({
                    'Action': actionName,
                })
            });
			Refresh();
		}
        
        function reloadStylesheets() {
            var links = document.getElementsByTagName("link");
            for (var i = 0; i < links.length; i++) {
                var link = links[i];
                if (link.rel === "stylesheet") {
                    link.href += "?ver=" + new Date().getTime();
                }
            }
        }
        window.onload = reloadStylesheets;
        
        
		</script>
		
		<table id='map'>

		</table></div>
		
		<div class="box buttons" id="buttons">
		</div>
		
		<jsp:include page="combat.jsp" />
		<div class="box combat" id="fight">
		</div>
		</div>

		
		

</body>
</html>