<%-- 
    Document   : page1
    Created on : Nov 24, 2020, 10:43:35 AM
    Author     : pupil
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/jspf/header.jspf" %>
        <div class="row">
            <div class="col-md-8">
                <h2>Добавить Продукт</h2>
                <p class="text-danger">${info}</p>
            </div>
        </div>
        <div class="row">
            <form class="col-lg-8" action="./createProduct" method="POST" enctype="multipart/form-data">
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
                <div class="form-group row">   
                    <label class="col-sm-2 col-form-label">Изображение</label>
                    <div class="col-sm-10">
                    <input class="" type="file" name="file" value="" onchange="loadFile(event)">
                    <img id="uploaded_image" class="mt-2">
                </div>
                </div> 
                <div class="form-group row align-items-end justify-content-between px-3">
                    <input class="btn btn-primary" type="submit" name="submit">
                    <a class="text-muted" href=".">На главную</a>
                </div>
            </form>
        </div>
<%@include file="/WEB-INF/jspf/footer.jspf" %>

<script>
var loadFile = function(event) {
	var image = document.getElementById('uploaded_image');
	image.src = URL.createObjectURL(event.target.files[0]);
};
</script>