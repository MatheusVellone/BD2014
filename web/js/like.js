$(document).ready(function() {

    $('.like').on('click', function() {
        var id_usuario = this.getAttribute('id_usuario');
        var id_post = this.getAttribute('id_post');
        var like = $('.bdg-like-' + id_post).text();
        var dislike = $('.bdg-dislike-' + id_post).text();
        $.ajax({
            url: context + '/post/like?id_usuario=' + id_usuario + '&id_post=' + id_post,
            success: function(html) {
                if (html === "1") {
                    //undo like
                    like--;
                } else if (html === "-1") {
                    //undo dislike
                    //like
                    like++;
                    dislike--;
                } else {
                    //like
                    like++;
                }
                $('.bdg-like-' + id_post).text(like);
                $('.bdg-dislike-' + id_post).text(dislike);
            }
        });
    });

    $('.dislike').on('click', function() {
        var id_usuario = this.getAttribute('id_usuario');
        var id_post = this.getAttribute('id_post');
        var like = $('.bdg-like-' + id_post).text();
        var dislike = $('.bdg-dislike-' + id_post).text();
        $.ajax({
            url: context + '/post/dislike?id_usuario=' + id_usuario + '&id_post=' + id_post,
            success: function(html) {
                if (html === "1") {
                    like--;
                    dislike++;
                } else if (html === "-1") {
                    dislike--;
                } else {
                    dislike++;
                }
                $('.bdg-like-' + id_post).text(like);
                $('.bdg-dislike-' + id_post).text(dislike);
            }
        });
    });
});