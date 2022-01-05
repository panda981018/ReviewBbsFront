function createHomeGrid() {
    grid = new tui.Grid({
        el: $('#memberHomeGrid')[0],
        minBodyHeight: 40,
        scrollX: false,
        scrollY: false,
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
    }); // homeGrid 객체
}

function getBbsData() {
    const page = 15;
    $.ajax({
        method: 'GET',
        url: '/home/bbs?perPage=' + page,
        dataType: 'json',
        success: function (jsonMap) {
            if (jsonMap.length !== 0) {
                grid.resetData(jsonMap);
            } else {
                alert('데이터가 존재하지 않습니다.');
            }
        }
    });
}