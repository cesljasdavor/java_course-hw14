<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="/voting-app/styles.jsp">
<link rel="icon" type="image/x-icon" href="/voting-app/logo.png">
<link
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
	rel="stylesheet">
<title>Aplikacija za glasanje: Home</title>
</head>
<body>
	<h1 align="center" style="color: navy"><span style="color: black" class="fa fa-rebel" aria-hidden="true"></span>
	<h1 align="center">Izbor anketa</h1>
	<div class="centered-container">
		<p>Molimo Vas da izaberete anketu u kojoj želite sudjelovati</p>
		<p>Ponuđene ankete:</p>
		<ul class="no-decorations">
			<c:forEach var="poll" items="${polls}">
				<li><a href="/voting-app/servleti/glasanje?pollID=${poll.id}">${poll.title}</a></li>			
			</c:forEach>
		</ul>
	</div>
	<hr>
	<h4 align="center">
		Autor ove stranice je: <b style="color: blue">Davor Češljaš</b> <span
			class="fa fa-copyright" aria-hidden="true"></span>
	</h4>
</body>
</html>