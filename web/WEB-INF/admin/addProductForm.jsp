<%-- 
    Document   : page1
    Created on : Nov 24, 2020, 10:43:35 AM
    Author     : pupil
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Page 1</title>
    </head>
    <body>
        <h2>Добавить Продукт</h2>
        <p>${info}</p>
        <form action="createProduct" method="POST" style="display: flex; flex-direction: column; width: 400px; height: 200px; justify-content: space-around">
            <div>   
                <label>Название</label>
                <input type="text" name="name" value="${name}">
            </div> 
            <div>   
                <label>Цена</label>
                <input type="number" name="price" value="${price}">
            </div> 
            <div>   
                <label>Количество</label>
                <input type="number" name="quantity" value="${quantity}">
            </div> 
            <input type="submit" name="submit">
        </form>
        <a href="index.jsp">На главную</a>
    </body>
</html>
