document.addEventListener('DOMContentLoaded', function () {
    const header = document.querySelector(".header");
    const headerHeight = header.clientHeight;
    document.body.style.paddingTop = headerHeight + 'px';

    const navToggle = document.getElementById('nav-toggle');
    const navMenu = document.querySelector('.nav-menu');
    const overlay = document.querySelector('.overlay');

    navToggle.addEventListener('click', function () {
        navMenu.classList.toggle('active');
        document.body.style.overflow = 'hidden';
        overlay.style.display = navMenu.classList.contains('active') ? 'block' : 'none';

        if (navMenu.classList.contains('active')) {
            document.body.style.overflow = 'hidden';
        } else {
            document.body.style.overflow = '';
        }
    });

    overlay.addEventListener('click', function () {
        navMenu.classList.remove('active');
        document.body.style.overflow = '';
        overlay.style.display = 'none';
    })
});