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
            <h1>Lista de grupos</h1>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Nome do Grupo</th>
                        <th>Nome do Administrador</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="g" items="${grupoList}" varStatus="index">
                        <tr>
                            <td><c:out value="${g.nome}"/></td>
                            <td><c:out value="${listAdmin[index.index]}"/></td>
                            <td><a href="${pageContext.servletContext.contextPath}/grupo/entrar?grupo=${g.id}&usuario=${usuarioLogado.id}">Entrar</a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>