<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="x" %>

<%
	Object user = request.getSession().getAttribute("current.user.id");
	
	String message = (String) request.getSession().getAttribute("error");
%>

<html>
	<head>
		<title>Register user</title>
	</head>
	
	<body>
	
		<%if(user != null) {%>
			<p>User already registered!</p>
		<%}else{ %>
			<form action="register" method="post">
				First name: <input type="text" name="firstName" value ="${firstName}" required/><br>
				Last name: <input type="text" name="lastName" value="${lastName}" required/><br>
				Email: <input type="text" name="email" value="${email}" required/><br>
				Nick: <input type="text" name="nick" required/><br>
				Password: <input type="password" name="password" required/><br>
				
				<input type="submit" value="register"/>		
				
				<%if(message != null) {%>
					<p><%=message %></p>
				<%} %>	
			</form>
		<%} %>
	</body>
</html>