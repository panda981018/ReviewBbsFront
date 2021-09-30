const replyBtn = $('#createReply');
const textEle = $('#replyTextarea');

function createReply(bbsId) {
    replyBtn.on('click', function () {

        if (textEle.val().trim() == '') {
            alert('내용을 입력해주세요 :)');
            return false;
        } else {
            $.ajax({
                url: "/reply/ajax/add",
                type: "post",
                data: JSON.stringify({'bbsId': bbsId, 'contents': textEle.val().trim()}),
                dataType: "json",
                contentType: "application/json;charset=utf-8;",
                success: function (result) {
                    console.log(result.responseCode);
                    if (result.responseCode === 'ok') {
                        location.reload();
                    }
                }
            })
        }
    })
}

function adjustHeight() {
    $('textarea').each(function (index, element) {
        $(this).css('height', 'auto');
        const textEleHeight = $(this).prop('scrollHeight');
        $(this).css('height', textEleHeight);
    })
}

function autoResizing() { // 컨텐츠 길이에 따라 높이가 늘어나게
    $('textarea').each(function () {
        $(this).on('keyup', function (index, element) {
            adjustHeight();
        })
    })
}

function deleteReply(bbsId) { // 삭제 버튼 눌렀을 때
    $('#writtenReply').on('click', "button[id='deleteReplyBtn']", function () {

        const replyId = $(this).parent().siblings('input').val();

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
            $(this).focused = false;
        }
    })
}

function showReplyModal() { // 댓글의 수정버튼 눌렀을 때의 이벤트 리스너
    $(document).on('click', '#updateReplyBtn', function () {
        const replyId = $(this).parent().siblings('input').val();
        const replyContent = $(this).parents().siblings('span[id=replyContents]')[0]; // 원래 내용이 포함된 span
        const oldText = replyContent.innerHTML.trim();

        const modalTextarea = $('#updateReplyTextarea')[0];
        const modalReplyId = $('#hiddenReplyId');
        modalReplyId.val(replyId);
        modalTextarea.innerHTML = oldText;

        $('#updateReplyDialog').modal('show');
    })
}

function modalListener() { // modal 수정 클릭 리스너
    const modalSaveBtn = $('#modalUpdateBtn').eq(0);
    const modalReplyId = $('#hiddenReplyId').eq(0);
    modalSaveBtn.on('click', function () {
        const updateReply = $('#updateReplyTextarea')[0].value.trim();
        const replyId = modalReplyId.val();
        $.ajax({
            url: '/reply/ajax/update',
            type: "post",
            data: JSON.stringify({ "id" : replyId, "contents" : updateReply }),
            contentType: "application/json;charset=utf-8;",
            success: function () {
                location.reload();
            }
        })
    })
}