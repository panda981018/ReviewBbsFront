$(document).on('click', 'td[data-column-name=bbsTitle]', function () { // 해당 게시글로 이동
    const td = $(this).siblings()[0];
    const bid = td.children[0].innerHTML;

    $.ajax({ // 조회수 증가 코드
        type: 'POST',
        url: '/api/bbs/update/views',
        dataType: 'json',
        data: JSON.stringify({"id": bid}),
        contentType: 'application/json; charset=utf-8',
        error: function () {
            console.log('view update failed.');
        }
    })

    location.href = '/post/bbs/view?id=' + bid;
})



let urlList = []; // 사진 src만 갖고 있는 배열

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
        let columnsModel = grid.getColumns();
        for(let i = 0; i < columnsModel.length; i++) { // 정렬기준 초기화
            grid.unsort(columnsModel[i].name);
        }

        const sortStandard = $('#sortStandard option:selected').val();
        grid.sort(sortStandard, false, true); // sort(columnName, ascending, multiple)
    });
}

function filterData() {
    $('#searchImg').on('click', function () {
        // 검색 초기화
        grid.unfilter('bbsTitle');
        grid.unfilter('bbsWriter');
        const input = $('#searchInput');
        const selectedValue = $('#findStandard option:selected').val();

        if (input.val() === '') {
            alert('내용을 입력해주세요.');
        } else {
            let state = {};
            let arr = [];
            switch (selectedValue) {
                case 'bbsTitle' :
                    state.code = 'contain';
                    state.value = input.val();
                    break;
                case 'bbsWriter' :
                    state.code = 'eq';
                    state.value = input.val();
                    break;
            }
            arr.push(state);
            input.val('');
            grid.filter(selectedValue, arr);

        }
    });
}