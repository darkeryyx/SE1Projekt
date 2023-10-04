<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="de">
<head>
    <title> Park Menü</title>
</head>
<body>
<form action="levelParkingLot.jsp">
    <input type="submit" value="Einparken" />
</form>
<br/>
<form action="leaveParkingLot.jsp">
    <input type="submit" value="Ausparken" />
</form>
<br/>
<form method="Get" action="parkhaus-servlet">
    <input type="hidden" value="firstEmptyParkingLot" name="aktion">
    <input type="submit" value="nächster Freier Parkplatz">
</form>
<br/>
<form action="parkhaus.jsp">
    <input type="submit" value="Zurück zur Startseite" />
</form>
</body>
</html>