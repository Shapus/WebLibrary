<%-- 
    Document   : createUser
    Created on : Nov 27, 2020, 10:59:22 AM
    Author     : pupil
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/jspf/header.jspf" %>
    <div class="row justify-content-center mt-5">
        <div class="col-6 alert alert-success d-flex justify-content-center align-items-center" style="height: 10em;">
            <h2 class="text-success">Пользователь ${login} создан</h2>
        </div>
    </div>
    <div class="row justify-content-center">
        <div class="col-6 text-center">
            <a class="text-muted" href=".">На главную</a>
        </div>
    </div>
<%@include file="/WEB-INF/jspf/footer.jspf" %>
