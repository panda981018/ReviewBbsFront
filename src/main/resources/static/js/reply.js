const replyBtn = $('#createReply');
const textEle = $('#replyTextarea');
const updateBtn = $('#updateReplyBtn');

function createReply(bbsId) {
    replyBtn.on('click', function () {

        if (textEle.val() == '') {
            return false;
        } else {
            $.ajax({
                url: "/reply/ajax/add",
                type: "post",
                data: JSON.stringify({'bbsId': bbsId, 'contents': textEle.val()}),
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

function adjustHeight() {
    textEle.eq(0).css('height', 'auto');

    const textEleHeight = textEle.prop('scrollHeight');
    textEle.css('height', textEleHeight);
}

function autoResizing() { // 컨텐츠 길이에 따라 높이가 늘어나게
    textEle.on('keyup', function () {
        adjustHeight();
    })
}

function deleteReply(bbsId) {
    $('#writtenReply').on('click', "button[id='deleteReplyBtn']", function (event) {

        console.log($(this).parent().siblings('input').val());
        const replyId = $(this).siblings('input').val();

        let result = confirm('댓글을 삭제하시겠습니까?');
        if (result) {
            $.ajax({
                url: '/reply/ajax/delete',
                type: 'DELETE',
                data: JSON.stringify({'bbsId': bbsId, 'replyId': replyId}),
                contentType: 'application/json;charset=utf-8;',
                success: function () {
                    location.reload();
                }
            })
        } else {
            return false;
        }
    })
}

function updateReply() {
    $('#writtenReply').on('click', "button[id='updateReplyBtn']", function (event) {
        const replyId = $(this).siblings('input').val();
        const replyContent = $(this).parents().siblings('span[id=replyContents]')[0]; // 원래 내용이 포함된 span
        const replyInfo = $(this).parents("div[id=replyInfo]").eq(0); // 댓글 정보를 가진 div
        const oldText = replyContent.innerHTML;

        replyContent.remove();

        const editableElement = $("<textarea id='updateReplyContents'></textarea>")[0];
        const submitBtn = $("<button type='button' id='updateReplySubmit' class='btn btn-outline-secondary'>확인</button>")[0];
        // replyContent.replaceWith(editableElement);
        editableElement.innerHTML = oldText;
        replyInfo.after(editableElement);
        editableElement.after(submitBtn);
        let data = { 'replyId' : replyId };

        submitBtn.onclick = function () {
            //const replyId = $('#updateReplySubmit').siblings('div[id=replyInfo]').children("input")[0].val();
            //console.log(replyId);
            let text = $('#updateReplyContents')[0].innerHTML;
            console.log($('#updateReplyContents')[0].innerText);
            data.contents = $('#updateReplyContents')[0].innerText;
            // $.ajax({
            //     url: "/reply/ajax/update",
            //     type: "post",
            //     data: JSON.stringify(data),
            //     contentType: "application/json;charset=utf-8;",
            //     success: function () {
            //         location.reload();
            //     }
            // })
        }
    })
}

function submitUpdateReply(replyId) {
    $('#updateReplySubmit').on('click', function () {
        let data = {'contents': $('#updateReplyContents')[0].innerHTML, 'replyId': replyId} ;
        $.ajax({
            url: "/reply/ajax/update",
            type: "post",
            data: JSON.stringify(data),
            contentType: "application/json;charset=utf-8;",
            success: function () {
                location.reload();
            }
        })
    })
}