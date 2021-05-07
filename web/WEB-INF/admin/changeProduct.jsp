<%-- 
    Document   : sell
    Created on : Dec 2, 2020, 9:21:35 AM
    Author     : pupil
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/jspf/header.jspf" %>

        <h1>Список продуктов</h1>
        <p>${info}</p>
        <div style="display: flex; flex-wrap: wrap">
            <c:forEach var="product" items="${productList}" varStatus = "status">
                <form action="changeProductForm" method="POST" style="display:flex; flex-direction: column; border: solid black 1px; width: 200px; 
                     justify-content: center; align-items: center; margin: 10px;">
                    <p>${status.index+1}. ${product.name}</p>
                    <p>Цена: ${product.price}</p>
                    <p>Количество: ${product.quantity}</p>
                    <img src="${product.image}">
                    <input type="hidden" value="${product.id}" name="productId" >
                    <input type="submit" value="Изменить" style="padding: 10px; margin: 10px">
                </form>
            </c:forEach>
        </div>
        <a href=".">На главную</a>

<%@include file="/WEB-INF/jspf/footer.jspf" %>
