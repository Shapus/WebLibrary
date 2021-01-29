<%-- 
    Document   : page1
    Created on : Nov 24, 2020, 10:43:35 AM
    Author     : pupil
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/jspf/header.jspf" %>

        <div class="row">
            <div class="col-md-4">
                <h2>Изменить Продукт</h2>
                <p class="text-danger">${info}</p>
            </div>
        </div>
        <div class="row">
            <form class="col-md-8" action="changeProductAnswer" method="POST">
                <div class="form-group row"> 
                    <label class="col-sm-2 col-form-label">Название</label>
                    <div class="col-sm-10">
                        <input class="form-control" type="text" name="name" value="${name}">
                    </div>
                </div>
                <div class="form-group row">   
                    <label class="col-sm-2 col-form-label">Цена</label>
                    <div class="col-sm-10">
                        <input class="form-control" type="number" name="price" value="${price}">
                    </div>
                </div> 
                <div class="form-group row">   
                    <label class="col-sm-2 col-form-label">Количество</label>
                    <div class="col-sm-10">
                        <input class="form-control" type="number" name="quantity" value="${quantity}">
                    </div>
                </div> 
                <input type="hidden" name="id" value="${id}">
                <div class="form-group row align-items-end justify-content-between px-3">
                    <input class="btn btn-primary" type="submit" name="submit" value="Подтвердить">
                    <a class="text-muted" href=".">На главную</a>
                </div>
            </form>
        </div>

<%@include file="/WEB-INF/jspf/footer.jspf" %>
