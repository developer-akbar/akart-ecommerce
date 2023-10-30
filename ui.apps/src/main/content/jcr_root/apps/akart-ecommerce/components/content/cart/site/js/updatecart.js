function incrementCart(thisButton, event) {
    event.stopPropagation();
    let thisProductId = $(thisButton).parents('.action-buttons').siblings('.product-info').find('.product-details .product-id').text();
    const existingCartItem = cart.find(item => item.productId == thisProductId);

    if (existingCartItem) {
        existingCartItem.quantity++;
    } else {
        cart.push({
            productId: thisProductId,
            quantity: 1
        });
    }

    updateCart($(thisButton).parent(), existingCartItem);
}

function decrementCart(thisButton, event) {
    event.stopPropagation();
    let thisProductId = $(thisButton).parents('.action-buttons').siblings('.product-info').find('.product-details .product-id').text();
    const existingCartItem = cart.find(item => item.productId == thisProductId);

    if (existingCartItem && existingCartItem.quantity > 0) {
        existingCartItem.quantity--;
        if (existingCartItem.quantity === 0) {
            removeItem(thisProductId);
        }
    }

    updateCart($(thisButton).parent(), existingCartItem);
}

function updateCart(thisButton, item) {
    // Update the cart buttons
    if (item && item.quantity > 0) {
        thisButton.html(`<button class="minus-button" onclick="decrementCart(this, event)" ${item.quantity === 1 ? 'disabled' : ''}>-</button><span class="item-count" style="padding: 10px;">${item.quantity}</span><button class="plus-button" onclick="incrementCart(this, event)" ${item.quantity === 10 ? 'disabled' : ''}>+</button>`);
        thisButton.parent().siblings('.items-price').find('.items-total-price').text(item.quantity * thisButton.parent().siblings('.items-price').find('.item-price').text())
    } else {
        thisButton.text(`Add to Cart`);
    }

    if (cart.length == 0) {
        thisButton.text(`Add to Cart`);
    }

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

    console.log('cart ', cart);
    localStorage.setItem('cart', JSON.stringify(cart));
    $('#cart-count').text(cart.length); // updating cart items
}