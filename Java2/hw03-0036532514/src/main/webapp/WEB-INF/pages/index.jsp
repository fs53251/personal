<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

	<head>
		<title>Index</title>
		
		<link rel="stylesheet" type="text/css" href="css/style.jsp">
	</head>
	
	<body>
		<p>Task 1:</p><br>
		<a href="colors.jsp">Background color chooser</a><br><br>
		
		<p>Task 2:</p><br>
		<a href="trigonometric?a=0&b=90">Trigonometric table(sin, cos) from 1 to 90</a><br><br>
		<form action="trigonometric" method="GET">
			Početni kut:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
			Završni kut:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
			<input type="submit" value="Tabeliraj"><input type="reset" value="Reset">
		</form><br><br>
		
		<p>Task 3:</p><br>
		<a href="stories/funny.jsp">Funny story</a>
		
		<p>Task 4:</p>
		<a href="reportImage">OS usage report</a>
		
		<p>Task 5:</p>
		<a href="powers?a=1&b=100&n=3">Download powers</a><br><br>
		
		<p>Task 6;</p>
		<a href="appinfo.jsp">Application information</a><br><br>
		
		<p>Task 7;</p>
		<a href="glasanje">Vote for the favourite band</a><br><br>
	</body>
</html>