<%--
  Created by IntelliJ IDEA.
  User: oezle
  Date: 26.04.2023
  Time: 10:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="de">
<head>
    <title>Barzahlung</title>
</head>
<body>
<form method="post" action="parkhaus-servlet">
    <%--@declare id="münzen"--%><label for="Münzen">Münzen:</label><br/>
    <input type="button" value="2 Euro" onclick="einwurf('2 Euro')">
    <input type="button" value="1 Euro" onclick="einwurf('1 Euro')">
    <input type="submit" value="Bezahlen" formaction="parkhaus-servlet?action=bar">
        <input type="hidden" name="counter2Euro" id="counter2Euro" value="0">
        <input type="hidden" name="counter1Euro" id="counter1Euro" value="0">
</form>

<script>
    var counter2=0;
    var counter1=0;

    function einwurf(action) {
        if (action === '2 Euro') {
            counter2++;
            document.getElementById('counter2Euro').value = counter2;
            document.getElementById('counterDisplay2Euro').innerHTML = 'Anzahl Klicks 2 Euro: ' + counter2;
        } else if (action === '1 Euro') {
            counter1++;
            document.getElementById('counter1Euro').value = counter1;
            document.getElementById('counterDisplay1Euro').innerHTML = 'Anzahl Klicks 1 Euro: ' + counter1;
        }

    }
</script>

</body>
</html>
