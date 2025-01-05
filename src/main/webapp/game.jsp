<%@page import="com.projet.model.TileType"%>
<%@page import="org.hibernate.internal.build.AllowSysOut"%>
<%@page import="com.projet.model.Map"%>
<%@page import="org.apache.tomcat.util.descriptor.web.SessionConfig"%>
<%@page import="jakarta.websocket.Session"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
//Vérification de l'existence de la session
HttpSession activeSession = request.getSession(false);
if (activeSession == null || activeSession.getAttribute("userID") == null) {
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
.container {
	    position: relative;
	}
	.img1, .img2, .img3 {
	    position: absolute;
	}</style>
</head>
<body>
	<h1>Welcome, <%= session.getAttribute("user") %> to the game</h1>
		<p>The current time is : <%= new java.util.Date() %> </p>
		<h2>carte : </h2>
		<table>
		<% 
		for(int i=0; i<Map.getMap().XSize(); i++){
			%><tr>
			<% for(int j=0; j<Map.getMap().YSize(); j++){
				%><td>
				<img alt="<%= Map.getMap().getTile(i, j).toString() %>" src="<%= Map.getMap().getTile(i, j).getImage() %>" class=img1>
				<%
				if (Map.getMap().getTile(i, j).getUnit()!=null){
					%><img alt="<%= Map.getMap().getTile(i, j).getUnit().toString() %>" src="<%= Map.getMap().getTile(i, j).getUnit().getImage() %>" class=img2>
					<%
				}else{
					if(Map.getMap().getTile(i, j).getType()== TileType.CITY){
						%><img alt="<%= Map.getMap().getTile(i, j).toString() %>" src="<%= Map.getMap().getTile(i, j).getImage() %>" class=img1>
						<%
					}
				}
				//TODO ajouter si il y a un joueur et l'image si l'un des éléments de la case appartient à un joueur (ville ou soldat) %>
				</td><%
			}
			%></tr><%
		}
		%>
		</table>
</body>
</html>