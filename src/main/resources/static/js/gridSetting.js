let bbsCategoryId = 0; // category 아이디(1번부터~)
let pagination; // grid의 pagination 객체
let dataSource; // grid datasource
let grid; // tui grid

function getCategoryId(categoryId) {
    bbsCategoryId = categoryId;
}

function createGrid(dataSource) {
    grid = new tui.Grid({
        el: $('#grid')[0],
        minBodyHeight: 40,
        scrollX: false,
        scrollY: false,
        pageOptions: {
            perPage: 5,
            visiblePages: 5
        },
        data: dataSource,
        useClientSort: true,
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
            },
            {
                header: '댓글수',
                name: 'replyCnt',
                width: "auto",
                align: "center"
            }
        ]
    }); // grid 객체
    grid.data = dataSource;
}

function setDatasource() {
    dataSource = {
        api: { readData: { url: '/api/bbs/get', method: 'GET', initParams: { category : bbsCategoryId } }
        }
    }
    createGrid(dataSource);
    grid.on('response', ev => { // readData의 결과를 받는 콜백함수
        console.log(JSON.parse(ev.xhr.response));

        const bbsData = JSON.parse(ev.xhr.response).data.contents;
        if(bbsData.length > 0) {
            $('#tableInfo').addClass("mb-3 d-flex flex-row justify-content-between align-items-center")
                .css('display', 'block');
            $('#noData').css('display', 'none');
            $('#grid').css('display', 'block');
            grid.resetData(bbsData);
        } else {
            $('#tableInfo').css('display', 'none');
            $('#noData').css('display', 'block');
        }
    });
}

// function callBbsData() {
//     $.ajax({
//         method: 'GET',
//         url: '/api/bbs/get?category=' + bbsCategoryId,
//         dataType: 'json',
//         success: function (jsonMap) {
//             if (jsonMap.data.length !== 0) {
//                 createGrid();
//                 $('#tableInfo').addClass("mb-3 d-flex flex-row justify-content-between align-items-center")
//                     .css('display', 'block');
//                 $('#noData').css('display', 'none');
//                 $('#grid').css('display', 'block');
//                 grid.resetData(jsonMap.data);
//             } else { // 데이터가 없을 때
//                 $('#tableInfo').css('display', 'none');
//                 $('#noData').css('display', 'block');
//             }
//         }
//     })
// }