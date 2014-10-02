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
        <script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/dropzone.js"></script>
        <script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/post-media.js"></script>
        <link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/css/dropzone.css"/>
        <div class="well">
            <h1 class="text-center">Edição do Post <c:out value="${post.titulo}"/></h1>
            <div class="row">
                <div class="col-md-6">
                    <form action="${pageContext.servletContext.contextPath}/post/update" method="POST" class="input-group col-md-12">

                        <label>ID:</label><br>
                        <input type="text" name="id_disabled" value="${post.id}" class="form-control" disabled><br><br>

                        <label>Título</label><br>
                        <input type="text" name="titulo" value="${post.titulo}" class="form-control"><br><br>

                        <label>Conteúdo</label><br>
                        <textarea name="conteudo" rows="7" id="conteudo" class="form-control">${post.conteudo}</textarea><br><br>

                        <input type="hidden" name="id" value="${post.id}">

                        <input type="submit" value="Enviar" class="btn btn-primary">
                    </form>
                </div>
                <div class="col-md-6">
                    <form id="my-awesome-dropzone" action="${pageContext.servletContext.contextPath}/file/upload" class="dropzone" enctype="multipart/form-data">

                    </form>
                </div>
            </div>
        </div>
    </body>
</html>