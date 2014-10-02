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
            <h1 class="text-center">Cadastro de Novo Post</h1>
            <div class="row">
                <div class="col-md-6">
                    <form action="${pageContext.servletContext.contextPath}/post/create" method="POST" class="input-group col-md-12">

                        <input type="hidden" name="id_autor" value="${usuarioLogado.id}">

                        <label>Título</label><br>
                        <input type="text" name="titulo" class="form-control input-lg" ><br><br>

                        <label>Conteúdo:</label><br>
                        <textarea name="conteudo" id="conteudo" rows="7" class="form-control"></textarea><br><br>

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