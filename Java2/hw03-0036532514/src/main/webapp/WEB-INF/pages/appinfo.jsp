<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="javax.naming.Context"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

	<head>
	    <title>App Informations</title>
	    
	    <link rel="stylesheet" type="text/css" href="css/style.jsp">
	</head>
	
	<body>
		<h1>Current time is <% long time = (long)application.getAttribute("timeStarted");
							   long interval = System.currentTimeMillis() - time;
					
							   long years = interval / (1000L * 60 * 60 * 24 * 365);
							   interval -= years * (1000L * 60 * 60 * 24 * 365);
							   long months = interval / (1000L * 60 * 60 * 24 * 30);
							   interval -= months * (1000L * 60 * 60 * 24 * 30);
							   long days = interval / (1000L * 60 * 60 * 24);
							   interval -= days * (1000L * 60 * 60 * 24);
							   long hours = interval / (1000L * 60 * 60);
							   interval -= hours * (1000L * 60 * 60);
							   long minutes = interval / (1000L * 60);
							   interval -= minutes * (1000L * 60);
							   long seconds = interval / 1000;
							   
							   out.println(String.format("Application is working for: %d years %d months %d days %02d:%02d:%02d", years, months, days, hours, minutes, seconds));
							   
							   
		%></h1>
	</body>
</html>