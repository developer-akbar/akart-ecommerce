// Prepopulate product form with query parameters from the URL
$(document).ready(function () {
    // formatting currency in Indian locale (this can be dynamic based on current locale)
    // $('.currency-inr').each(function () {
    //     $(this).html(Number(parseFloat($(this).html().replace(/,/g, ''))).toLocaleString('en-in'));
    // });

    const cart = localStorage.getItem('cart');
    cart != undefined ? $('#cart-count').text(JSON.parse(cart).length) : 0;


    const queryParams = new URLSearchParams(window.location.search);

    // Loop through the query parameters and prepopulate the form fields
    for (const key of queryParams.keys()) {
        const inputElement = $('FORM').find('[name="' + key + '"]');
        if (key === 'prodName') {
            //$(inputElement).attr('readonly', true)
        }
        if (inputElement) {
            $(inputElement).val(queryParams.get(key));
        }
    }

    if (queryParams.has('action') && queryParams.get('action') === 'edit') {
        $('[name="add-product"]').text('Update Product');
        $('button.cmp-form-button[name="add-product"]').attr('name', 'update-product')
    }
});

