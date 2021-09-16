const setting = { // summernote 설정
    height: 200,                 // 에디터 높이
    minHeight: null,             // 최소 높이
    maxHeight: null,             // 최대 높이
    lang: "ko-KR",					// 한글 설정
    placeholder: '장소에 대한 리뷰를 적어주세요!',	//placeholder 설정
    disableGrammar: true,
    toolbar: [
        ['fontname', ['fontname']],
        ['fontsize', ['fontsize']],
        ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
        ['color', ['forecolor','color']],
        ['table', ['table']],
        ['para', ['ul', 'ol', 'paragraph']],
        ['height', ['height']],
        ['insert',['picture','link','video']],
        ['view', ['fullscreen', 'help']],
        ['lineHeights', ['lineHeights']]
    ],
    fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋움체','바탕체'],
    lineHeights: ['0.2', '0.3', '0.4', '0.5', '0.6', '0.8', '1.0', '1.2', '1.4', '1.5', '2.0', '3.0'],
    callbacks: {
        onImageUpload: function (files) { // override image upload handler(default: base64 dataURL on IMG tag)
            for(let i = files.length - 1; i >= 0; i--) {
                uploadSummernoteImage(files[i]);
            }
        },
        onMediaDelete: function (target) { // delete media handler
            const startIndex = target[0].src.indexOf('/summernoteImg/');
            let imageUrl = target[0].src.substring(startIndex, target[0].src.length);
            deleteImageFile(imageUrl);
            deleteImageFile(imageUrl);
        },
        onPaste: function (e) { // 복붙 핸들러
            let clipboardData = e.originalEvent.clipboardData;
            if (clipboardData && clipboardData.items
                && clipboardData.items.length) {
                let item = clipboardData.items[0];
                if (item.kind === 'file' && item.type.indexOf('image/') !== -1) {
                    e.preventDefault();
                }
            }
        }
    }};
let urlList = []; // 사진 src만 갖고 있는 배열

function noticeTitleValidate() { // 타이틀 비어있으면 error
    const noticeTitle = $('#noticeTitle').eq(0);
    const titleError = $('#titleError');

    noticeTitle.on('focusout', function () {
        if (noticeTitle.val() == '') {
            invalidMsg(titleError);
        } else {
            validMsg(titleError);
        }
    })
}

function noticeContentValidate() { // 내용이 비어있으면 error
    const noticeError = $('#noticeError');

    // summernote에서 제공하는 콜백 사용
    $('#noticeContents').on('summernote.blur', function () {
        if ($('#noticeContents').summernote('isEmpty')) {
            invalidMsg(noticeError);
        } else {
            validMsg(noticeError);
        }
    });
}

function validateNotice() { // 타이틀과 내용이 버어있는가
    $('#submitBtn').on('click', function () {
        const editor = $('.note-editable');
        const noticeContent = $('#noticeContents');
        const titleError = $('#titleError');
        const noticeError = $('#noticeError');

        if (titleError.hasClass('error')
            || noticeError.hasClass('error')) {
            alert('내용을 모두 채워주세요 :)');
            return false;
        } else {
            noticeContent.val(editor[0].innerHTML);
            $('form').eq(0).submit();
        }
    })
}

function invalidMsg(object) { // error
    object.removeClass('valid');
    object.addClass('error');
}

function validMsg(object) { // valid
    object.removeClass('error');
    object.addClass('valid');
}

function cancelBbs() { // 취소버튼 클릭
    const cancel = $('#cancelBtn');
    cancel.on('click', function () {
        let data = {};

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
                url: '/notice/ajax/deleteImg',
                success: function (response) {
                    if (response === 'ok') {
                        window.history.back();
                    }
                }
            })
        } else { window.history.back(); }
    })
}

function uploadSummernoteImage(file) {
    const data = new FormData();
    data.append('file', file);

    $.ajax({
        type: 'POST',
        data: data,
        enctype: 'multipart/form-data',
        url: '/notice/ajax/uploadImg',
        contentType: false,
        processData: false,
        success: function (data) {
            $('#noticeContents').summernote('insertImage', data.url);
        }
    })
}

function deleteImageFile(src) {
    let obj = [];
    obj.push(src);
    $.ajax({
        type: 'POST',
        data: JSON.stringify({ 'src' : obj }),
        url: '/notice/ajax/deleteImg',
        contentType: 'application/json;charset=utf-8;',
        dataType: 'text',
        success: function (response) {
            console.log(response);
        }
    })
}