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

$(document).on('readystatechange', function (e) {
    if (e.target.readyState === 'complete') {
        if ($(".cart-items-wrapper .product").length <= 0) {
            $(".cart-price-wrapper").remove();
            $(".cart-items-wrapper").html(`<div class="empty-cart-wrapper"><img src="/content/dam/akart-ecommerce/images/empty-cart.png" class="empty-cart-image"><div class="empty-cart-message">Ohh no! Your cart is empty!!</div></div>`);
        }
    }
});

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

        var totalMrpPrice = 0;
        var totalCart = 0;
        for (var key in response) {
            if (response.hasOwnProperty(key)) {
                totalMrpPrice = totalMrpPrice + (response[key].itemCount * response[key].prodMrpPrice);
                totalCart = totalCart + (response[key].itemCount * response[key].prodPrice);
            }
        }
        $(".cart-price").text(totalMrpPrice);
        $(".discount-price").text(totalMrpPrice - totalCart);
        $(".total-amount").text(totalCart);
        $(".save-message").html(`You will save <span class="currency-inr">${totalMrpPrice - totalCart}</span> on this order`);
    },
    error: function () {

    }
});

$(document).on("click", ".remove-cart-item", function (e) {

    // removing cart item
    var productPagePath = '';
    var productDiv = $(this).parents(".product");
    productDiv.addClass('product-delete');
    setTimeout(function () {
        productDiv.remove();
        updateOrderSummary();
        var thisProductId = productDiv.find('.product-details .product-id').text();
        var currentCart = JSON.parse(localStorage.getItem('cart'));
        var existingCartItem = currentCart.find(item => item.productId == thisProductId);
        currentCart.splice(existingCartItem, 1);
        localStorage.setItem('cart', JSON.stringify(currentCart)); // updating cart after removing item

        $('#cart-count').text(JSON.parse(localStorage.getItem('cart')).length);

        if ($(".cart-items-wrapper .product").length <= 0) {
            $(".cart-price-wrapper").remove();
            $(".cart-items-wrapper").html(`<div class="empty-cart-wrapper"><img src="/content/dam/akart-ecommerce/images/empty-cart.png" class="empty-cart-image"><div class="empty-cart-message">Ohh no! Your cart is empty!!</div></div>`);
        }
    }, 250);


});

function updateOrderSummary() {
    var totalMrpPrice = 0;
    $(".product-mrpprice").each(function () {
        var itemQty = $(this).parents('.product').find('.item-count').text();
        totalMrpPrice = eval(totalMrpPrice + Number.parseInt($(this).text() * itemQty));
    });
    $(".cart-price").text(totalMrpPrice);

    var totalCart = 0;
    $(".items-total-price").each(function () {
        totalCart = eval(totalCart + Number.parseInt($(this).text()));
    });

    var discountPrice = totalMrpPrice - totalCart;
    $(".discount-price").text(discountPrice);
    $(".total-amount").text(totalCart);
    $(".save-message").html(`You will save <span class="currency-inr">${totalMrpPrice - totalCart}</span> on this order`);
}