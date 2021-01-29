<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="entities.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!-- Navbar start -->
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href=".">Market</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse justify-content-between" id="navbarNavAltMarkup">
        <div class="navbar-nav">
            <a class="nav-item nav-link" href="addProduct">Создать продукт</a>
            <a class="nav-item nav-link" href="productList">Список продуктов</a>
        </div>
        <div class="navbar-nav">
            <%
            if((User)session.getAttribute("user")==null){
            %>
                <a class="nav-item nav-link" href="login" >Войти</a>
                <a class="nav-item nav-link" href="registration" >Зарегистрироваться</a>
            <%
            }
            else{
                String name=((User)session.getAttribute("user")).getLogin();
                request.setAttribute("userName", name);
            %>
                <div class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                      ${userName}
                    </a>
                    <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                      <a class="nav-item nav-link" href="logout">Выйти</a>
                    </div>
                </div>
            <%
            }
            %>
        </div>   
      </div>
    </div>
</nav>
<!-- Navbar end -->

    