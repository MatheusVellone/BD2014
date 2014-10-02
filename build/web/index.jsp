<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/css/bootstrap.css" />
        <title>[BD 2014] Login</title>
    </head>
    <style>
        body{
            background: url(${pageContext.servletContext.contextPath}/img/default-bg.png) no-repeat fixed center center;
        }
    </style>
    <body class="container-fluid">
        <div class="row">
            <div class="col-md-offset-4 col-md-4 well">
                <form action="${pageContext.servletContext.contextPath}/login" method="POST">
                    <label>Login:</label><br>
                    <input type="text" name="login" class="form-control"><br><br>

                    <label>Senha:</label><br>
                    <input type="password" name="senha" class="form-control"><br><br>

                    <input type="submit" value="Login" class="btn btn-primary col-md-4 col-md-offset-4">
                </form>

                <a href="${pageContext.servletContext.contextPath}/usuario/create" class="btn btn-info col-md-12">Não possui cadastro?</a>
            </div>
        </div>
    </body>
</html>