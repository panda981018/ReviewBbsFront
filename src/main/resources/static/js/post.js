function updateViews() { // 조회수 업데이트

    $('tr').on('click', function () {
        const tr = $(this).children();
        const bbsId = tr.first().text();

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
// 보고 있는 카테고리에서 작성을 누르면 write로 넘어갔을 때 그 카테고리로 세팅되도록 설정하는 함수
function selectCategory(categoryId) {
    $('#bbsCategory').val(categoryId);
}

function titleValidate() { // 타이틀 비어있으면 error
    const bbsTitle = $('#bbsTitle');
    const titleError = $('#titleError');

    bbsTitle.on('focusout', function () {
        if (bbsTitle.val() == '') {
            invalidMsg(titleError);
            bbsTitle.addClass('is-invalid');
        } else {
            validMsg(titleError);
            bbsTitle.removeClass('is-invalid');
            bbsTitle.addClass('is-valid');
        }
    })
}

function contentValidate() { // 내용이 비어있으면 error
    const editor = $('.note-editable');
    const bbsError = $('#bbsError');

    editor.on('focusout', function () {
        console.log($('#bbsContents').summernote('isEmpty'));
        if ($('#bbsContents').summernote('isEmpty')) { // 기본으로 <p><br></p> 형식
            invalidMsg(bbsError);
        } else {
            validMsg(bbsError);
            editor.removeClass('is-invalid');
            editor.addClass('is-valid');
        }
    })
}

function validateBbs() { // 타이틀과 내용이 버어있는가
    const editor = $('.note-editable');
    const bbsContent = $('#bbsContents');
    const titleError = $('#titleError');
    const bbsError = $('#bbsError');

    if (titleError.hasClass('invalid-feedback')
        || bbsError.hasClass('invalid-feedback')) {
        return false;
    } else {
        bbsContent.val(editor[0].innerHTML);
        return true;
    }
}

function invalidMsg(object) { // error
    object.removeClass('invisible');
    object.addClass('invalid-feedback');
}

function validMsg(object) { // valid
    object.removeClass('invalid-feedback');
    object.addClass('invisible');
}

function deleteBbs(bbsId, bbsCategoryId) { // 게시물 삭제
    $('#deleteBtn').on('click', function () {
        const address = '/post/bbs?category=' + bbsCategoryId;
        $.ajax({
            method: "DELETE",
            url: "/post/bbs/delete/" + bbsId + "?category=" + bbsCategoryId,
            dataType: "text",
            success: function () {
                location.href = address;
            }
        })
    })
}

function uploadSummernoteImage(file, editor) {
    const data = new FormData();
    data.append('file', file);

    $.ajax({
        type: 'POST',
        data: data,
        url: '/post/bbs/uploadImg',
        contentType: false,
        processData: false,
        success: function (data) {
            $(editor).summernote('insertImage', data.url);
        }
    })
}