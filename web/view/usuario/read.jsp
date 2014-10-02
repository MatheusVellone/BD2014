<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${empty sessionScope.usuarioLogado}">
    <c:redirect context="${pageContext.servletContext.contextPath}" url="/"/>
</c:if>
<!DOCTYPE html>
<html>
    <head>
        <title>[BD 2014] Usuários</title>
    </head>
    <body class="container-fluid">
        <%@include file="/menu.jsp" %>
        <div class="well">
            <h1>Visualização do usuário <c:out value="${usuario.nome}"/></h1>
            <img width="300" src='${pageContext.servletContext.contextPath}/img/<c:out value="${usuario.foto}"/>'>
            <ul>
                <li>ID: <c:out value="${usuario.id}"/></li>
                <li>Login: <c:out value="${usuario.login}"/></li>
                <li>Nome: <c:out value="${usuario.nome}"/></li>
                <li>Data de nascimento: <c:out value="${usuario.nascimento}"/></li>
                <li>Descrição: <c:out value="${usuario.descricao}"/></li>
            </ul>
        </div>
    </body>
</html>