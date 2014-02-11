(function () {
    /*
     * @ascandroli:
     * this is a naive implementation that would only work with one (and only one) composition component per page.
     */
    define(["jquery"], function ($) {

        var id;

        return {
            init: function (_id) {
                id = _id;
            },

            reset: function () {
                $(document.getElementById(id))[0].reset();
            }
        };
    });
}).call(this);