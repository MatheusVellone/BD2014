<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${empty sessionScope.usuarioLogado}">
    <c:redirect context="${pageContext.servletContext.contextPath}" url="/"/>
</c:if>
<!DOCTYPE html>
<html>
    <head>
        <title>[BD 2014] Grupos</title>
    </head>
    <body class="container-fluid">
        <%@include file="/menu.jsp" %>
        <div class="well">
            <h1>Cadastro de grupo</h1>

            <form action="${pageContext.servletContext.contextPath}/grupo/create" method="POST" class="input-group">
                <input type="hidden" name="id_admin" value="${usuarioLogado.id}">

                <label>Nome do Grupo:</label><br>
                <input type="text" name="nome" class="form-control" placeholder="Ex: Família, Amigos"><br><br>

                <input type="submit" value="Enviar" class="btn btn-primary">
            </form>
        </div>
    </body>
</html>