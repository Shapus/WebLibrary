<%-- 
    Document   : index
    Created on : Nov 27, 2020, 9:14:26 AM
    Author     : pupil
--%>

<%@page import="entities.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JKTVR19_WEBLibrary</title>
    </head>
    <style>
        a{
            text-decoration: none;
            color: black;
        }
        .login{
            
            border: solid black 1px;
            width: max-content;
            padding: 5px;
            
        }
        .login:hover{
            background-color: black;
            color: white;
        }
        .action{
            
        }
        .action:hover{
            text-decoration: underline;
        }
    </style>
    <body>
        <h1>Магазин</h1>
        <p><%
            if((User)session.getAttribute("user")==null){
            %>
            <div>
                <a href="login" class="login">Войти</a> /
                <a href="registration" class="login">Зарегистрироваться</a>
            </div>
            <%
            }
            else{
                String name=((User)session.getAttribute("user")).getLogin();
                request.setAttribute("userName", name);
            %>
            <div>
                <p>${userName}</p>
                <a href="logout" class="login">Выйти</a>
            </div>
            <%
            }
        %></p>
        <br>
        <div style="display: flex; align-items: center; justify-content: space-around; width: 30%" >
            <a href="addProduct" class="action">Создать продукт</a>
            <br>
            
            <a href="productList" class="action">Список продуктов</a>
            <br>
            <a href="buyProduct" class="action">Купить продукт</a>
            <br>
        </div>
    </body>
</html>
