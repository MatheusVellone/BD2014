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
            <h1>Edição do usuário <c:out value="${usuario.nome}"/></h1>

            <form action="${pageContext.servletContext.contextPath}/usuario/update" method="POST" enctype="multipart/form-data" class="input-group">
                <label>ID:</label><br>
                <input type="text" name="id_disabled" value="${usuario.id}" disabled><br><br>

                <label>Login:</label><br>
                <input type="text" name="login" value="${usuario.login}" class="form-control"><br><br>

                <label>Senha:</label><br>
                <input type="password" name="senha" class="form-control" placeholder="Nova senha">
                <p class="help-block">Deixar o campo em branco manterá sua senha atual</p>

                <label>Nome:</label><br>
                <input type="text" name="nome" value="${usuario.nome}" class="form-control"><br><br>

                <label>Data de nascimento:</label><br>
                <input type="date" name="nascimento" value="${usuario.nascimento}" class="form-control"><br><br>

                <label>Descrição:</label><br>
                <textarea name="descricao" rows="7" class="form-control">${usuario.descricao}</textarea><br><br>

                <label>Foto do Perfil:</label><br>
                <input type="file" name="foto" id="fileChooser">
                <p class="help-block">Não escolher nenhuma foto, manterá sua foto atual</p>

                <input type="hidden" name="id" value="${usuario.id}">

                <input type="submit" value="Enviar" class="btn btn-primary">
            </form>
        </div>
    </body>
</html>