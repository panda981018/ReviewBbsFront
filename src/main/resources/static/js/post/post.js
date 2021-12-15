const searchInput = $('#searchInput');
let isAdded = false;
let urlList = []; // 사진 src만 갖고 있는 배열

function deleteBbs(bbsId, categoryId) { // 게시물 삭제
    $('#deleteBbsBtn').on('click', function () {
        if (confirm('게시글을 삭제하시겠습니까?')) {
            const address = '/post/bbs?category=' + categoryId;
            let data = {};
            data.id = bbsId;

            const imgList = $('#bbsContents img');
            if (imgList != null) {
                urlList = [];
                for (let i = 0; i < imgList.length; i++) {
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
                url: '/api/bbs/delete',
                success: function () {
                    location.href = address;
                }
            })
        } else {
            $(this).focused = false;
        }
    })
}

function sortBbs() { // 정렬
    $('#sortStandard').on('change', function () {
        const sortType = $('#sortStandard option:selected').val();
        grid.readData(1, {"column": sortType});
    });
}

function filterGrid() { // 검색 이벤트 핸들러
    const selectedValue = $('#findStandard option:selected').val();

    if (searchInput.val() === '') {
        alert('내용을 입력해주세요.');
    } else {
        grid.readData(1, {"searchType": selectedValue, "keyword": searchInput.val()});
    }
}

$('#searchImg').on('click', function () {
    filterGrid();
});

searchInput.on('keydown', function (key) {
    if (key.key === 'Enter') {
        filterGrid();
    }
});

function replyListener() {
    let isShowReply = true;
    $('#replyBtn').on('click', function () {
        if (isShowReply) {
            isShowReply = false;
            $('#replyContainer').hide();
        } else {
            isShowReply = true;
            $('#replyContainer').show();
        }
    });
}

function clickLikeBtn(bbsId) { // Like 버튼을 눌렀을 때 동작하는 함수
    $('#likeBtn').on('click', function () {
        const imgSrc = $('#likeBtn img').attr('id');
        let type = '';

        if (imgSrc === 'heartImg') { // 현재 이미지의 id가 heartImg = 좋아요 클릭된 상태
            type = 'cancel';
        } else { // 좋아요 취소한 상태
            type = 'like';
        }

        $.ajax({
            url: '/api/heart/like',
            method: 'POST',
            data: JSON.stringify({"bbsId": bbsId, "type": type}),
            dataType: "json",
            contentType: 'application/json;charset=utf-8',
            success: function (jsonResult) {
                if (type === 'cancel') {
                    $('#likeBtn img').attr('src', '/img/emptyHeart.png');
                    $('#likeBtn img').attr('id', 'emptyHeartImg');
                } else {
                    $('#likeBtn img').attr('src', '/img/heart.png');
                    $('#likeBtn img').attr('id', 'heartImg');
                }
                const likeText = $('span#likeCntText');

                likeText[0].innerHTML = jsonResult.likeCnt;
            }
        })
    });
}

function showMapModal(lat, lng) {
    $('#locationInfo').on('click', function () {
        initMap(lat, lng, 'saved');
        $('#mapDialog').modal('show');
    });
}

// map에 추가/삭제
function clickAddMapBtn(favObj, bbsId, lat, lng) {
    const addBtn = $('#addMap'); // map btn

    if (favObj) { // 추가했는지 여부
        isAdded = true;
    } else {
        isAdded = false;
    }

    const placeName = $('.placeName')[0].innerHTML;

    addBtn.on('click', function (e) {
        e.stopPropagation(); // 버블링 중단
        // bbsId, type(add/cancel), lat, lng
        const data = {
            "bbsId": bbsId,
            "lat": lat,
            "lng": lng,
            "placeName": placeName
        }
        if (!isAdded) { // 추가 x -> o
            data.type = "add";
        } else {
            data.type = "cancel";
        }
        $.ajax({
            url: '/api/fav/map',
            method: 'POST',
            data: JSON.stringify(data),
            contentType: 'application/json;charset=utf-8',
            dataType: "json",
            success: function (mapData) {
                if (mapData.status === "OK") {
                    // map에 추가되었습니다. 확인창 표시
                    // + 버튼을 - 버튼으로 바꾸기
                    while (addBtn[0].hasChildNodes()) {
                        addBtn[0].removeChild(addBtn[0].lastChild);
                    }

                    if (data.type === "add") {
                        alert('Map에 추가되었습니다. 상단의 Map 메뉴에서 확인해주세요.');
                        isAdded = true;
                        addBtn.prepend('<i class="fas fa-minus me-3"></i>' +
                            '<span class="tooltip-text">Map에서 제거하기</span>');
                    } else {
                        alert('Map에서 제거되었습니다. 상단의 Map 메뉴에서 확인해주세요.');
                        isAdded = false;
                        addBtn.prepend('<i class="fas fa-plus me-3"></i>' +
                            '<span class="tooltip-text">Map에 추가하기</span>');
                    }
                } else {
                    alert('오류가 발생하였습니다. 잠시후 다시 시도해주십시오.');
                }
            }
        });
    });
}
