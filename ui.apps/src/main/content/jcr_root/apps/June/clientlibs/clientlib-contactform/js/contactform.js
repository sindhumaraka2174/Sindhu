function submitForm() {
    var name = document.getElementById("name").value;
    var email = document.getElementById("email").value;
    var mobile = document.getElementById("mobile").value;

    // hidden field (exists only during edit)
    var nodeNameField = document.getElementById("nodeName");
    var nodeName = nodeNameField ? nodeNameField.value : "";

    $.ajax({
        url: "/bin/contactform",
        type: "GET",
        data: {
            name: name,
            email: email,
            mobile: mobile,
            nodeName: nodeName   // ⭐ THIS LINE IS CRITICAL
        },
        success: function (response) {
            alert(response);
            window.location.href = "/content/June/june-test-page.html";
        },
        error: function () {
            alert("Error while saving data");
        }
    });
}
