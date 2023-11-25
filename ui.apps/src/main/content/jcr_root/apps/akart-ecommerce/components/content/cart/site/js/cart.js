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

const savedItems = JSON.parse(localStorage.getItem('saved-items')) ?? [];
const cart = JSON.parse(localStorage.getItem('cart')) ?? [];
$('#cart-count').text(cart.length);
$(".fa-cart-shopping").attr("data-cart-count", cart.length); // updating cart items

$(document).on('readystatechange', function (e) {
    if (e.target.readyState === 'complete') {
        if ($(".cart-items-wrapper .product").length <= 0) {
            $(".cart-price-wrapper").remove();
            $(".place-order-container").remove();
            // $(".cart-container").css("justify-content", "center");
            $(".cart-items-wrapper").css("width", "100%");
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
            if (response.hasOwnProperty(key) && response[key].isInStock) {
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

$.ajax({
    url: '/bin/akart/cart?cartItems=' + JSON.stringify(savedItems),
    type: 'GET',
    dataType: 'json',
    contentType: 'application/json',
    success: function (response) {
        var source = $("#saved-items-template").html();
        var template = Handlebars.compile(source);
        var html = template(response);
        $("#saved-items").html(html);
    },
    error: function () {

    }
});

$(document).on("click", ".remove-cart-item", function (e) {
    // removing cart item
    var productDiv = $(this).parents(".product");
    productDiv.addClass('product-delete');
    setTimeout(function () {
        productDiv.remove();
        updateOrderSummary();
        var thisProductId = productDiv.find('.product-details .product-id').text();
        var currentCart = JSON.parse(localStorage.getItem('cart'));
        var existingCartItem = currentCart.find(item => item.productId == thisProductId);
        currentCart.splice(currentCart.indexOf(existingCartItem), 1);
        localStorage.setItem('cart', JSON.stringify(currentCart)); // updating cart after removing item

        $(".fa-cart-shopping").attr("data-cart-count", JSON.parse(localStorage.getItem('cart')).length); // updating cart items

        if ($(".cart-items-wrapper .product").length <= 0) {
            $(".cart-price-wrapper").remove();
            $(".place-order-container").remove();
            // $(".cart-container").css("justify-content", "center");
            $(".cart-items-wrapper").css("width", "100%");
            $(".cart-items-wrapper").html(`<div class="empty-cart-wrapper"><img src="/content/dam/akart-ecommerce/images/empty-cart.png" class="empty-cart-image"><div class="empty-cart-message">Ohh no! Your cart is empty!!</div></div>`);
        }
    }, 250);
});

$(document).on("click", ".remove-saved-item", function (e) {
    // removing cart item
    var productDiv = $(this).parents(".product");
    productDiv.addClass('product-delete');
    setTimeout(function () {
        productDiv.remove();
        updateOrderSummary();
        var thisProductId = productDiv.find('.product-details .product-id').text();
        var currentSavedItems = JSON.parse(localStorage.getItem('saved-items'));
        var existingSavedItem = currentSavedItems.find(item => item.productId == thisProductId);
        currentSavedItems.splice(currentSavedItems.indexOf(existingSavedItem), 1);
        localStorage.setItem('saved-items', JSON.stringify(currentSavedItems)); // updating saved-items after removing item
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

// save for later
$(document).on("click", ".save-for-later-item", function () {
    let thisProductId = $(this).parents('.product').find('.product-details .product-id').text();
    let itemQty = $(this).parents('.product').find('.item-count').text();

    let existingSavedItem = savedItems.find(item => item.productId == thisProductId);
    if (existingSavedItem == undefined) {
        savedItems.push({
            productId: thisProductId,
            quantity: itemQty == '' ? 1 : itemQty
        });
    }
    localStorage.setItem('saved-items', JSON.stringify(savedItems));

    var productDiv = $(this).parents(".product");
    productDiv.addClass('product-delete');
    setTimeout(function () {
        productDiv.remove();
        updateOrderSummary();
        var currentCart = JSON.parse(localStorage.getItem('cart'));
        var existingCartItem = currentCart.find(item => item.productId == thisProductId);
        currentCart.splice(currentCart.indexOf(existingCartItem), 1);
        localStorage.setItem('cart', JSON.stringify(currentCart)); // updating cart after removing item

        $(".fa-cart-shopping").attr("data-cart-count", JSON.parse(localStorage.getItem('cart')).length); // updating cart items

        if ($(".cart-items-wrapper .product").length <= 0) {
            $(".cart-price-wrapper").remove();
            $(".place-order-container").remove();
            // $(".cart-container").css("justify-content", "center");
            $(".cart-items-wrapper").css("width", "100%");
            $(".cart-items-wrapper").html(`<div class="empty-cart-wrapper"><img src="/content/dam/akart-ecommerce/images/empty-cart.png" class="empty-cart-image"><div class="empty-cart-message">Ohh no! Your cart is empty!!</div></div>`);
        }

        /*$.ajax({
            url: '/bin/akart/cart?cartItems=' + JSON.stringify(savedItems),
            type: 'GET',
            dataType: 'json',
            contentType: 'application/json',
            success: function (response) {
                var source = $("#saved-items-template").html();
                var template = Handlebars.compile(source);
                var html = template(response);
                $("#saved-items").html(html);
            },
            error: function () {
        
            }
        });*/
    }, 250);
});

$(document).on("click", ".move-to-cart", function () {
    let thisProductId = $(this).parents('.product').find('.product-details .product-id').text();
    let itemQty = $(this).parents('.product').find('.item-count').text();

    let existingCartItem = cart.find(item => item.productId == thisProductId);
    if (existingCartItem == undefined) {
        cart.push({
            productId: thisProductId,
            quantity: itemQty == '' ? 1 : itemQty
        });
    }
    localStorage.setItem('cart', JSON.stringify(cart));

    var productDiv = $(this).parents(".product");
    productDiv.addClass('product-delete');
    setTimeout(function () {
        productDiv.remove();
        updateOrderSummary();
        var savedItems = JSON.parse(localStorage.getItem('saved-items'));
        var existingSavedItem = savedItems.find(item => item.productId == thisProductId);
        savedItems.splice(savedItems.indexOf(existingSavedItem), 1);
        localStorage.setItem('saved-items', JSON.stringify(savedItems)); // updating saved-items after removing item

        $(".fa-cart-shopping").attr("data-cart-count", JSON.parse(localStorage.getItem('cart')).length); // updating cart items
    }, 250);
});