<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="de">
<head>
    <title>Parkautomat</title>

</head>
<body>

<%--<%long amount = (long) request.getAttribute("amount");%>
<%response.getWriter().println(amount);%>--%>
<br>
<p>Bitte zahlen Sie ${amount} Euro</p>

<br/>
<form method="post" action="parkhaus-servlet">
    <input type="hidden" name="amount" value=${amount}>
    <input type="hidden" name="ID" value="${ID}">
    <input type="submit" value="Bezahlen" formaction="${pageContext.request.contextPath}/parkhaus-servlet?action=payExtraMT">
</form>

</body>
</html>


