<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="de">
<head>
    <title>Ticketübersicht:</title>
</head>
<body>
<h3> Tagestickets</h3>
    <c:forEach items="${TicketIDs}" var="pl">
        <option>
            ID: ${pl.value.ticketID}, State: ${pl.value.getStateDescription()}, Start: ${pl.value.startDate}, Ende: ${pl.value.endDate == null ? "/" : pl.value.endDate}
        </option>
    </c:forEach>
<br/>
<br/>
<br/>
<h3> Monatstickets</h3>
<c:forEach items="${MTIDs}" var="mt">
    <option>
        ID: ${mt.value.getID()}, gültig von: ${mt.value.getstartdate()} bis: ${mt.value.getenddate()}, im Parkhaus: ${mt.value.getIsInPH() ? "ja" : "nein" }, geparkt: ${mt.value.isCurrentlyInParkingLot() ? "ja" : "nein"}
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
