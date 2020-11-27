<%-- 
    Document   : addUserForm
    Created on : Nov 27, 2020, 10:59:37 AM
    Author     : pupil
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h2>Добавить пользователя</h2>
        <p>${info}</p>
        <form action="createUser" method="POST" style="display: flex; flex-direction: column; width: 400px; height: 200px; justify-content: space-around">
            <div>   
                <label>Имя пользователя</label>
                <input type="text" name="login" value="${login}">
            </div> 
            <div>   
                <label>Пароль</label>
                <input type="password" name="password" value="">
            </div> 
            <input type="submit" name="submit">
        </form>
    </body>
</html>
