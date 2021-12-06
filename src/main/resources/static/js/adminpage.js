let pagination; // grid의 pagination 객체
let dataSource; // grid datasource
let grid; // tui grid

// 선택된 row의 멤버 정보 수정 페이지로 이동하는 리스너
function clickMember() {
    // 클릭된 rowkey를 가져오고 해당 row의 id column 값을 가져와서 이동시키기
    grid.on('click', ev => {
        if (ev.rowKey > -1) {
            const id = grid.getFormattedValue(ev.rowKey, 'id'); // (rowKey, columnName)
            location.href = '/admin/info?id=' + id;
        }
    });
}

function selectOrder() {
    // url에 sort 파라미터가 있는지 보기 위해
    const urlParams = new URLSearchParams(window.location.search);
    const sorted = urlParams.get('sort');
    if (sorted == null) {
        $('#sortSelect').val('id');
    } else {
        $('#sortSelect').val(sorted);
    }
}

// 정렬 기준이 변경되면 동작할 리스너
function sortBy() {
    $('#sortSelect').on('change', function () {
        let sort = $('#sortSelect option:selected').val().toLowerCase();
        grid.readData(1, {"sort": sort});
        // $.ajax({
        //     method: "GET",
        //     url: "/admin/manage?" + sort,
        //     dataType: "json",
        //     success: function (result) {
        //         console.log(result.rs);
        //         const address = '/admin/manage?page=' + result.rs.number + '&sort=' + sort;
        //         location.href = address;
        //     }
        // });
    });
}

// 사이드바에서 선택한 메뉴가 클릭되면 background 색 다르게 하기
function selectMenu(menu) {
    const aList = $('.link-dark');

    for (let i = 0; i < aList.length; i++) {
        if (aList.get(i).innerText.trim() == menu) {
            aList.get(i).classList.add('current');
        } else {
            aList.get(i).classList.remove('current');
        }
    }
}

function createMemberInfoGrid(dataSource) {
    grid = new tui.Grid({
        el: $('#grid')[0],
        minBodyHeight: 40,
        scrollX: false,
        scrollY: false,
        data: dataSource,
        pageOptions: {
            perPage: 10,
            visiblePages: 5,
            centerAlign: true
        },
        columns: [
            {
                header: 'No.',
                name: 'id',
                width: "auto",
                resizable: true,
                align: "center"
            },
            {
                header: '아이디',
                name: 'username',
                resizable: true,
                align: "center"
            },
            {
                header: '닉네임',
                name: 'nickname',
                resizable: true,
                align: "center"
            },
            {
                header: '생년월일',
                name: 'birth',
                resizable: true,
                align: "center"
            },
            {
                header: '성별',
                name: 'gender',
                width: "auto",
                resizable: true,
                align: "center"
            },
            {
                header: '가입일자',
                name: 'regDate',
                resizable: true,
                width: "auto",
                align: "center"
            }
        ]
    }); // grid 객체
}

function getDatasource() {
    dataSource = {
        api: {
            readData: {
                url: '/admin/manage', method: 'GET'
            }
        }
    }
    createMemberInfoGrid(dataSource);
}

function memberResponseHandler() {
    grid.on('response',ev => {
        console.log(ev);
        console.log(JSON.parse(ev.xhr.response).data.contents);
        const resultCnt = JSON.parse(ev.xhr.response).data.pagination.totalCount;

        if (resultCnt > 0) {
            grid.resetData(JSON.parse(ev.xhr.response).data.contents);
        } else { // count가 0이라면
            alert('회원이 없습니다.');
        }
    });
}
