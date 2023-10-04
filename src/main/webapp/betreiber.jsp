<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="de">
<head>
    <title>Administration</title>
</head>
<body>
<form action="parkhaus-servlet" method="post">
    Neuer Stundenpreis: <input type="text" name="newstd">
    <input type="submit" value="Neuen Preis Bestätigen" formaction="${pageContext.request.contextPath}/parkhaus-servlet?action=newstd">
</form>
<br>
<form action="parkhaus-servlet" method="post">
    Neuer Monatsticketpreis: <input type="text" name="newmt">
    <input type="submit" value="Neuen Preis Bestätigen" formaction="${pageContext.request.contextPath}/parkhaus-servlet?action=newmt">
</form>
<br>
<form action="parkhaus-servlet" method="get">
    <input type="hidden" name="aktion" value="income">
    <input type="submit" value="Einnahmen einsehen">
</form>
<br/>
<a href="parkhaus.jsp">Zurück zur Startseite</a>
</body>
</html>