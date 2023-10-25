function incrementCart(thisButton, event) {
    event.stopPropagation();
    let thisProductId = $(thisButton).parents('.action-buttons').siblings('.product-info').find('.product-details p.product-id').text();
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
    let thisProductId = $(thisButton).parents('.action-buttons').siblings('.product-info').find('.product-details p.product-id').text();
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
        thisButton.html(`<button class="cart-button" onclick="decrementCart(this, event)" ${item.quantity === 1 ? 'disabled' : ''}>-</button><span>${item.quantity}</span><button class="cart-button" onclick="incrementCart(this, event)" ${item.quantity === 10 ? 'disabled' : ''}>+</button>`);
        thisButton.parent().siblings('.items-total-price').text(item.quantity * thisButton.parent().siblings('.item-price').text())
    } else {
        thisButton.text(`Add to Cart`);
    }

    if (cart.length == 0) {
        thisButton.text(`Add to Cart`);
    }    

    var totalCart = 0;
    $(".items-total-price").each(function (itemTotalPrice) {
        totalCart = eval(totalCart + Number.parseInt($(this).text()));   
    });
    $(".cart-price").text(totalCart);
    $(".total-amount").text(totalCart - $(".discount-price").text());

    console.log('cart ', cart);
    localStorage.setItem('cart', JSON.stringify(cart));
    $('#cart-count').text(cart.length); // updating cart items
}