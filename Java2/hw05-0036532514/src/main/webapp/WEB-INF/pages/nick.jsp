<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
	<head>
		<title>All blog entires</title>
	</head>
	
	<body>
		<p>Blog entries:</p>
		<c:forEach var="blogEntrie" items="${entries}">
			<p><a href="/blog/servleti/author/${nick}/${blogEntrie.id}">${blogEntrie.title}</a></p>
		</c:forEach>
		
		<% if(request.getAttribute("addEntry") != null) {%>
			<a href="/blog/servleti/author/${nick}/new">Add entry for user <%= (String)request.getAttribute("addEntry") %></a>
		<%}%>
	</body>
</html>