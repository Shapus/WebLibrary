<%-- 
    Document   : page2
    Created on : Nov 24, 2020, 10:52:35 AM
    Author     : pupil
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/jspf/header.jspf" %>

    <div class="row">
        <div class="col-md-8">
            <div class="form-group">
                <h2>Продукт создан</h2>
            </div>
            <div class="form-group row">
                <p class="col-form-label">Название:</p>
                <p class="form-control">${name}</p>
            </div>
            <div class="form-group row">
                <p class="col-form-label">Цена</p>
                <p class="form-control">${price}</p>
            </div>
            <div class="form-group row">
                <a class="text-muted" href=".">На главную</a>
            </div>
        </div>
    </div>

<%@include file="/WEB-INF/jspf/footer.jspf" %>
