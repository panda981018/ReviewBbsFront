function sortBy() {
    $('#sortSelect').on('change', function () {
        let sort = $('#sortSelect option:selected').val().toLowerCase();
        $.ajax({
            method: "GET",
            url: "/admin/manage?sort=" + sort,
            dataType: "json"
        })
            .done(function (result) {
                console.log(result);
            });
    });
}