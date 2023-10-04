<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="de">
<head>
    <title>Wähle eine Etage aus:</title>
</head>
<body>
    <form method="post" action="parkhaus-servlet">
        <input type="hidden" name="action" value="pickLevel">
        <label>
            <select name="level">
                <c:forEach items="${Parkplätze.getLevelsAsList()}" var="p">
                    <option name="Etagen">
                            ${p}
                    </option>
                </c:forEach>
            </select>
        </label>

        <input type="submit"  value="Etage wählen">
    </form>
    <br/>
    <form action="parkhaus.jsp">
        <input type="submit" value="Zurück zur Startseite" />
    </form>
    </body>
</html>
