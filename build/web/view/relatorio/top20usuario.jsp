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
            <div class="row h1">
                <div class="col-md-2">
                    Influência
                </div>
                <div class="col-md-2">
                    Foto
                </div>
                <div class="col-md-8">
                    Nome
                </div>
            </div>
            <c:forEach var="top" items="${top20}">
                <a href="${pageContext.servletContext.contextPath}/usuario/perfil?id=<c:out value="${top.id}"/>">
                    <div class="row">
                        <div class="col-md-2 h1">
                            <c:out value="${top.influencia}"/>
                        </div>
                        <div class="col-md-2">
                            <img src="${pageContext.servletContext.contextPath}/img/<c:out value="${top.foto}"/>" class="img-responsive">
                        </div>
                        <div class="col-md-8">
                            <c:out value="${top.nome}"/>
                        </div>
                    </div>
                </a>
            </c:forEach>
        </div>
    </body>
</html>