<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="de">
<head>
    <title>Wähle einen Parkplatz aus:</title>
</head>
<body>
<form method="post" action="parkhaus-servlet">
    <input type="hidden" name="action" value="wahlParkplatz">
    Geben Sie ihre EinzelticketID an:<br/>
    <label>
        <input type="text" name="ticketId">
    </label>
    <br/>
    <br/>
    <input type="hidden" name="action" value="wahlParkplatz">
    Geben Sie ihre MonatsticketID an:<br/>
    <label>
        <input type="text" name="mticketId">
    </label>
    <br/>
    <br/>
    Wählen Sie einen Parkplatz aus:
    <br/>
    <label for="p"></label><select name="p" id="p">
    <c:forEach items="${Parkplätze.getParkingLotsByLevel(level)}" var="pl">
        <option name="Etagen">
                ${pl.level}${pl.levelId} ${pl.occupied ? "besetzt" : "frei"} ${pl.ticketId}
        </option>
    </c:forEach>
</select>
    <input type="submit" value ="Parkplatz wählen">
</form>
<br/>
<br/>
<br/>
<br/>
<form action="levelParkingLot.jsp">
    <input type="submit" value="Zurück zur Etagenauswahl" />
</form>
<br/>
<form action="parkhaus.jsp">
    <input type="submit" value="Zurück zur Startseite" />
</form>
</body>
</html>
