<%-- 
    Document   : createUser
    Created on : Nov 27, 2020, 10:59:22 AM
    Author     : pupil
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/jspf/header.jspf" %>
    <div class="row">
        <div class="col-md-8">
            <h2 class="text-success">Пользователь ${login} создан</h2>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8">
            <a class="text-muted" href=".">На главную</a>
        </div>
    </div>
<%@include file="/WEB-INF/jspf/footer.jspf" %>
