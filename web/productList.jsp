<%-- 
    Document   : bookList
    Created on : Dec 1, 2020, 10:00:27 AM
    Author     : pupil
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Продукты</title>
    </head>
    <body>
        <h1>Список продуктов</h1>
        <div style="display: flex; flex-wrap: wrap">
            <c:forEach var="product" items="${productList}" varStatus = "status">
                <div style="display:flex; flex-direction: column; border: solid black 1px; width: 200px; 
                            justify-content: center; align-items: center; margin: 10px;">
                    <p>${status.index+1}. ${product.name}</p>
                    <p>Цена: ${product.price}</p>
                    <p>Количество: ${product.quantity}</p>
                </div>
            </c:forEach>
        </div>
        <a href="index.jsp">На главную</a>
    </body>
</html>
