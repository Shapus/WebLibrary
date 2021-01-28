<%-- 
    Document   : index
    Created on : Nov 27, 2020, 9:14:26 AM
    Author     : pupil
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="entities.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true" %>

<%@include file="/WEB-INF/jspf/header.jspf" %>

    
    <br>
    <div style="display: flex; align-items: center; justify-content: space-around; width: 30%" >
        <a href="addProduct" class="action">Создать продукт</a>
        <br>

        <a href="productList" class="action">Список продуктов</a>
        <br>
        <a href="buyProduct" class="action">Купить продукт</a>
        <br>
    </div>

<%@include file="/WEB-INF/jspf/footer.jspf" %>