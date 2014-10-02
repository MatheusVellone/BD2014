<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${empty sessionScope.usuarioLogado}">
    <c:redirect context="${pageContext.servletContext.contextPath}" url="/"/>
</c:if>
<!DOCTYPE html>
<html>
    <head>
        <title>[BD 2014] Busca</title>
    </head>
    <style>
        .highlight {
            background-color: #FF0;
        }
    </style>
    <body class="container-fluid">
        <%@include file="/menu.jsp" %>
        <script>
            $(document).ready(function() {
                $('#resultado_pesquisa').highlight('<c:out value="${termo_pesquisa}"/>');
            });
        </script>
        <script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/like.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/comentario.js"></script>
        <div class="well" id="resultado_pesquisa">

            <div class="panel panel-info" id="pessoas">
                <div class="panel-heading h3 text-center">
                    Pessoas encontradas pela busca de <c:out value="${termo_pesquisa}"/>
                </div>
                <div class="panel-body">
                    <c:forEach var="u" items="${listUsuario}" varStatus="index">
                        <div class="row">
                            <div class="col-md-2 text-center">
                                <a href="${pageContext.servletContext.contextPath}/usuario/perfil?id=${u.id}">
                                    <img class="img-rounded img-circle img-responsive img-thumbnail" src='${pageContext.servletContext.contextPath}/img/<c:out value="${u.foto}"/>'>
                                    <c:out value="${u.login}"/>
                                </a>
                            </div>
                            <div class="col-md-7">
                                <div class="h3">
                                    <c:out value="${u.nome}"/>
                                </div>
                                <c:out value="${u.descricao}"/>
                            </div>
                            <div class="col-md-3">
                                <c:choose>
                                    <c:when test="${segue_ou_nao[index.index]}">
                                        <a href="${pageContext.servletContext.contextPath}/usuario/pararDEseguir?id_segue=${u.id}" class="btn btn-danger">Des-Seguir</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.servletContext.contextPath}/usuario/seguir?seguir=${u.id}" class="btn btn-success">Seguir</a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <hr>
                    </c:forEach>
                </div>
            </div>
            <hr>
            <div class="panel panel-info" id="posts">
                <div class="panel-heading h3 text-center">
                    Posts encontradas pela busca de <c:out value="${termo_pesquisa}"/>
                </div>
                <% int i = 0;%>
                <div class="panel-body">
                    <c:forEach var="p" items="${listPost}" varStatus="index">
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
            </div>
            <hr>
            <div class="panel panel-info" id="pessoas">
                <div class="panel-heading h3 text-center">
                    Grupo encontrados pela busca de <c:out value="${termo_pesquisa}"/>
                </div>
                <div class="panel-body">
                    <c:forEach var="g" items="${listGrupo}">
                        <div class="row">
                            <div class="col-md-12 h3">
                                <c:out value="${g.nome}"/>
                            </div>
                        </div>
                        <hr>
                    </c:forEach>
                </div>
            </div>
        </div>
    </body>
</html>
