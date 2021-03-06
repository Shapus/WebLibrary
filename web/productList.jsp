<%-- 
    Document   : bookList
    Created on : Dec 1, 2020, 10:00:27 AM
    Author     : pupil
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/jspf/header.jspf" %>
<%
    Role role = Role.GUEST;
    String money = "";
    if((User)session.getAttribute("user") != null){
        User user = (User)session.getAttribute("user");
        role = user.getRole();
        money = "$"+String.valueOf(user.getMoney());
    }
    String deal_info = (String)session.getAttribute("deal_info");
    request.setAttribute("deal_info", deal_info);
    request.setAttribute("user_money", money);
%>
    <div class="row my-4">
        <div class="col-md-2">
            <h3>${user_money}</h3>
        </div>
        <div class="col-md-8">
            <h2>Список продуктов</h2>
        </div>
    </div>

    <div class="row">
        <div class="col-2">
            <h5 class="mb-4">Информация:</h5>
            <p class="text-danger">${deal_info}</p>
        </div>
        <div class="col-xl-10">
            <div class="row">
            <c:forEach var="product" items="${productList}" varStatus = "status">
                <div class="col-lg-4 pb-4">
                    <div class="card">
                        <h5 class="card-header <c:choose><c:when test="${product.deleted==true}">bg-danger text-light</c:when></c:choose>">${product.name}</h5>
                        <div class="card-body <c:choose><c:when test="${product.deleted==true || product.quantity==0}">bg-light</c:when></c:choose>">
                            <p class="card-text">Цена: ${product.price}</p>
                            <p class="card-text">В наличии: ${product.quantity}</p>
                            <% if(role == Role.GUEST){%>

                            <% }else if(role == Role.ADMIN){ %>
                                <a class="btn btn btn-primary" href="changeProductForm?id=${product.id}">Изменить</a>
                                <c:choose>
                                    <c:when test="${product.deleted==true}">
                                        <a class="btn btn btn-secondary float-right" href="restoreProduct?id=${product.id}">Восстановить</a>
                                    </c:when>    
                                    <c:otherwise>
                                        <a class="btn btn btn-danger float-right" href="deleteProduct?id=${product.id}">Удалить</a>
                                    </c:otherwise>
                                </c:choose>
                            <% }else if(role == Role.USER){%>
                                <form class="" action="createDeal" method="POST">
                                    <div class="form-group">
                                        <label class="col-form-label">Выберите количство:</label> 
                                        <input type="number" name="quantity">
                                    </div>
                                    <div class="form-group">
                                        <input type="hidden" value="${product.id}" name="productId" >
                                        <input class="form-control btn btn-primary" type="submit" value="Купить">
                                    </div>
                                </form>
                            <%}%>
                        </div>
                    </div>
                </div>
            </c:forEach>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8 offset-md-2">
            <a href=".">На главную</a>
        </div>
    </div>
           
<% request.getSession().removeAttribute("deal_info");  %>  
    
