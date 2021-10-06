let bbsCategoryId = 0; // category 아이디(1번부터~)
let pagination; // grid의 pagination 객체
let grid;

function getCategoryId(categoryId) {
    bbsCategoryId = categoryId;
}

function createGrid() {
    grid = new tui.Grid({
        el: $('#grid')[0],
        minBodyHeight: 40,
        scrollX: false,
        scrollY: false,
        pageOptions: {
            useClient: true,
            perPage: 10
        },
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
                name: 'bbsTitle',
                resizable: true,
                sortable: true,
                sortingType: 'desc',
                filter: {
                    type: 'text'
                },
                align: "left"
            },
            {
                header: '작성자',
                name: 'bbsWriter',
                width: 65,
                sortable: true,
                sortingType: 'desc',
                filter: {
                    type: 'text'
                },
                align: "center"
            },
            {
                header: '작성날짜',
                name: 'bbsDate',
                width: 135,
                resizable: true,
                sortable: true,
                sortingType: 'desc',
                align: "center"
            },
            {
                header: '조회수',
                name: 'bbsViews',
                width: "auto",
                sortable: true,
                sortingType: 'desc',
                align: "center"
            },
            {
                header: '댓글수',
                name: 'replyCnt',
                width: "auto",
                sortable: true,
                sortingType: 'desc',
                align: "center"
            }
        ],
        useClientSort: true
    }); // grid 객체
}

function callBbsData() {
    $.ajax({
        method: 'GET',
        url: '/api/bbs/get?category=' + bbsCategoryId,
        dataType: 'json',
        success: function (jsonMap) {
            if (jsonMap.data.length !== 0) {
                createGrid();
                $('#tableInfo').addClass("mb-3 d-flex flex-row justify-content-between align-items-center")
                    .css('display', 'block');
                $('#noData').css('display', 'none');
                $('#grid').css('display', 'block');
                grid.resetData(jsonMap.data);
            } else { // 데이터가 없을 때
                $('#tableInfo').css('display', 'none');
                $('#noData').css('display', 'block');
            }
        }
    })
}