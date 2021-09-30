function invalidMsg(object) { // error
    object.removeClass('valid');
    object.addClass('error');
}

function validMsg(object) { // valid
    object.removeClass('error');
    object.addClass('valid');
}

function titleValidate() {
    const title = $('#titleInput').eq(0);
    const titleError = $('#titleError');

    title.on('focusout', function () {
        if (title.val().trim() == '') {
            invalidMsg(titleError);
        } else {
            validMsg(titleError);
        }
    })
}

function contentValidate() {
    const contentError = $('#contentsError');

    // summernote에서 제공하는 콜백 사용
    $('#writeSpace').on('summernote.blur', function () {
        if ($('#writeSpace').summernote('isEmpty')) {
            invalidMsg(contentError);
        } else {
            validMsg(contentError);
        }
    });
}

let urlList = []; // 사진 src만 갖고 있는 배열

function validateForm() { // 타이틀과 내용이 버어있는가
    $('#submitBtn').on('click', function () {
        const editor = $('.note-editable');
        const writeSpace = $('#writeSpace');
        const titleError = $('#titleError');
        const contentsError = $('#contentsError');

        if (titleError.hasClass('error')
            || contentsError.hasClass('error')) {
            alert('내용을 모두 채워주세요 :)');
            return false;
        } else {
            writeSpace.val(editor[0].innerHTML.trim());
            $('form').eq(0).submit();
        }
    })
}

function cancelingWrite() { // 취소버튼 클릭
    const editor = $('.note-editable');
    const oldContents = editor.eq(0).innerHTML.trim();
    const cancel = $('#cancelBtn');

    cancel.on('click', function () {

        const newContents = editor.eq(0).innerHTML.trim();
        if (newContents === oldContents || newContents === '') {// 기존 내용이랑 현재 에디터의 내용 같으면 window.history.back()
            window.history.back();
        } else if (newContents !== oldContents) { // 다르면
            let data = {}; // ajax data object

            const imgList = $('.note-editable p').eq(0).children('img');
            if (imgList != null) {
                urlList = [];
                for(let i = 0; i < imgList.length; i++) {
                    const startIndex = imgList[i].currentSrc.indexOf('/summernoteImg/');
                    urlList.push(imgList[i].currentSrc.substring(startIndex, imgList[i].currentSrc.length));
                }
                data.src = urlList;

                $.ajax({
                    method: 'POST',
                    data: JSON.stringify(data),
                    contentType: 'application/json;charset=utf-8;',
                    url: '/summernote/deleteImg',
                    success: function (response) {
                        if (response === 'ok') {
                            window.history.back();
                        }
                    }
                })
            }
        }
    })
}