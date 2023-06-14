<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="x" %>

<html>
	<head>
		<title>Success registration</title>
	</head>
	
	<body>
		<p>User <%= (String)request.getSession().getAttribute("current.user.fn") %> <%= (String)request.getSession().getAttribute("current.user.ln") %> is successfully registered.</p>
	</body>
</html>