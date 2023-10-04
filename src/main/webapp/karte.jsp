<%--
  Created by IntelliJ IDEA.
  User: oezle
  Date: 26.04.2023
  Time: 19:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="de">
<head>
    <title>Kartenzahlung</title>
</head>
<body>
<form method="post" action="parkhaus-servlet">
  <%--@declare id="karte"--%><label for="Karte">Karte:</label><br/>
      <input type="submit" value="Einführen" id="eingeführt" formaction="parkhaus-servlet?action=in">
</form>

</body>
</html>
