<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="/voting-app/styles.jsp">
<title>Rezultati glasanja</title>
</head>
<body>
<body>
	<h1 align="center">Rezultati glasanja za pitanje: </h1>
	<h3 align="center">${poll.title}</h3>
	<a href="/voting-app/servleti/index.html">Natrag na izbor anketa</a>
	<p>Ovo su rezultati glasanja</p>
	<table border="1" cellspacing="0" class="rez centered-container">
		<thead>
			<tr>
				<th>Odgovor</th>
				<th>Broj glasova</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="pollOption" items="${pollOptions}">
				<tr>
					<td>${pollOption.optionTitle}</td>
					<td>${pollOption.votesCount}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<h2>Grafički prikaz rezultata</h2>
	<img class="centered-container" alt="Pie-chart" src="/voting-app/servleti/glasanje-grafika?pollID=${poll.id}" width="400" height="400" />
	<h2>Results in XLS format</h2>
	<p>
		Results in XLS format are available <a href="/voting-app/servleti/glasanje-xls?pollID=${poll.id}">here</a>
	</p>
	<h2>Razno</h2>
	<p>Linkovi na reprezentaciju pobjedničkih odgovora:</p>
	<ul>
		<c:forEach var="winner" items="${winners}">
		<li><a href="${winner.optionLink}"
			target="_blank">${winner.optionTitle}</a></li>
		</c:forEach>
	</ul>
</body>

</body>

</html>