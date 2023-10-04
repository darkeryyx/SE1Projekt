<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Lionel
  Date: 27.05.2023
  Time: 22:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="de">
<head>
    <title>Stats</title>
</head>
<body>

<%--Zeigt die betreiber.jsp an--%>
<jsp:include page="betreiber.jsp" />

<h4> Einnahmen</h4>
<ul>
  <%
    List<String> incomeList = (List<String>) request.getAttribute("incomeList"); // Listeninhalte ausgaben
    for (String einnahmeInfo : incomeList) {
  %>
  <li><%= einnahmeInfo %></li>
  <%
    }
  %>
</ul>

<h4>Auslastung</h4>
<ul>
  <%
    List<String> customerAndFloorList = (List<String>) request.getAttribute("customerAndFloorList");
    for (String autoInfo : customerAndFloorList) {
  %>
  <li><%= autoInfo %></li>
  <%
    }
  %>
</ul>
</body>
</html>
