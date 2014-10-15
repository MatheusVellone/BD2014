<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${empty sessionScope.usuarioLogado}">
    <c:redirect context="${pageContext.servletContext.contextPath}" url="/"/>
</c:if>
<!DOCTYPE html>
<html>
    <head>
        <title>[BD 2014] Relatório</title>
    </head>
    <body class="container-fluid">
        <%@include file="/menu.jsp" %>
        <script>
            var botao;
            function OnSubmitForm(form) {
                switch (botao) {
                    case 'Top 20 Usuários':
                        form.action = context + "/relatorio/top20usuarios";
                        break;
                    case 'Top 20 Publicações':
                        form.action = context + "/relatorio/top20publicacoes";
                        break;
                    case 'Top 3 Usuários':
                        form.action = context + "/relatorio/flutuacao";
                        break;
                    case 'Influência':
                        form.action = context + "/relatorio/influencia";
                        break;
                    case 'Top 10 Perfis Similares':
                        form.action = context + "/relatorio/similares";
                        break;
                }
                return true;
            }
        </script>
        <div class="well">
            <div class="row">
                <form onsubmit="return OnSubmitForm(this);" method="POST">
                    <div class="col-md-2">
                        <label>Data Início:</label>
                        <input type="datetime-local" name="data_inicio" class="form-control">
                        <br>
                        <label>Data Fim:</label>
                        <input type="datetime-local" name="data_fim" class="form-control">
                    </div>
                    <div class="col-md-10">
                        <input type="submit" onclick="botao = this.value" value="Top 20 Usuários" class="btn btn-primary">
                        <input type="submit" onclick="botao = this.value" value="Top 3 Usuários" class="btn btn-primary">
                        <input type="submit" onclick="botao = this.value" value="Top 20 Publicações" class="btn btn-primary">
                    </div>
                </form>
            </div>
            <hr>
            <form onsubmit="return OnSubmitForm(this);" method="POST" class="form-group">
                <div class="row">
                    <div class="col-md-5">
                        <select class="form-control" name="usuario">
                            <c:forEach var="u" items="${usuarios}">
                                <option value="${u.id}" <c:if test="${u.id==usuarioLogado.id}">selected</c:if>>${u.nome}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-7">
                        <input type="submit" onclick="botao = this.value" value="Influência" class="btn btn-primary">
                        <input type="submit" onclick="botao = this.value" value="Top 10 Perfis Similares" class="btn btn-primary">
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>
