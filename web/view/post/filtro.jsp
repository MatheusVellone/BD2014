<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${empty sessionScope.usuarioLogado}">
    <c:redirect context="${pageContext.servletContext.contextPath}" url="/"/>
</c:if>
<!DOCTYPE html>
<html>
    <head>
        <title>[BD 2014] Filtro</title>
    </head>
    <body class="container-fluid">
        <%@include file="/menu.jsp" %>
        <script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/like.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/comentario.js"></script>
        <% int i = 0;%>
        <div class="well">
            <c:forEach var="p" items="${postList}" varStatus="index">
                <div class="panel panel-default container-fluid">
                    <div class="panel-heading row">
                        <div class="col-md-8 h4">
                            #<%= ++i%> <c:out value="${p.titulo}"/> <c:if test="${p.republicacao}">
                                (Post original de <a href="${pageContext.servletContext.contextPath}/usuario/perfil?id=<c:out value="${repubAutores[repost].id}"/>"><c:out value="${repubAutores[repost].nome}"/></a>)
                                <c:set var="repost" value="${repost+1}"/>
                            </c:if>
                        </div>
                        <div class="col-md-3 text-right">
                            <div class="col-md-6">
                                <span class="badge bdg-like-<c:out value="${p.id}"/>">
                                    <c:out value="${likeList[index.index].like}"/>
                                </span> <a id_usuario="<c:out value="${usuarioLogado.id}"/>" id_post="<c:out value="${p.id}"/>" class="glyphicon glyphicon-thumbs-up btn btn-primary like"></a>
                            </div>
                            <div class="col-md-6">
                                <span class="badge bdg-dislike-<c:out value="${p.id}"/>">
                                    <c:out value="${likeList[index.index].dislike}"/>
                                </span> <a id_usuario="<c:out value="${usuarioLogado.id}"/>" id_post="<c:out value="${p.id}"/>" class="glyphicon glyphicon-thumbs-down btn btn-danger dislike"></a>
                            </div>
                        </div>
                        <div class="col-md-1">
                            <a href="${pageContext.servletContext.contextPath}/post/repost?id_post=<c:out value="${p.id}"/>" class="glyphicon glyphicon-retweet btn btn-primary"></a>
                        </div>
                    </div>
                    <div class="panel-body">
                        <c:out value="${p.conteudo}" escapeXml="false"/>
                    </div>
                </div>
            </c:forEach>
        </div>
    </body>
</html>