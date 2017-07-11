<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="/voting-app/styles.jsp">
<title>${poll.title}</title>
</head>
<body>
	<h1 align="center">${poll.title}</h1>
	<p>${poll.message}</p>
	<ol>
		<c:forEach var="pollOption" items="${pollOptions}">
			<li><a href="/voting-app/servleti/glasanje-glasaj?pollID=${poll.id}&id=${pollOption.id}">${pollOption.optionTitle}</a></li>
		</c:forEach>
	</ol>
</body>
</html>