let dataSource; // grid datasource
let grid; // tui grid

function createGrid() {
    grid = new tui.Grid({
        el: $('#newGrid')[0],
        minBodyHeight: 40,
        scrollX: false,
        scrollY: false,
        pageOptions: {
            useClient: true,
            perPage: 15
        },
        columns: [
            {
                header: '번호',
                name: 'id',
                width: "auto",
                align: "center"
            },
            {
                header: '제목',
                name: 'bbsTitle',
                resizable: true,
                align: "left"
            },
            {
                header: '작성자',
                name: 'bbsWriter',
                width: 65,
                align: "center"
            },
            {
                header: '작성날짜',
                name: 'bbsDate',
                width: 135,
                resizable: true,
                align: "center"
            },
            {
                header: '조회수',
                name: 'bbsViews',
                width: "auto",
                align: "center"
            }
        ]
    }); // grid 객체
}

function getBbsData() {
    $.ajax({
        method: 'GET',
        url: '/api/bbs/get/home?perPage=15',
        dataType: 'json',
        success: function (jsonMap) {
            if (jsonMap.data.length !== 0) {
                grid.resetData(jsonMap.data);
            } else {
                alert('데이터가 존재하지 않습니다.');
            }
        }
    })
}