let grid = new tui.Grid({
    el: $('#grid')[0],
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

function callBbsData(categoryId) {
    $.ajax({
        method: 'GET',
        url: '/api/bbs/get?category=' + categoryId,
        dataType: 'json',
        success: function (bbsList) {
            setGridData(bbsList.bbsList);
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