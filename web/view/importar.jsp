<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${empty sessionScope.usuarioLogado}">
    <c:redirect context="${pageContext.servletContext.contextPath}" url="/"/>
</c:if>
<!DOCTYPE html>
<html>
    <head>
        <title>[BD 2014] Importar</title>
    </head>
    <body class="container-fluid">
        <%@include file="/menu.jsp" %>
        <link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/css/dropzone.css"/>
        <div class="well">
            <div class="row">
                <div class="col-md-12">
                    <form action="${pageContext.servletContext.contextPath}/importar" method="POST" class="input-group col-md-12">
                        <label>URL para Importar</label><br>
                        <input type="text" name="url" class="form-control" ><br><br>

                        <input type="submit" value="Importar" class="btn btn-primary">
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>