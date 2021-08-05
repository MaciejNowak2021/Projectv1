<%--
  Created by IntelliJ IDEA.
  User: maciej
  Date: 30.07.2021
  Time: 18:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%@ include file="/admin/header.jsp" %>
<h2>Lista uterek:</h2><table border="solid">
    <c:forEach items="${faultOrder}" var="ord">


        <tr>
            <td>ID:</td><td>Klient</td><td>Adres</td><td>Opis:</td><td>Status</td><td>Wykonawca:</td><td>Akcje:</td>
        </tr>
        <tr>
            <td>${ord.id}</td><td>${ord.client.fullName}</td><td>${ord.address.fullAddress}</td><td>${ord.description}</td><td>${ord.status}</td><td>${ord.user.fullName}</td><td><a href="http://localhost:8080/faultOrder/update?id=${ord.id}">Edytuj</a> </td>
        </tr>

    </c:forEach>
</table>
</body>
</html>
