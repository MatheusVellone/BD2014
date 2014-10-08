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

                if (botao == 'Top 20 Usuários') {
                    form.action = context + "/relatorio/top20usuarios";
                }
                else if (botao == 'Top 20 publicações') {
                    form.action = context + "/relatorio/top20publicacoes";
                }
                else if (botao == 'Top 3 Usuários') {
                    form.action = context + "/relatorio/flutuacao";
                }
                return true;
            }
        </script>
        <div class="well">
            <div class="row">
                <form onsubmit="return OnSubmitForm(this);" method="POST">
                    <div class="col-md-2">
                        <label>Data Início:</label>
                        <input type="date" name="data_inicio" class="form-control">
                        <br>
                        <label>Data Fim:</label>
                        <input type="date" name="data_fim" class="form-control">
                    </div>
                    <div class="col-md-10">
                        <input type="submit" onclick="botao = this.value" value="Top 20 Usuários" class="btn btn-primary">
                        <input type="submit" onclick="botao = this.value" value="Top 3 Usuários" class="btn btn-primary">
                        <input type="submit" onclick="botao = this.value" value="Top 20 Publicações" class="btn btn-primary">
                    </div>
                </form>
            </div>
            <hr>
            <div class="row">
                Tamanho da zona de influência de um usuário fornecido por parâmetro, considerando-se o número de seguidores em 2 níveis (seguidores diretos e seguidores de seguidores); repetições devem ser descartadas, isto é, um seguidor de segundo nível (seguidor de seguidor) não deve ser contado novamente se também é seguidor de primeiro nível (direto).
            </div>
            <hr>
            <form action="${pageContext.servletContext.contextPath}/relatorio/similares" method="POST" class="form-group">
                <div class="row">
                    <div class="col-md-5">
                        <select class="form-control" name="usuario">
                            <c:forEach var="u" items="${usuarios}">
                                <option value="${u.id}" <c:if test="${u.id==usuarioLogado.id}">selected</c:if>>${u.nome}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-7">
                        <input type="submit" value="Top 10 perfis similares" class="btn btn-primary">
                    </div>
                    - Top 10 usuários com perfis mais similares a um usuário: selecionando-se um usuário, identificar outros usuários que tenham perfil semelhante a ele. Isto é, usuários que tenham republicado ou comentado as mesmas publicações. A lista deve ser ordenada descrescentemente pelo grau de similaridade entre o usuário de interesse (u1) e outro usuário (u2), que é dado por:

                    similaridade(u1, u2) = ( (num_replicações_u1_e_u2)/(num_replicações_u1) + (num_comentários_u1_e_u2)/(num_comentários_u1) ) / 2

                    , onde num_replicações_u1_e_u2 é o número de publicações que tanto u1 quanto u2 republicaram e num_replicações_u1 é o número de publicações que u1 republicou (o mesmo vale para comentários).    
                </div>
            </form>
        </div>
    </body>
</html>
