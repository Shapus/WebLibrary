<%-- 
    Document   : bookList
    Created on : Dec 1, 2020, 10:00:27 AM
    Author     : pupil
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/jspf/header.jspf" %>
<div class="row">
    <div class="col-md-8">
        <h1>Список продуктов</h1>
    </div>
</div>
        
        <div class="row">
            <c:forEach var="product" items="${productList}" varStatus = "status">
                <div class="col-md-4 pb-4">
                    <div class="card">
                        <h5 class="card-header">${product.name}</h5>
                        <div class="card-body">
                            <p class="card-text">Цена: ${product.price}</p>
                            <p class="card-text">В наличии: ${product.quantity}</p>
                            <div>
                                <% if((User)session.getAttribute("user") == null){%>
                                
                                <% }else if(((User)session.getAttribute("user")).getRole() == Role.GUEST){ %>
                                <a class="btn btn btn-primary" href="changeProduct?id=${product.id}">Изменить</a>
                                <a class="btn btn btn-danger float-right" href="deleteProduct?id=${product.id}">Удалить</a>
                                <% }else{%>
                                <a class="btn btn-primary" href="buyProduct?id=${product.id}">Купить</a>
                                <%}%>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
        <a href="index.jsp">На главную</a>
    </body>
</html>
