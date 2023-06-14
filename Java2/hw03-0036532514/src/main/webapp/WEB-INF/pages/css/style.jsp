<%@ page contentType="text/css; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

body{
	background-color: <%= session.getAttribute("pickedBgCol") != null ? session.getAttribute("pickedBgCol") : "white" %>;
}

table {
  border: 1px solid black; /* add a border around the table */
  border-collapse: collapse; /* merge the borders between the cells */
}

td, th {
  border: 1px solid black; /* add a border around each cell */
}