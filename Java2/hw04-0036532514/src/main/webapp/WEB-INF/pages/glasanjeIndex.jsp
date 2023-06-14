<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
	<head>
		<title>Voting</title>
	</head>
	
	<body>
		<h1>Vote for your favourite:</h1>
		
		<ol>
			<c:forEach var="object" items="${objects}">
			<li>${object.optionTitle} ->
			<a href='/voting-app/servleti/glas?pollID=${object.pollID}&id=${object.id}'>LIKE</a>
			<a href='/voting-app/servleti/glas/dislike?pollID=${object.pollID}&id=${object.id}'>DISLIKE</a>
			</li>
			</c:forEach>
		</ol>
		
	</body>
</html>