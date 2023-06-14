<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
		<title>Voting</title>
	</head>
   
	<body>
	   	<h1>Click to choose poll:</h1>
		
		<ul>
			<c:forEach var="poll" items="${polls}">
			<li>${poll.message}, link: <a href="/voting-app/servleti/glasanje?pollID=${poll.id}">${poll.title}</a></li>
			</c:forEach>
		</ul>
	</body>
</html>