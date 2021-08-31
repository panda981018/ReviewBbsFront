function updateViews() {

    $('tr').on('click', function () {
        const tr = $(this);
        const tdList = tr.children();
        const bbsId = tdList.first().text();

        console.log("bbsId : " + bbsId);
        $.ajax({
            type: "POST",
            url: "/post/bbs/update/views",
            dataType: "json",
            data: JSON.stringify({ "id": bbsId}),
            contentType: 'application/json; charset=utf-8',
            error: function () {
                console.log('view update failed.');
            }
        })
    })
}