//alert("Header Validation JS Loaded!");

(function ($, document) {
  "use strict";

  // Run when dialog is ready
  $(document).on("dialog-ready", function () {
          console.log("✅ Header Validation JS Loaded!");


    // Target your multifield container
let multifield = $(".header-multifield");


    // ✅ Stop if not found
    if (multifield.length === 0) {
      return;
    }

    // Validation function
    function validate() {

      // Count items added
      let count = multifield.find("coral-multifield-item").length;

      // Remove old error message
      multifield.next(".custom-error").remove();

      // ✅ Min validation
      if (count < 3) {
        multifield.after(
          `<p class="custom-error" style="color:red;">
            Minimum 3 entries are required.
          </p>`
        );
        return false;
      }

      // ✅ Max validation
      if (count > 5) {
        multifield.after(
          `<p class="custom-error" style="color:red;">
            Maximum 5 entries are allowed.
          </p>`
        );
        return false;
      }

      return true;
    }

    // Validate on add/remove
    multifield.on("coral-collection:add coral-collection:remove", function () {
      validate();
    });

    // Validate on dialog submit
    $("form.cq-dialog").on("submit", function (e) {

      if (!validate()) {
        e.preventDefault(); // Stop saving
      }

    });

    // Initial validation
    validate();
  });

})(Granite.$, document);
