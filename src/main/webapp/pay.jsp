<%--
  Created by IntelliJ IDEA.
  User: oezle
  Date: 24.04.2023
  Time: 16:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="de">
<head>
    <title>Parkautomat</title>

</head>
<body>
<%--@declare id="zahlungsart"--%><br/>

<form method="post" action="parkhaus-servlet">
    <label for="zahlungsart">Zahlungsart:</label><br/>
    <input type="submit" value="Bar" formaction="${pageContext.request.contextPath}/parkhaus-servlet?action=bar">
    <input type="submit" value="Karte" formaction="${pageContext.request.contextPath}/parkhaus-servlet?action=karte">
</form>

<%--<jsp:include page="parkhaus.jsp"></jsp:include>--%>
</body>
</html>


