let bbsCategoryId = 0; // category 아이디(1번부터~)
let grid; // grid 객체
let pagination; // grid의 pagination 객체
let bbsData; // bbs에 저장된 객체 집합

function getCategoryId(categoryId) {
    bbsCategoryId = categoryId;
}

function createGrid() {
    grid = new tui.Grid({
        el: $('#grid')[0],
        scrollX: false,
        pageOptions: { perPage : 10 },
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

function createPagination() {
    pagination = grid.getPagination();
    pagination.setTotalItems(bbsData.length);
    pagination.centerAlign = true;
    // pagination = new tui.Pagination($('#pagination'), {
    //     totalItems: bbsData.length,
    //     itemsPerPage: 10,
    //     visiblePages: 5,
    //     centerAlign: true
    // });
}

function callBbsData() {
    console.log('url: /api/bbs/get?category=' + bbsCategoryId);
    $.ajax({
        method: 'GET',
        url: '/api/bbs/get?category=' + bbsCategoryId,
        dataType: 'json',
        success: function (bbsList) {
            if (bbsList.bbsList.length !== 0) {
                bbsData = bbsList.bbsList;
                createGrid();
                createPagination();
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