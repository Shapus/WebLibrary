<%-- 
    Document   : addUserForm
    Created on : Nov 27, 2020, 10:59:37 AM
    Author     : pupil
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/jspf/header.jspf" %>

    <div class="row">
        <div class="col-md-10">
            <h2>Регистрация</h2>
            <p class="text-danger">${info}</p>
        </div>
    </div>
    <div class="row">
        <form class="col-md-10" action="createUser" method="POST">
            <div class="form-group">   
                <label class="col-form-label">Имя пользователя</label>
                <div>
                    <input class="form-control" type="text" name="login" value="">
                </div>
            </div> 
            <div class="form-group">   
                <label class="col-form-label">Пароль</label>
                <div>
                    <input class="form-control" type="password" name="password" value="">
                </div>
            </div> 
            <div class="form-group row align-items-end justify-content-between px-3">
                <input class="btn btn-primary col-md-4" type="submit" name="submit" value="Зарегистрироваться">  
                <a class="text-muted" href=".">На главную</a>
            </div>
        </form>
    </div>

<%@include file="/WEB-INF/jspf/footer.jspf" %>
