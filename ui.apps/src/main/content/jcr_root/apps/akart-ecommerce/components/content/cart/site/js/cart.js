Handlebars.registerHelper('ifvalue', function (conditional, options) {
    if (options.hash.value === conditional) {
        return options.fn(this)
    } else {
        return options.inverse(this);
    }
});

Handlebars.registerHelper('multiply', function (val1, val2) {
    return val1 * val2;
});

const cart = JSON.parse(localStorage.getItem('cart'));
$('#cart-count').text(cart.length);

$.ajax({
    url: '/bin/akart/cart?cartItems=' + JSON.stringify(cart),
    type: 'GET',
    dataType: 'json',
    contentType: 'application/json',
    success: function (response) {
        var source = $("#cart-template").html();
        var template = Handlebars.compile(source);
        var html = template(response);
        $("#cart-items").html(html);

        var totalCart = 0;
        $(".items-total-price").each(function (itemTotalPrice) {
            totalCart = eval(totalCart + Number.parseInt($(this).text()));
        });        
        $(".cart-price").text(totalCart);
        $(".total-amount").text(totalCart - $(".discount-price").text());
    },
    error: function () {

    }
})
