<script>
    var context = '${pageContext.servletContext.contextPath}';
    var idUsuario = ${usuarioLogado.id};
</script>
<style>
    body{
        background: url(${pageContext.servletContext.contextPath}/img/background/<%= (int) (Math.random() * 10)%>.jpg)no-repeat center center fixed;
    }
</style>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/jquery-2.1.0.min.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/jquery.highlight.js"></script>

<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/css/bootstrap-theme.min.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/css/custom.css" />

<div class="navbar-inverse">
    <div class="navbar-header">
        <a href="${pageContext.servletContext.contextPath}/" class="navbar-brand"><span class="glyphicon glyphicon-home"></span></a>
    </div>
    <div class="navbar-collapse collapse">
        <div class="nav navbar-nav">
            <div class="btn-group">
                <button class="btn btn-default dropdown-toggle navbar-btn" type="button" id="dropdownMenu1" data-toggle="dropdown">
                    Usuários
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                    <li role="presentation"><a href="${pageContext.servletContext.contextPath}/usuario">Lista dos Usuários Cadastrados</a></li>
                    <li role="presentation"><a href="${pageContext.servletContext.contextPath}/usuario/update?id=${usuarioLogado.id}">Atualizar Cadastro</a></li>
                </ul>
            </div>

            <div class="btn-group">
                <button class="btn btn-default dropdown-toggle navbar-btn" type="button" id="dropdownMenu1" data-toggle="dropdown">
                    Grupos
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                    <li role="presentation"><a href="${pageContext.servletContext.contextPath}/grupo/create">Criar Novo Grupo</a></li>
                    <li role="presentation"><a href="${pageContext.servletContext.contextPath}/grupo">Ver Grupos Existentes</a></li>
                    <li role="presentation"><a href="${pageContext.servletContext.contextPath}/grupo/gerencia?id=${usuarioLogado.id}">Grupos Que Eu Gerencio</a></li>
                    <li role="presentation"><a href="${pageContext.servletContext.contextPath}/grupo/participa?id=${usuarioLogado.id}" >Grupos Que Eu Participo</a></li>
                </ul>
            </div>

            <div class="btn-group">
                <button class="btn btn-default dropdown-toggle navbar-btn" type="button" id="dropdownMenu1" data-toggle="dropdown">
                    Post
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                    <li role="presentation"><a href="${pageContext.servletContext.contextPath}/post/create">Nova postagem</a></li>
                    <li role="presentation"><a href="${pageContext.servletContext.contextPath}/post/meus">Meus Posts</a></li>
                </ul>
            </div>

            <div class="btn-group">
                <a href="${pageContext.servletContext.contextPath}/relatorio" class="btn btn-info navbar-btn"><span class="glyphicon glyphicon-th-list"></span> Relatórios</a>
            </div>

            <div class="btn-group">
                <a href="${pageContext.servletContext.contextPath}/logout" class="btn btn-danger navbar-btn"><span class="glyphicon glyphicon-log-out"></span> Logout</a>
            </div>

            <div class="btn-group">
                <a href="${pageContext.servletContext.contextPath}/usuario/delete?id=${usuarioLogado.id}" class="navbar-btn btn btn-danger">Excluir Cadastro</a>
            </div>
            <div class="btn-group">
                <form action="${pageContext.servletContext.contextPath}/busca" method="POST" class="form-inline">

                    <input type="text" name="termo" placeholder="Pesquise por usuários/posts/grupos" class="form-control">

                    <button type="submit" class="btn btn-default navbar-btn">
                        <span class="glyphicon glyphicon-search"></span> Pesquisar
                    </button>
                </form>
            </div>

            <div class="btn-group">
            </div>

            <div class="btn-group">
                Bem-vindo 
                <a href="${pageContext.servletContext.contextPath}/usuario/perfil?id=${usuarioLogado.id}">
                    <c:out value="${usuarioLogado.nome}"/>
                    <img src="${pageContext.servletContext.contextPath}/img/${usuarioLogado.foto}" class="img-circle" width="50">
                </a>
            </div>
        </div>
    </div>
</div>