let bbsCategoryId = 0;
let grid;

function getCategoryId(categoryId) {
    bbsCategoryId = categoryId;
}

function createGrid() {
    grid = new tui.Grid({
        el: $('#grid')[0],
        pageOptions: { perPage: 10 },
        scrollX: false,
        scrollY: false,
        columns: [
            {
                header: '번호',
                name: 'id',
                width: "auto",
                sortable: true,
                sortingType: 'desc',
                align: "center"
            },
            {
                header: '제목',
                name: 'title',
                resizable: true,
                sortable: true,
                sortingType: 'desc',
                align: "left"
            },
            {
                header: '작성자',
                name: 'writer',
                width: 65,
                sortable: true,
                sortingType: 'desc',
                align: "center"
            },
            {
                header: '작성날짜',
                name: 'createDate',
                width: 135,
                resizable: true,
                sortable: true,
                sortingType: 'desc',
                align: "center"
            },
            {
                header: '조회수',
                name: 'views',
                width: "auto",
                sortable: true,
                sortingType: 'desc',
                align: "center"
            },
            {
                header: '댓글수',
                name: 'replies',
                width: "auto",
                sortable: true,
                sortingType: 'desc',
                align: "center"
            }
        ]
    });
}

function callBbsData() {
    console.log('url: /api/bbs/get?category=' + bbsCategoryId);
    $.ajax({
        method: 'GET',
        url: '/api/bbs/get?category=' + bbsCategoryId,
        dataType: 'json',
        success: function (bbsList) {
            if (bbsList.bbsList.length != 0) {
                createGrid();
                $('#tableInfo').addClass("mb-3 d-flex flex-row justify-content-start align-items-center")
                    .css('display', 'block');
                $('#noData').css('display', 'none');
                $('#grid').css('display', 'block');
                setGridData(bbsList.bbsList);
            } else if (bbsList.bbsList.length == 0) { // 데이터가 없을 때
                $('#tableInfo').css('display', 'none');
                $('#noData').css('display', 'block');
            }
        }
    })
}

function setGridData(bbsList) {
    let data = [];
    for (let i = 0; i < bbsList.length; i++) {
        const bbs = {};
        bbs.id = bbsList[i].id;
        bbs.title = bbsList[i].bbsTitle;
        bbs.writer = bbsList[i].bbsWriter;
        bbs.createDate = bbsList[i].bbsDate;
        bbs.views = bbsList[i].bbsViews;
        bbs.replies = bbsList[i].replyCnt;

        data.push(bbs);
    }
    grid.resetData(data);
}