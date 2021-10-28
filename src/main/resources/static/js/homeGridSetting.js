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


// function setDatasource() {
//     dataSource = {
//         api: {
//             readData: {
//                 url: '/api/bbs/get', method: 'GET',
//                 initParams: {category: bbsCategoryId, column: sortType}
//             }
//         }
//     }
//     createGrid(dataSource);
// }
//
// function responseHandler() {
//     grid.on('response', ev => { // readData의 결과를 받는 콜백함수
//         console.log(ev);
//         const bbsData = JSON.parse(ev.xhr.response).data.contents;
//         const resultCnt = JSON.parse(ev.xhr.response).data.pagination.totalCount;
//         if (resultCnt > 0) {
//             $('#tableInfo').addClass("mb-3 d-flex flex-row justify-content-between align-items-center")
//                 .css('display', 'block');
//             $('#grid').css('display', 'block');
//             grid.resetData(bbsData);
//         } else { // 0이거나 그 이하면
//             alert('찾으시는 결과가 없습니다.');
//         }
//     });
// }