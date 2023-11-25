document.addEventListener('DOMContentLoaded', function () {
    // updating cart count
    const cart = JSON.parse(localStorage.getItem('cart')) ?? [];
    document.querySelector(".fa-cart-shopping").setAttribute("data-cart-count", cart.length);

    const header = document.querySelector(".primary-header");
    const headerHeight = header.getBoundingClientRect().height;
    document.body.style.paddingTop = headerHeight + 'px';
});

const primaryNav = document.querySelector('.primary-navigation');
const navToggle = document.querySelector('.mobile-nav-toggle');

navToggle.addEventListener('click', function () {
    primaryNav.classList.toggle('active');

    document.body.style.overflow = 'hidden';

    if (primaryNav.classList.contains('active')) {
        document.body.style.overflow = 'hidden';
        navToggle.setAttribute("aria-expanded", "true");
        document.querySelector('.fa-bars').style.display = 'none';
        document.querySelector('.fa-xmark').style.display = 'block';
    } else {
        document.body.style.overflow = '';
        navToggle.setAttribute("aria-expanded", "false");
        document.querySelector('.fa-bars').style.display = 'block'
        document.querySelector('.fa-xmark').style.display = 'none'
    }
});

let letScrollTop = 0;

window.addEventListener('scroll', function() {
    const scrollTop = window.scrollY || this.document.documentElement.scrollTop;

    const primaryHeader = document.querySelector('.primary-header');

    if(scrollTop > letScrollTop) {
        document.body.classList.add('header-hidden');
    } else {
        document.body.classList.remove('header-hidden');
    }

    letScrollTop = scrollTop;
});