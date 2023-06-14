<%@page import="hr.fer.oprpp2.hw04.model.PollOptions"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
		<title>Results:</title>
	</head>
	<body>
		<h1>Rezultati glasanja</h1>
		<p>Ovo su rezultati glasanja.</p>
	<table border="1"  class="rez">
		<thead><tr><th>Bend</th><th>Like</th><th>Dislike</th><th>Razlika</th></tr></thead>
		<tbody>
			<c:forEach var="pollOption" items="${pollOptions}">
				<tr><td>${pollOption.optionTitle}</td><td>${pollOption.votesCount}</td><td>${pollOption.dislikeCount}</td><td>${pollOption.getRazlika()}</td></tr>
			</c:forEach>
		</tbody>
	</table>
	
	<h2>Grafički prikaz rezultata</h2>
	<img src="${pageContext.request.contextPath}/servleti/glasanje-grafika?pollID=${pollOptions.get(0).pollID}" class="transparent shrinkToFit" width="400" height="400">
	
	<h2>Rezultati u XLS formatu</h2>
	<p>Rezultati u XLS formatu dostupni su <a href="/voting-app/servleti/glasanje-xls?pollID=${pollOptions.get(0).pollID}">ovdje</a></p>

	<h2>Razno</h2>
	<p>Primjeri pjesama pobjedničkih bendova:</p>
		<ul>
		
		<c:forEach var="obj" items="${winners}">
			<li><a href="${obj.optionLink}">${obj.optionTitle}</a></li>
		</c:forEach>
		</ul>
</body>
</html>