let urlList = []; // 사진 src만 갖고 있는 배열

function updateNoticeViews() { // 조회수 업데이트
    $('tr').on('click', function () {
        const tr = $(this).children();
        const noticeId = tr.first().text();

        console.log("noticeId : " + noticeId);
        $.ajax({
            type: 'POST',
            url: '/notice/ajax/update/views',
            dataType: 'json',
            data: JSON.stringify({ "id": noticeId}),
            contentType: 'application/json; charset=utf-8',
            error: function (error) {
                console.log(this.error + '\nview update failed.');
            }
        });
    });
}

function deleteNotice(noticeId) { // 게시물 삭제
    $('#deleteNoticeBtn').on('click', function () {
        let result = confirm('공지를 삭제하시겠습니까?');
        if (result) {
            let data = new Object();
            data.id = noticeId;

            const imgList = $('#noticeContents img');
            if (imgList !== null) {
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
                url: '/notice/ajax/delete',
                success: function () {
                    location.href = '/notice/';
                }
            })
        } else {
            return false;
        }
    });
}

