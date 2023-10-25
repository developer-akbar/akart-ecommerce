// Get the modal
var modal = $('#modalDialog');

// Get the button that opens the modal
var btn = $(".modal-popup");

// Get the <span> element that closes the modal
var span = $(".close");

$(document).on("click", ".modal-popup", function(e) {
	$('#modalDialog').show();
});

$(document).on("click", ".close-modal", function(e) {
	$('#modalDialog').fadeOut();
});

$(document).ready(function(){
    // When the user clicks the button, open the modal 
    btn.on('click', function() {
        modal.show();
    });
    
    // When the user clicks on <span> (x), close the modal
    span.on('click', function() {
        modal.fadeOut();
    });
});

// When the user clicks anywhere outside of the modal, close it
$('body').bind('click', function(e){
    if($(e.target).hasClass("modal")){
        modal.fadeOut();
    }
});