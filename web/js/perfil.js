$(document).ready(function() {
    $('.mostra_segue').on('click', function(){
        $('#info_'+this.id).toggle('fast');
    });
});