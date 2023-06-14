<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
	<head>
		<title>Entry Id</title>
	</head>
	
	<body>
		<p>Blog Entry:</p>
		<p>${blogEntry.title}</p>
		<p>${blogEntry.text}</p>
		
		<a href="/blog/servleti/author/${nick}/edit">Edit blog entry</a>
		
		<p>Comments</p><br>
		<c:forEach var="com" items="${comments}" >
			<p>${com.usersEmail}: ${com.message}; PostedOn: ${com.postedOn}</p><br>
		</c:forEach>
		
		<form action="/blog/servleti/comment/${nick}" method="post">
			Message: <input type="text" name="message"><br>
			<input type="submit" value="AddComment"/>
		</form>
		
	</body>
</html>