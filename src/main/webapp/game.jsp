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
	    background-color: yellow;
	}
.image-container img {
	    position: absolute;
	    top : 0;
	    left : 0;
	    width: 100%;
	    height: 100%;

	}
.container {
	display: flex;
}        

.map {
	/*flex :1;*/
	width: 1100px;
    background-color: lightblue;
}
.buttons {
	width: 100px;
    background-color: lightgreen;
}
.button {
    background-color: #4CAF50; /* Vert */
    border: none;
    color: white;
    padding: 15px 32px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    margin: 4px 2px;
    cursor: pointer;
    border-radius: 12px; /* Coins arrondis */
    transition: background-color 0.3s ease; /* Transition douce */
}

.button:hover {
    background-color: #45a049; /* Couleur au survol */
}

	
</style>
</head>
<body>
	<h1>Welcome, <%= session.getAttribute("user") %> to the game</h1>
		<p>The current time is : <%= new java.util.Date() %> </p>
		<h2>carte : </h2>
		<div class="Container">
		<div class="box map">
		
		<script>
		const wsMap = new WebSocket('ws://'+window.location.host+"/4X-Game/map");
		wsMap.onopen = () => {
            console.log('Connexion WebSocket établie');
            wsMap.send(JSON.stringify({ type: 'join', pseudonyme: "pseudo" }));// remplacer par login
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
                    text: "texte"
                };
                wsMap.send(JSON.stringify(message));
                //messageInput.value = '';
            }
        }
        
        
        
        function replaceContent(newHtml) {
        	console.log(newHtml);
        	console.log(typeof newHtml);
        	const jsonObject = JSON.parse(newHtml);
        	console.log(typeof jsonObject);
        	console.log("type :"+newHtml.type);
        	//const jsonObject = JSON.parse(newHtml);
            const contentDiv = document.getElementById('content');
            if (jsonObject.html){
            	console.log("True" + jsonObject.html);
            }else{
            	console.log("False");
            }
            const htmlContent = jsonObject.html;
            console.log(htmlContent);
            console.log(newHtml.html);
            contentDiv.innerHTML = htmlContent;
        }
		</script>
		
		<table id='content'>
		
		
		
		
		<%
		
		Player player = new Player("Joueur1",0,0,new ArrayList<Soldier>(),new ArrayList<City>());
		Player player2 = new Player("Joueur2",0,0,new ArrayList<Soldier>(),new ArrayList<City>());
		Player player3 = new Player("Joueur3",0,0,new ArrayList<Soldier>(),new ArrayList<City>());
		
		Soldier s = new Soldier(9, 9, 0, player, Map.getMap());
		Tile t = Map.getMap().getTile(0, 0);
		t.setUnit(s);
		City c = (City)t;
		City c2 = (City)Map.getMap().getTile(9, 0);
		City c3 = (City)Map.getMap().getTile(9, 9);
		
		
		c.newOwner(player);
		c2.newOwner(player2);
		c3.newOwner(player3);
		
		
		String display = Map.getMap().printJSP(player,Map.getMap().getTile(1, 1)); 
		//out.println(display);
		%>

		</table></div>
		
		<div class="box buttons">
		<br>Test
		</div>
		<%
		String test = "test1";
		%>
		<script>
		function afficherMessage() {
            alert("<%= test %>");
        }
		
		function moveNorth(){
			<% Player.getPlayerByLogin(((String)activeSession.getAttribute("user"))).getUnits().get(0).moveNorth();%>
			alert("deplacement");
		}
		
		</script>
		<button class="button" onclick="moveNorth()">Move To North</button>
		<button class="button" onclick="sendMessage()">Move To West</button>
		</div>

		
		

</body>
</html>