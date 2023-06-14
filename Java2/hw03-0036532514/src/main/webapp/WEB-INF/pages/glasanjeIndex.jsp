<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

	<head>
	    <title>Vote for the band</title>
	    
	    <link rel="stylesheet" type="text/css" href="css/style.jsp">
	</head>
	
	<body>
	    <h1>Glasanje za omiljeni bend:</h1><br>
	    <p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!</p><br>
	    <ol>
	    	<c:forEach var="band" items="${bendovi}">
	    		<li><a href="glasanje-glasaj?id=${band.id }">${band.name }</a></li>
	    	</c:forEach>
	    </ol>
	</body>
</html>