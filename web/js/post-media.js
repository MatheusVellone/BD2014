Dropzone.options.myAwesomeDropzone = {
    acceptedFiles: "image/*,video/*",
    init: function() {
        this.on("addedfile", function(file) {
            var conteudo = document.getElementById('conteudo');
            if ((file.type.match(/image.*/))) {
                var imagem = ' $i:"'+context+'/img/posts/' + file.name + '"';
                conteudo.value += imagem;
            }
            if ((file.type.match(/video.*/))) {
                var video = ' $v:"'+context+'/img/posts/' + file.name + '"';
                conteudo.value += video;
            }
        });
    }
};