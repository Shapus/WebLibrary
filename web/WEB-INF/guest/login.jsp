<%-- 
    Document   : login
    Created on : Dec 3, 2020, 8:31:42 AM
    Author     : pupil
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/jspf/header.jspf" %>
    <div class="row">
        <div class="col-md-4">
            <h2>Вход</h2>
            <p class="text-danger">${info}</p>
        </div>
    </div>
    <div class="row">
        <form class="col-md-10" action="login" method="POST">
            <div class="form-group row">   
                <label class="col-sm-4 col-form-label">Имя пользователя</label>
                <div class="col-sm-8">
                    <input class="form-control" type="text" name="login" value="">
                </div>
            </div> 
            <div class="form-group row">   
                <label class="col-sm-4 col-form-label">Пароль</label>
                <div class="col-sm-8">
                    <input class="form-control" type="password" name="password" value="">
                </div>
            </div> 
            <div class="form-group row align-items-end justify-content-between px-3">
                <input class="btn btn-primary col-md-2" type="submit" name="submit" value="Войти">
                <a class="text-muted" href=".">На главную</a>
            </div>
        </form>
    </div>
<%@include file="/WEB-INF/jspf/footer.jspf" %>

