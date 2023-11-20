const primaryNav = document.querySelector('.primary-navigation');
const navToggle = document.querySelector('.mobile-nav-toggle');

navToggle.addEventListener('click', function () {
    primaryNav.classList.toggle('active');

    document.body.style.overflow = 'hidden';

    if (primaryNav.classList.contains('active')) {
        document.body.style.overflow = 'hidden';
        navToggle.setAttribute("aria-expanded", "true");
        document.querySelector('.fa-bars').style.display = 'none'
        document.querySelector('.fa-xmark').style.display = 'block'
    } else {
        document.body.style.overflow = '';
        navToggle.setAttribute("aria-expanded", "false");
        document.querySelector('.fa-bars').style.display = 'block'
        document.querySelector('.fa-xmark').style.display = 'none'
    }
});