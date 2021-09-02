function updateViews() {

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

function titleValidate() {
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

function contentValidate() {
    const bbsContent = $('#bbsContents');
    const bbsError = $('#bbsError');

    bbsContent.on('focusout', function () {
        if (bbsContent.val() == '') {
            invalidMsg(bbsError);
        } else {
            validMsg(bbsError);
            bbsContent.removeClass('is-invalid');
            bbsContent.addClass('is-valid');
        }
    })
}

function validateBbs() {
    const titleError = $('#titleError');
    const bbsError = $('#bbsError');

    if (titleError.hasClass('invalid-feedback')
        || bbsError.hasClass('invalid-feedback')) {
        return false;
    } else {
        return true;
    }
}

function invalidMsg(object) {
    object.removeClass('invisible');
    object.addClass('invalid-feedback');
}

function validMsg(object) {
    object.removeClass('invalid-feedback');
    object.addClass('invisible');
}

function deleteBbs(bbsId, bbsCategoryId) {
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