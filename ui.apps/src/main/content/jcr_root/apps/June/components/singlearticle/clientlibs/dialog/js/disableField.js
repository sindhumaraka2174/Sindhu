(function ($, $document) {
    "use strict";

    $document.on("dialog-ready", function () {

        const checkbox = $(".my-enablepii input[type='checkbox']");
        const textField = $(".my-cssclass input");

        function toggleField() {

            if (checkbox.is(":checked")) {
                // Enable text field
                textField.prop("disabled", false);
            } else {
                // Disable text field
                textField.prop("disabled", true);

                // Clear value from UI
                textField.val("");

                // Delete old value from JCR on save
                $('<input />', {
                    type: 'hidden',
                    name: './cssClass@Delete',
                    value: 'true'
                }).appendTo('.cq-dialog form.foundation-form');
            }
        }

        checkbox.on("change", toggleField);

        toggleField(); // apply state on dialog open
    });

})(jQuery, jQuery(document));
