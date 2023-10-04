<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="de">
<head>
    <title>Ergebnis</title>
</head>
<body>${message}
<br/>
<jsp:include page="monatsticket.jsp"></jsp:include>
<form action="parkhaus.jsp">
    <input type="hidden" name="ID" value="${ID}">
    <input type="submit" value="Zurück zur Startseite" />
</form>
</body>
</html>