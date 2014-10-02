<div class="row">
    <div class="col-md-1">
        <img src="${pageContext.servletContext.contextPath}/img/${usuarioLogado.foto}" class="foto-comentario">
    </div>
    <div class="col-md-2">
        <a href="${pageContext.servletContext.contextPath}/usuario/perfil?id=${usuarioLogado.id}">${usuarioLogado.nome}</a>
    </div>
    <div class="col-md-9">
        ${comentario}
    </div>
</div>