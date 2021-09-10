let setting = { // summernote 설정
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
    fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72'],
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

function changeUrl(categoryId) { // page, sort 파라미터가 보이지 않게 처리
    let newUrl = location.origin + location.pathname + '?category=' + categoryId;
    console.log("location.href = " + location.href);
    const params = new URLSearchParams(location.href);
    const page = params.get('page');
    const sort = params.get('sort');
    const state = { 'category': categoryId, 'page' : page, 'sort' : sort};

    history.pushState(state, null, newUrl);

    window.onpopstate = function (event) {
        alert(event.state);
    }
}

function updateViews() { // 조회수 업데이트

    $('tr').on('click', function () {
        const tr = $(this).children();
        const bbsId = tr.first().text();

        console.log("bbsId : " + bbsId);
        $.ajax({
            type: 'POST',
            url: '/post/bbs/ajax/update/views',
            dataType: 'json',
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
    const bbsTitle = $('#bbsTitle').eq(0);
    const titleError = $('#titleError');

    bbsTitle.on('focusout', function () {
        if (bbsTitle.val() == '') {
            invalidMsg(titleError);
        } else {
            validMsg(titleError);
        }
    })
}

function contentValidate() { // 내용이 비어있으면 error
    const bbsError = $('#bbsError');

    // summernote에서 제공하는 콜백 사용
    $('#bbsContents').on('summernote.blur', function () {
        if ($('#bbsContents').summernote('isEmpty')) {
            invalidMsg(bbsError);
        } else {
            validMsg(bbsError);
        }
    });
}

function validateBbs() { // 타이틀과 내용이 버어있는가
    $('#submitBtn').on('click', function () {
        const editor = $('.note-editable');
        const bbsContent = $('#bbsContents');
        const titleError = $('#titleError');
        const bbsError = $('#bbsError');

        if (titleError.hasClass('error')
            || bbsError.hasClass('error')) {
            alert('내용을 모두 채워주세요 :)');
            return false;
        } else {
            bbsContent.val(editor[0].innerHTML);
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

let urlList = []; // 사진 src만 갖고 있는 배열

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
                url: '/post/bbs/ajax/deleteImg',
                success: function (response) {
                    if (response === 'ok') {
                        window.history.back();
                    }
                }
            })
        } else { window.history.back(); }
    })
}

function deleteBbs(bbsId, bbsCategoryId) { // 게시물 삭제
    $('#deleteBtn').on('click', function () {
        const address = '/post/bbs?category=' + bbsCategoryId;
        let data = new Object();
        data.id = bbsId;

        const imgList = $('#bbsContents img');
        if (imgList != null) {
            urlList = [];
            for(let i = 0; i < imgList.length; i++) {
                const startIndex = imgList[i].currentSrc.indexOf('/summernoteImg/');
                urlList.push(imgList[i].currentSrc.substring(startIndex, imgList[i].currentSrc.length));
            }
            data.urls = urlList;
        }
        console.log(data);
        $.ajax({
            method: 'DELETE',
            data: JSON.stringify(data),
            contentType: 'application/json;charset=utf-8;',
            url: '/post/bbs/ajax/delete',
            success: function () {
                location.href = address;
            }
        })
    })
}

function uploadSummernoteImage(file) {
    const data = new FormData();
    data.append('file', file);

    $.ajax({
        type: 'POST',
        data: data,
        enctype: 'multipart/form-data',
        url: '/post/bbs/ajax/uploadImg',
        contentType: false,
        processData: false,
        success: function (data) {
            $('#bbsContents').summernote('insertImage', data.url);
        }
    })
}

function deleteImageFile(src) {
    let obj = [];
    obj.push(src);
    $.ajax({
        type: 'POST',
        data: JSON.stringify({ 'src' : obj }),
        url: '/post/bbs/ajax/deleteImg',
        contentType: 'application/json;charset=utf-8;',
        dataType: 'text',
        success: function (response) {
            console.log(response);
        }
    })
}