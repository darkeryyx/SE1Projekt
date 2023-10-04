<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="de">
<head>
    <h3>Monatsticket ziehen</h3>
</head>
<body>
<form action="parkhaus-servlet" method="post">
    Gewünschtes Startdatum: <input type="date" name="datum">
    <input type="hidden" name="ID" value="${ID}">
    <input type="submit" value="Monatsticket ziehen" formaction="${pageContext.request.contextPath}/parkhaus-servlet?action=mtziehen">
</form>
<br/>
</body>
</html>