<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
	<head>
		<title>New</title>
	</head>
	
	<body>
		<%if(request.getSession().getAttribute("current.user.id") != null) {%>
			<p>Enter new Blog entry:</p>
			<form action="/blog/servleti/new" method="post">
				Title: <input type="text" name="title" /><br>
				Text: <input type="text" name="text" /><br>
				<input type="submit" value="Enter" />
			</form>
		<%} %>
	</body>
</html>