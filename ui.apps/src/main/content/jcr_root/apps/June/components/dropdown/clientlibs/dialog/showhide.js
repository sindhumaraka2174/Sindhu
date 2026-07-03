(function (document, $) {
    "use strict";

    function toggleFields(el) {
        var value = el.val();
        var target = el.data("cq-dialog-dropdown-showhide-target");

        if (!value || !target) return;

        var $dialog = el.closest(".coral-Dialog");

        $dialog.find(target).hide();
        $dialog.find(target + "." + value).show();
    }

    // On dialog load
    $(document).on("dialog-ready", function () {
        $(".cq-dialog-dropdown-showhide").each(function () {
            toggleFields($(this));
        });
    });

    // On dropdown change
    $(document).on("change", ".cq-dialog-dropdown-showhide", function () {
        toggleFields($(this));
    });

})(document, Granite.$);