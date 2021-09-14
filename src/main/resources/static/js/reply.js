const replyBtn = $('#createReply');

function createReply(bbsId) {
    replyBtn.on('click', function () {

        if ($('#replyTextarea').val() == '') {
            return false;
        } else {
            $.ajax({
                url: "/reply/ajax/add",
                type: "post",
                data: JSON.stringify({ 'bbsId' : bbsId, 'contents' : $('#replyTextarea').val()}),
                dataType: "json",
                contentType: "application/json;charset=utf-8;",
                success: function (result) {
                    console.log(result.responseCode);
                    if (result.responseCode == 'ok') {
                        location.reload();
                    }
                }
            })
        }
    })
}