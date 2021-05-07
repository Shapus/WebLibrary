<%-- 
    Document   : sell
    Created on : Dec 2, 2020, 9:21:35 AM
    Author     : pupil
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Selling product</title>
        <style>
            .product-item:hover{
                background-color: lightgray;
            }
        </style>
    </head>
    <body>
        <h1>Список продуктов</h1>
        <p>${info}</p>
        <div style="display: flex; flex-wrap: wrap">
            <c:forEach var="product" items="${productList}" varStatus = "status">
                <form action="createDeal" method="POST" style="display:flex; flex-direction: column; border: solid black 1px; width: 200px; 
                     justify-content: center; align-items: center; margin: 10px;">
                    <p>${status.index+1}. ${product.name}</p>
                    <p>Цена: ${product.price}</p>
                    <p>Количество: ${product.quantity}</p>
                    <div style="padding: 10px">
                        <label>Выберите количство:</label> 
                        <input type="number" name="quantity">
                    </div>
                    <input type="hidden" value="${product.id}" name="productId" >
                    <input type="submit" value="Купить" style="padding: 10px; margin: 10px">
                </form>
            </c:forEach>
        </div>
        <a href=".">На главную</a>
    </body>
</html>
