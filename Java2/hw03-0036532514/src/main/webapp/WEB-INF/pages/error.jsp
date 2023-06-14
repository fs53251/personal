<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

	<head>
	    <title>Error</title>
	    
	    <link rel="stylesheet" type="text/css" href="css/style.jsp">
	</head>
	
	<body>
	    <h1><% out.println((String)request.getParameter("error")); %></h1>
	</body>
</html>