<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
	<head>
		<title>Edit</title>
	</head>

	<body>
		<%if(request.getSession().getAttribute("current.user.id") != null) {%>
			<p>Edit Blog entry:</p>
			<form action="/blog/servleti/edit?id=<%= request.getSession().getAttribute("current.user.id") %>" method="post">
				Title: <input type="text" name="title" value="<%=(String)request.getSession().getAttribute("current.entry.title") %>"/><br>
				Text: <input type="text" name="text" value="<%= (String)request.getSession().getAttribute("current.entry.text")%>"/><br>
				<input type="submit" value="Enter" />
			</form>
		<%} %>
	</body>
</html>