$(document).ready(function () {
    $('.category-section .category-item').each(function () {
        const categoryContent = $(this).find('.category-item-content');
        const prevButton = $(this).find('.prev-button');
        const nextButton = $(this).find('.next-button');
        const featuredItem = categoryContent.find('.featured-item');
        const featuredItemWidth = featuredItem.outerWidth(); // Get the width of one featured item
        let currentScrollPosition = 0;
        const scrollStep = featuredItemWidth; // Scroll one item width per click

        // Calculate the number of visible items at once
        const visibleItems = Math.floor(categoryContent.width() / featuredItemWidth);

        // Function to hide/show buttons based on scroll position
        function updateButtonVisibility() {
            if (currentScrollPosition <= 0) {
                prevButton.hide();
            } else {
                prevButton.show();
            }

            if (currentScrollPosition + categoryContent.width() >= featuredItem.length * featuredItemWidth) {
                nextButton.hide();
            } else {
                nextButton.show();
            }
        }

        // Initial button visibility update
        updateButtonVisibility();

        nextButton.click(function () {
            if (currentScrollPosition + categoryContent.width() < featuredItem.length * featuredItemWidth) {
                currentScrollPosition += scrollStep;
                categoryContent.animate({ scrollLeft: currentScrollPosition }, 'fast');
                updateButtonVisibility();
            }
        });

        prevButton.click(function () {
            if (currentScrollPosition > 0) {
                currentScrollPosition -= scrollStep;
                categoryContent.animate({ scrollLeft: currentScrollPosition }, 'fast');
                updateButtonVisibility();
            }
        });
    });
});