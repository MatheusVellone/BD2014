<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${empty sessionScope.usuarioLogado}">
    <c:redirect context="${pageContext.servletContext.contextPath}" url="/"/>
</c:if>
<!DOCTYPE html>
<html>
    <head>
        <title>[BD 2014] Posts</title>
    </head>
    <body class="container-fluid">
        <%@include file="/menu.jsp" %>
        <div class="well">
            <h1 class="text-center">Lista de posts de todos usuários</h1>

            <% int i = 0;%>
            <div class="well">
                <c:forEach var="p" items="${postList}">
                    <h3>#<%= ++i%> - <c:out value="${p.titulo}"/></h3>
                    <p><c:out value="${p.conteudo}" escapeXml="false"/></p>
                    <!--<a href="${pageContext.servletContext.contextPath}/post/update?id=${p.id}">Editar</a>-->
                    <hr>
                </c:forEach>
            </div>
        </div>
    </body>
</html>