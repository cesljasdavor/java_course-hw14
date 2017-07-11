<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="/voting-app/styles.jsp">
<title>Pogreška prilikom glasanja</title>
</head>
<body>
	<div class="error centered-container">
		<h1 align="center">Pogreška prilikom glasanja</h1>
		<p>${message}</p>
		<p>Da biste se vratili na početnu stranicu kliknite poveznicu ispod</p>
		<a href="/voting-app/servleti/index.html">Natrag na listu anketa</a>
	</div>
</body>
</html>