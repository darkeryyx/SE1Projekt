<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="de">
<head>
    <title>Parkplatzübersicht:</title>
</head>
<body>
    <c:forEach items="${Parkplätze.getParkingLotList()}" var="pl">
        <option name="Etagen">
                ${pl.level}${pl.levelId} ${pl.occupied ? "besetzt von TicketID: " : "frei"} ${pl.ticketId}
        </option>
    </c:forEach>
<br/>
<br/>
<br/>
<form action="parkhaus.jsp">
    <input type="submit" value="Zurück zur Startseite" />
</form>
</body>
</html>
