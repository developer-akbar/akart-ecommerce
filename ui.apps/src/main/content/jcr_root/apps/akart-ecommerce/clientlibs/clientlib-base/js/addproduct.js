var formObject = {};
var SUBMIT_BUTTON = 'button[type="BUTTON"].cmp-form-button';

// on page load
$(document).ready(function() {
    $('form [required]').siblings('label').append(' *');
});

// on click event
$(document).on("click", SUBMIT_BUTTON, function(e) {
    formObject = $(this).closest('form');

    e.preventDefault();
    if($(SUBMIT_BUTTON).attr('name') == 'add-product') {
    	fireSubmission(); // move this inside condition for validation
    } else if($(SUBMIT_BUTTON).attr('name') == 'update-product') {
		$('#modalDialog').show(); // open modal
    }
    if (isValidForm(formObject, e, $(this))) {
        //fireSubmission();
    }
});

// checking form validation in this function
function isValidForm(formObject, e, submitObj) {
    return formObject.valid();
}

/**
 * This function triggers the form submission
 */
function fireSubmission() {
    saveFormData();
}

/**
 * This function is used to submit the form data to end point and
 * redirects to configured thank you page
 */
function saveFormData() {

    // disabling fields to avoid passing their values to payload
    $('[name=":formstart"]').attr("disabled", "true");
    $('[name="_charset_"]').attr("disabled", "true");

    var formData = formObject.closest("form").serializeToJSON({
        parseBooleans: false
    });
    var reqObjJson = JSON.stringify(formData);

    $.ajax({
        url: '/bin/akart/products.addproduct.json',
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        data: reqObjJson,
        success: function(response) {
            if (response.status == "201") {
                console.log('The product page has been created');
                var redirectPage = formObject.find("[name=':redirect']").val();
                if (redirectPage !== undefined) {
                    window.location = redirectPage + '?productId=' + response.productId;
                }
            } else if (response.status == "200") {
                console.log('The product page is already available');
                //$('#modalDialog').show(); // open modal

            } else {
                console.warn("Failed to save form data");
                //formObject.find(FIELDS.FORM_SERVER_ERROR).show(); // show server side error message
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.warn("Failed to save form data");
            var errorMsg = jqXHR.responseJSON.errormessage + ', ' + jqXHR.responseJSON.message;
            formObject.find('.cmp-form-button__msg-formservererror').append(errorMsg);
        }
    });
}

$(document).on("click", ".modal-primay-button", function(e) {
    var formData = formObject.closest("form").serializeToJSON({
        parseBooleans: false
    });

    var queryParams = new URLSearchParams(window.location.search);
    if(queryParams.has('path')) {
        formData.productPagePath = queryParams.get('path');
    }
    var reqObjJson = JSON.stringify(formData);

	$.ajax({
        url: '/bin/akart/products.updateproduct.json',
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        data: reqObjJson,
        success: function(response) {
            if (response.status == "200") {
                console.log('The product page is already available');
                $('#modalDialog').fadeOut(); // close modal
                var redirectPage = formObject.find("[name=':redirect']").val();
                if (redirectPage !== undefined) {
                    window.location = redirectPage + '?productId=' + response.productId;
                }                
            } else {
                console.warn("Failed to save form data");
                //formObject.find(FIELDS.FORM_SERVER_ERROR).show(); // show server side error message
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.warn("Failed to save form data");
            var errorMsg = jqXHR.responseJSON.errormessage + ', ' + jqXHR.responseJSON.message;
            formObject.find('.cmp-form-button__msg-formservererror').append(errorMsg);
        }
    });
});
$(document).on("click", ".delete-product-button", function(e) {

    //$('#modalDialog').show(); // open modal

    var productPagePath = '';
	var productDiv = $(this).parents(".product");
    var productLink = productDiv.find(".product-link");
    if(productLink) {
        if(productLink.attr("href")) {
			productPagePath = productLink.attr("href").split(".html")[0];
        }
    }

    if(productPagePath) {
		$.ajax({
            url: '/bin/akart/products.deleteproduct.json?productPagePath=' + productPagePath,
            type: 'delete',
            success: function(response) {
                if (response != undefined) {
    				console.log('Product page deleted');
					productDiv.addClass('product-delete');
                } else {
                    console.warn("Failed to delete product page");
    
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.warn("Failed to delete product page");
            }
        });
    }

});

$(document).on("click", ".close-modal", function(e) {
	$('#modalDialog').fadeOut();
});