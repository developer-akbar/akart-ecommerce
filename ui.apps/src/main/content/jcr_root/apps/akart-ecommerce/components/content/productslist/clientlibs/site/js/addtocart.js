//const cart = JSON.parse(localStorage.getItem('cart'));
const cart = [];
let savedCartCount = cart != undefined ? cart.length : 0;
$('#cart-count').text(savedCartCount);

$(document).on('readystatechange',function(e){
    if(e.target.readyState === 'complete'){
        $(".product-list .product").find(".product-id").each(function (params) {
            var thisProductId = $(this).text();
            console.log('product id: ', thisProductId);
            var existingCartItem = cart.find(item => item.productId == thisProductId);
            console.log('cart item: ', existingCartItem);
            updateCart1($(this), existingCartItem);
        });
    }
});

function updateCart1(thisButton, item) {
    console.log('updating for ', item);
    // Update the cart buttons
    if (item && item.quantity > 0) {
        thisButton.parents('.product').find('.edit-product').addClass('add-to-cart-button');
        thisButton.parents('.product').find('.edit-product').html(`<button class="minus-button" onclick="decrementCart(this, event)">-</button><span class="item-count" style="padding: 10px;">${item.quantity}</span><button class="plus-button" onclick="incrementCart(this, event)" ${item.quantity === 10 ? 'disabled' : ''}>+</button>`);
        // thisButton.parents('.product').find('.add-to-cart-button').html(`<button class="minus-button" onclick="decrementCart(this, event)">-</button><span class="item-count" style="padding: 10px;">${item.quantity}</span><button class="plus-button" onclick="incrementCart(this, event)">+</button>`);
    } else {
        thisButton.parents('.product').find('.edit-product').html(`<button class="add-to-cart-button button">Add to Cart</button>`);
    }

    if (cart.length == 0) {
        thisButton.parents('.product').find('.edit-product').html(`<button class="add-to-cart-button button">Add to Cart</button>`);
    }
}

//const cart = [];

$(document).on("click", ".add-to-cart-button", function () {
    let thisProductId = $(this).parents('.action-buttons').siblings('.product-info').find('.product-details .product-id').text();

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
    if (item && item.quantity > 1) {
        thisButton.html(`<button class="minus-button" onclick="decrementCart(this, event)">-</button><span class="item-count" style="padding: 10px;">${item.quantity}</span><button class="plus-button" onclick="incrementCart(this, event)" ${item.quantity === 10 ? 'disabled' : ''}>+</button>`);
    } else if (item && item.quantity == 1) {
        thisButton.parents('.edit-product').addClass('add-to-cart-button');
        thisButton.parents('.edit-product').html(`<button class="minus-button" onclick="decrementCart(this, event)">-</button><span class="item-count" style="padding: 10px;">${item.quantity}</span><button class="plus-button" onclick="incrementCart(this, event)" ${item.quantity === 10 ? 'disabled' : ''}>+</button>`);

        if(thisButton.hasClass('add-to-cart-button')) {
            thisButton.html(`<button class="minus-button" onclick="decrementCart(this, event)">-</button><span class="item-count" style="padding: 10px;">${item.quantity}</span><button class="plus-button" onclick="incrementCart(this, event)" ${item.quantity === 10 ? 'disabled' : ''}>+</button>`);
        }
    } else {
        thisButton.removeClass('add-to-cart-button');
        thisButton.html(`<button class="add-to-cart-button button">Add to Cart</button>`);
    }

    if (cart.length == 0) {
        thisButton.text(`<button class="add-to-cart-button button">Add to Cart</button>`);
    }
    console.log('cart ', cart);
    localStorage.setItem('cart', JSON.stringify(cart));
    $('#cart-count').text(cart.length); // updating cart items
}