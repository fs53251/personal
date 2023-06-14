<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
		<title>Vrijeme</title>
		
		<style>
			body{ background-image: url('${sessionScope.slika}');}
			div {
				background-color: white;
			}
		</style>
	</head>
	
	<body>
		<div>
			<h1>Trenutno vrijeme</h1>
			<p> ${vrijeme}</p>
			<a href="choose">Odaberi pozadinu</a>
		</div>
	</body>
</html>