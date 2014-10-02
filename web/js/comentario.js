$(document).ready(function() {
    $('.comentar').on('click', function() {
        var post_id = this.id.substring(1);
        var comentario = document.getElementById('c' + post_id).value;
        $.ajax({
            type: "POST",
            url: context + "/post/comentario",
            data: {idPost: post_id, comentario: comentario, idUsuario: idUsuario},
            success: function(html) {
                $('#comentarios-' + post_id).append(html);
            },
            error: function() {
                alert('Erro! Comentário nao adicionado.');
            }
        });
    });
});