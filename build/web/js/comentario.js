$(document).ready(function() {
    $('.comentar').on('click', function() {
        var post_id = this.id.substring(1);
        var comentario = document.getElementById('c' + post_id);
        $.ajax({
            type: "POST",
            url: context + "/post/comentario",
            data: {idPost: post_id, comentario: comentario.value, idUsuario: idUsuario},
            success: function(html) {
                $('#comentarios-' + post_id).append(html);
                comentario.value = '';
            },
            error: function() {
                alert('Erro! Comentário nao adicionado.');
            }
        });
    });
});