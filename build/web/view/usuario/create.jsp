<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <title>[BD 2014] Usuários</title>
    </head>
    <body class="container-fluid">
        <div class="well">
            <h1>Cadastro de usuário</h1>

            <form action="${pageContext.servletContext.contextPath}/usuario/create" method="POST" enctype="multipart/form-data" class="input-group">
                <label>Login:</label><br>
                <input type="text" name="login" class="form-control"><br><br>

                <label>Senha:</label><br>
                <input type="password" name="senha" class="form-control"><br><br>

                <label>Nome:</label><br>
                <input type="text" name="nome" class="form-control"><br><br>

                <label>Data de nascimento:</label><br>
                <input type="date" name="nascimento" class="form-control"><br><br>

                <label>Descrição:</label><br>
                <textarea name="descricao" rows="7" class="form-control"></textarea><br><br>

                <label>Foto do Perfil:</label><br>
                <input type="file" name="foto" id="fileChooser"/><br/><br/>

                <input type="submit" value="Enviar" class="btn btn-primary">
            </form>
        </div>
    </body>
</html>