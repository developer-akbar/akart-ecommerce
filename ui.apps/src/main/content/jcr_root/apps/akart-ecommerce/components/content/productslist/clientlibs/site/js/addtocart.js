const cart = JSON.parse(localStorage.getItem('cart'));
let savedCartCount = cart != undefined ? cart.length : 0;
$('#cart-count').text(savedCartCount);

//const cart = [];

$(document).on("click", ".add-to-cart-button", function () {
    let thisProductId = $(this).parents('.action-buttons').siblings('.product-info').find('.product-details p.product-id').text();

    let existingCartItem = cart.find(item => item.productId == thisProductId);
    if (existingCartItem) {
        // Toggle between showing the count and "Add to Cart"        
        if (existingCartItem.quantity > 0) {
            existingCartItem.quantity++;
        } else {
            //existingCartItem.quantity = 0;
            removeItem(thisProductId);
        }
    } else {
        cart.push({
            productId: thisProductId,
            quantity: 1
        });
    }
    existingCartItem = cart.find(item => item.productId == thisProductId);
    updateCart($(this), existingCartItem);
});

function removeItem(productId) {
    const itemIndex = cart.findIndex(item => item.productId == productId);

    if (itemIndex !== -1) {
        cart.splice(itemIndex, 1);
    }
}

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
        thisButton.html(`<button class="cart-button" onclick="decrementCart(this, event)">-</button><span>${item.quantity}</span><button class="cart-button" onclick="incrementCart(this, event)">+</button>`);
        // $(thisButton).css('display', 'none');
        // $(thisButton).siblings(".cart-decr-incr-buttons").css('display', 'block');
        // $(thisButton).siblings(".cart-decr-incr-buttons").find("span.item-count").text(item.quantity)
    } else {
        thisButton.text(`Add to Cart`);
    }

    if (cart.length == 0) {
        thisButton.text(`Add to Cart`);
    }
    console.log('cart ', cart);
    localStorage.setItem('cart', JSON.stringify(cart));
    $('#cart-count').text(cart.length); // updating cart items
}