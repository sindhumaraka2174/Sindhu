function deleteUser(nodeName) {

    if (!nodeName) {
        alert("Node name missing");
        return;
    }

    if (confirm("Are you sure you want to delete this record?")) {

        $.ajax({
            url: "/bin/deleteuser",   // MUST match servlet path
            type: "GET",             // POST is correct for delete
            data: {
                node: nodeName    // MUST match servlet parameter
            },
            success: function (response) {
                alert(response);
                window.location.reload();
            },
            error: function () {
                alert("Delete failed");
            }
        });
    }
}
