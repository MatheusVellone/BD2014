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
            <h1>Edição do grupo <c:out value="${grupo.id}"/></h1>

            <form action="${pageContext.servletContext.contextPath}/grupo/update" method="POST">
                <label>ID:</label><br>
                <input type="text" name="id_disabled" value="${grupo.id}" disabled><br><br>

                <label>Nome:</label><br>
                <input type="text" name="nome" value="${grupo.nome}"><br><br>

                <input type="hidden" name="id" value="${grupo.id}">
                <input type="hidden" name="id_admin" value="${grupo.id_admin}">

                <input type="submit" value="Enviar" class="btn btn-primary">
            </form>
        </div>
    </body>
</html>