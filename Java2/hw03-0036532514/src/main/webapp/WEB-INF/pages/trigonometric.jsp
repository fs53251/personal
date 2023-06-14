<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

	<head>
	    <title>Trigonometric</title>
	    
	    <link rel="stylesheet" type="text/css" href="css/style.jsp">
	</head>
	
	<body>
	    <table>
	    	<thead>
	    		<tr>
	    			<th>x</th>
	    			<th>sin(x)</th>
	    			<th>cos(x)</th>
	    		</tr>
	    	</thead>
	    	<tbody>
	    		<c:forEach var="item" items="${rez}" >
	    			<tr>
		    			<td>${item.getKey()}</td>
		    			<td>${item.getValue().get(0)}</td>
		    			<td>${item.getValue().get(1)}</td>
		    		</tr>
	    		</c:forEach>
	    	</tbody>
	    </table>
	</body>
</html>