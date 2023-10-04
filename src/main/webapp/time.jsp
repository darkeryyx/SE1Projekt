<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="de">
<head>
    <h1>Aktuelle Uhrzeit</h1>
</head>
<body>
<br/>
<br/>
${currentTime}
<br/>
<br/>
<form action="parkhaus-servlet" method="post">
    <input type="hidden" name="action" value="reset">
    <input type="submit" value="Reset">
</form>
<br>
<a href="parkhaus.jsp">Zurück zur Startseite</a>
</body>
</html>