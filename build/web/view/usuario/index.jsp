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
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th colspan="6" class="text-center">
                <h3>Lista de usuários</h3>
                </th>
                </tr>
                <tr>
                    <th>#</th>
                    <th>Foto</th>
                    <th>Login</th>
                    <th>Nome</th>
                    <th>Descrição</th>
                    <th>Ações</th>
                </tr>
                </thead>
                <tbody>
                    <c:forEach var="u" items="${usuarioList}" varStatus="index">
                        <tr>
                            <td><c:out value="${u.id}"/></td>
                            <td><img width="200" class="img-rounded img-circle" src='${pageContext.servletContext.contextPath}/img/<c:out value="${u.foto}"/>'></td>
                            <td>
                                <a href="${pageContext.servletContext.contextPath}/usuario/perfil?id=${u.id}">
                                    <c:out value="${u.login}"/>
                                </a>
                            </td>
                            <td><c:out value="${u.nome}"/></td>
                            <td><c:out value="${u.descricao}"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${segue_ou_nao[index.index]}">
                                        <a href="${pageContext.servletContext.contextPath}/usuario/pararDEseguir?id_segue=${u.id}" class="btn btn-danger"><span class="glyphicon glyphicon-remove-sign"></span> Des-Seguir</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.servletContext.contextPath}/usuario/seguir?seguir=${u.id}" class="btn btn-success"><span class="glyphicon glyphicon-ok-sign"></span> Seguir</a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>