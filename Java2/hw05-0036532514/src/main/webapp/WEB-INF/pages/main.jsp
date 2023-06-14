<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
	<head>
		<title>Main page</title>
	</head>
	
	<body>
	
		<%if(request.getSession().getAttribute("current.user.id") == null) {%>
			<a href="/blog/servleti/register">Registriraj se</a>
			
			<form action="login" method="post">
				Nick:<input type="text" name="nick" /><br />
				Password:<input type="password" name="userPass" /><br /> 
				<input type="submit" value="login"/>
			</form>
		<%} else{ %>
			<p>Current user logged in: <%=(String)request.getSession().getAttribute("current.user.fn") %> <%=(String)request.getSession().getAttribute("current.user.ln") %></p>
			<a href="/blog/servleti/logout">Logout</a>
		<%} %>
		
		<% if(request.getSession().getAttribute("error") != null){ %>
					<p><%= (String)request.getSession().getAttribute("error") %></p>
					<% request.getSession().setAttribute("error", null); %>
		<%} %>
		
		<p>Popis svih autora - Kliknite na link da dođete do njihovih djela.</p>
		<c:forEach var="user" items="${users}">
			<a href="/blog/servleti/author/${user.nick}">Autor: ${user.nick}</a><br>
		</c:forEach>
	</body>
</html>