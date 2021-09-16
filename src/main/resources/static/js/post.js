let urlList = []; // 사진 src만 갖고 있는 배열

function changeUrl(categoryId) { // page, sort 파라미터가 보이지 않게 처리
    let newUrl = location.origin + location.pathname + '?category=' + categoryId;
    console.log("location.href = " + location.href);
    const params = new URLSearchParams(window.location.search);
    let page = '';
    let sort = '';
    let sortStandard = [];

    if (params.get('page') != null || undefined) {
        page = params.get('page');
    }
    if (params.get('sort') != null || undefined) {
        sort = params.get('sort');
        sortStandard = sort.split(','); // sort=id,asc 이런 query를 [id,asc]로 나누는 작업
    }

    const state = (sort !== '')
        ? {'category': categoryId, 'page': page, 'sort': sort} : {'category': categoryId, 'page': page}

    if (sort === '') { // 주소에 sort가 없을 때는 id를 선택하게
        $('#sortStandard').val('id');
    } else {
        $('#sortStandard').val(sortStandard[0]);
    }

    history.pushState(state, null, newUrl);

    window.onpopstate = function (event) { // ???
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
            data: JSON.stringify({"id": bbsId}),
            contentType: 'application/json; charset=utf-8',
            error: function () {
                console.log('view update failed.');
            }
        })
    })
}

function deleteBbs(bbsId, bbsCategoryId) { // 게시물 삭제
    $('#deleteBbsBtn').on('click', function () {

        const result = confirm('게시글을 삭제하시겠습니까?');
        if (result) {
            const address = '/post/bbs?category=' + bbsCategoryId;
            let data = new Object();
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
                url: '/post/bbs/ajax/delete',
                success: function () {
                    location.href = address;
                }
            })
        } else {
            $(this).focused = false;
        }
    })
}

function sortBbs(categoryId) {
    $('#sortStandard').on('change', function () {
        let sort = $('#sortStandard option:selected').val();
        $.ajax({
            method: "GET",
            url: '/post/bbs/ajax/sort?sort=' + sort + ',desc' + '&category=' + categoryId,
            dataType: "json",
            success: function (result) {
                console.log(result.bbsList);
                const address = '/post/bbs?category=' + categoryId + '&page=' + result.bbsList.number + '&sort=' + sort + ',desc';
                location.href = address;
            }
        });
    });
}

