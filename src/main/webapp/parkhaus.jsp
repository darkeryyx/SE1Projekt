<%@ page import="StartConf.StartConfig" %>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="de">
<head>
    <title>JSP - Parkhaus Startseite</title>
    <style>
        body {
            background-color: #e7e7e7; /* Hintergrundfarbe */
        <%--    background-image: url('hintergrundtextur.jpg'); /* Pfad zur Hintergrundtextur */
            background-repeat: repeat; /* Textur wiederholen */--%>

            display: flex;
            flex-direction: column;
            align-items: center;
            padding-top: 50px;
        }

        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            justify-items: center;
            align-items: center;
            text-align: center;
            margin-bottom: 20px;
        }

        h1, h3 {
            color: #2a2a2a;
        }

        .button {
            padding: 10px 20px;
            font-size: 16px;
            text-align: center;
            text-decoration: none;
            background-color: #4CAF50;
            color: #fff;
            border: none;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }

        .button:hover {
            background-color: #45a049;
        }

        form {
            display: flex;
            flex-direction: column;
            margin-top: 10px;
            margin-bottom: 10px;
        }

        label {
            display: block;
            margin-bottom: 10px;
        }

        input[type="text"],
        input[type="password"],
        input[type="number"],
        input[type="datetime-local"] {
            padding: 8px;
            font-size: 14px;
            border-radius: 3px;
            border: 1px solid #ccc;
        }

    </style>
</head>
<body>
<h1>Willkommen</h1>

<p>Anzahl der freien Parkplätze: <%=StartConfig.getInstance().getFrei()%></p>

<form action="parkhaus-servlet" method="get">
    <input type="hidden" name="aktion" value="getTime">
    <input type="submit" value="Aktuelle Uhrzeit" class="button">
</form>


<form action="parkhaus-servlet" method="get">
    <input type="hidden" name="aktion" value="ticketOverview">
    <input type="submit" value="Zeige die existierenden Tickets an" class="button">
</form>

<form action="parkhaus-servlet" method="get">
    <input type="hidden" name="aktion" value="std">
    <input type="submit" value="Stundenpreis anzeigen" class="button">
</form>
<div id="stundenpreis-container">
    <p>${stundenpreis}</p>
</div>

<h3>Tagesticket ziehen:</h3>
<form method="post" action="parkhaus-servlet">
    <input type="datetime-local" step="1" name="zeitpunkt" value="2023-05-04T09:00:00">
    <input type="submit" value="Parkhaus betreten" class="button" formaction="${pageContext.request.contextPath}/parkhaus-servlet?action=enter">
</form>

<h3>Betreten mit Monatsticket:</h3>
<form method="post" action="parkhaus-servlet">
    <input type="datetime-local" step="1" name="zeitpunkt" value="2023-05-04T09:00:00">
    <input type="text" name="MTID" placeholder="Monatsticket-ID eingeben" formaction="${pageContext.request.contextPath}/parkhaus-servlet?action=entermt">
    <input type="submit" value="Parkhaus betreten" class="button" formaction="${pageContext.request.contextPath}/parkhaus-servlet?action=entermt">
</form>



<h3>Zum Ein- & Ausparken bitte hier entlang:</h3>
<form method="get" action="parkhaus-servlet">
    <input type="hidden" name="aktion" value="parkMenu">
    <input type="submit" value="Parkmenü" class="button" />
</form>

<h3>Bezahlen:</h3>
<form method="post" action="parkhaus-servlet">
    <%--<label for="ID">Bitte Ticket-ID angeben:</label>--%>
    <input type="datetime-local" step="1" name="zeitpunkt" value="2023-05-04T09:30:00">
    <input type="text" id="ID" name="ID" placeholder="Ticket-ID eingeben" formaction="${pageContext.request.contextPath}/parkhaus-servlet?action=pay">
    <input type="submit" value="Bezahlen" class="button" formaction="${pageContext.request.contextPath}/parkhaus-servlet?action=pay">
</form>
<h3>Verlassen:</h3>
<form method="post" action="parkhaus-servlet">
    <input type="datetime-local" step="1" name="zeitpunkt" value="2023-05-04T09:35:00">
    <input type="text" id="ticketID" name="ticketID" placeholder="Ticket-ID eingeben" formaction="${pageContext.request.contextPath}/parkhaus-servlet?action=leave"> <%--notwendig?--%>
    <input type="submit" value="Parkhaus verlassen" class="button" formaction="${pageContext.request.contextPath}/parkhaus-servlet?action=leave">
</form>

<h3>Verwaltung:</h3>

<form action="parkhaus-servlet" method="post">
    <input type="password" name="password" placeholder="Passwort eingeben" required>
    <input type="submit" value="Bestätigen" class="button" formaction="${pageContext.request.contextPath}/parkhaus-servlet?action=admin">
</form>
</body>
</html>
