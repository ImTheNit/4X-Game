<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Résultats de la Partie</title>
    <style>
        body {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-around;
            align-items: center;
            height: 100vh;
            margin: 0;
            font-family: Arial, sans-serif;
            background-color: #f0f0f0;
        }
        .player {
            background-color: #fff;
            border: 1px solid #ccc;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 45%;
            margin: 10px;
            padding: 20px;
            text-align: center;
        }
        .player h2 {
            margin-top: 0;
        }
        .player p {
            margin: 5px 0;
        }
    </style>
</head>
<body>

	<script>
		const ws = new WebSocket('ws://'+window.location.host+"/4X-Game/MainController");
		ws.onopen = () => {
	        console.log('Connexion WebSocket établie ');
	        ws.send(JSON.stringify({ type :"finalScore",session: "<%=session.getAttribute("user")%>" }));
	    };
	
	    ws.onmessage = (event) => {
	    	console.log('message recu de FrontController' );
	        const message = JSON.parse(event.data);
	        console.log("message : " + message);
	        replaceContent(event.data);
	        //replaceFightContent(event.data);
	        //checkEnd(event.data);
	    };
	
	    ws.onclose = () => {
	        console.log('Connexion WebSocket fermée ');
	    };
	    
	    
	    
	    
	    function replaceContent(newHtml) {
        	const jsonObject = JSON.parse(newHtml);
        	           
            const htmlContent1 = jsonObject.htmlJoueur1;
            const htmlContent2 = jsonObject.htmlJoueur2;
            const htmlContent3 = jsonObject.htmlJoueur3;
            const htmlContent4 = jsonObject.htmlJoueur4;

			replaceContentHtml(htmlContent1,"player1");
			replaceContentHtml(htmlContent2,"player2");
			replaceContentHtml(htmlContent3,"player3");
			replaceContentHtml(htmlContent4,"player4");
        }
	    
	    function replaceContentHtml(NewContent,chaine) {
            const contentDiv = document.getElementById(chaine);
            contentDiv.innerHTML = NewContent;
            
        }
	
	</script>


    <div class="player" id="player1">
        <h2>Joueur 1</h2>
        <p>Nom: Joueur 1</p>
        <p>Nombre: 1</p>
        <p>Score: 1500</p>
        <p>Production de Ressources: 300</p>
        <p>Combats Gagnés: 5</p>
    </div>
    <div class="player" id="player2">
        <h2>Joueur 2</h2>
        <p>Nom: Joueur 2</p>
        <p>Nombre: 2</p>
        <p>Score: 1200</p>
        <p>Production de Ressources: 250</p>
        <p>Combats Gagnés: 3</p>
    </div>
    <div class="player" id="player3">

    </div>
    <div class="player" id="player4">
        
    </div>
</body>
</html>
