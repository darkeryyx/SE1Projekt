<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="de">
<head>
    <title>Ausparken:</title>
</head>
<body>
<form method="post" action="parkhaus-servlet">
<input type ="hidden" name = "action" value = "leaveParkingLot"/>
    <label>
        <select name="id">
            <c:forEach items="${Parkplätze.occupiedParkingLots()}" var="p">
                <option name="belegte Parkplätze">
                        ${p.level}${p.levelId} ${p.occupied ? "besetzt" : "frei"} besetzt von ID ${p.ticketId}
                </option>
            </c:forEach>
        </select>
    </label>
    <input type="submit" value ="Parkplatz wählen">
</form>
<br/>
<br/>
<br/>
<form action="parkhaus.jsp">
    <input type="submit" value="Zurück zur Startseite" />
</form>
</body>
</html>
