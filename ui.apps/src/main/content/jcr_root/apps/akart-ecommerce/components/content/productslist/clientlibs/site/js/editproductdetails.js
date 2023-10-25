$(document).on("click", ".edit-product-button", function(e) {
    var productPagePath = '';

    var productLink = $(this).parents(".product").find(".product-link")
    if(productLink) {
        if(productLink.attr("href")) {
			productPagePath = productLink.attr("href").split(".html")[0];
        }
    }

    if(productPagePath) {
		$.ajax({
            url: '/bin/akart/getproductdetails.json?productPagePath=' + productPagePath,
            type: 'get',
            success: function(response) {
                if (response != undefined) {
    
                    // creating query parameters
                    var queryParams = Object.keys(response).map(key =>
                        `${encodeURIComponent(key)}=${encodeURIComponent(response[key])}`
                    ).join("&");
    
                    window.location = '/content/akart-ecommerce/en-us/misc/add-product-details-form.html?'+queryParams+'&action=edit'+'&path='+productPagePath;
                } else {
                    console.warn("Failed to fetch product data");
    
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.warn("Failed to fetch product data");
            }
        });
    }
});