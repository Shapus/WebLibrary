<%-- 
    Document   : bookList
    Created on : Dec 1, 2020, 10:00:27 AM
    Author     : pupil
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="entities.User"%>
<%@page import="entities.User.Role"%>
<%@include file="/WEB-INF/jspf/header.jspf" %>
    <div class="row">
        <div class="col-md-8 offset-md-2 my-4">
            <h1>Список пользователей</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-2">
            <h5 class="mb-4">Информация:</h5>
            <p class="text-danger">${deal_info}</p>
        </div>
        <div class="col-xl-10">
            <table class="table">
                <tr>
                    <th>Пользователь</th>
                    <th>Роль</th>
                    <th></th>
                </tr>
                <tbody>
            <c:forEach var="user" items="${userList}" varStatus = "status">
                <tr>
                    <td>${user.login}</td>
                    <td>${user.role}</td>
                    <td>
                        <c:choose>
                            <c:when test="${user.role == \"ADMIN\"}">
                                <a class="btn btn-danger disabled" href="">Заблокировать</a>
                            </c:when>    
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${user.deleted == \"false\"}">
                                        <a class="btn btn-danger" href="blockUser?id=${user.id}">Заблокировать</a>
                                    </c:when>    
                                    <c:otherwise>
                                        <a class="btn btn-secondary" href="restoreUser?id=${user.id}">Восстановить</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
                <tbody>
            </table>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8 offset-md-2">
            <a href="index.jsp">На главную</a>
        </div>
    </div>
           
<% request.getSession().removeAttribute("deal_info");  %>  
    
