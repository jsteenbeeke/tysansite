(function ($) {
    $.fn.revealable = function () {
        return this.each(function (i, target) {
            $(target).focus(function () {
                $(this).attr('type', 'text');
            });
            $(target).blur(function () {
                $(this).attr('type', 'password');
            });
        });
    };
}(jQuery));
