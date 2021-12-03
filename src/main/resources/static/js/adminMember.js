let pagination; // grid의 pagination 객체
let dataSource; // grid datasource
let grid; // tui grid

// 선택된 row의 멤버 정보 수정 페이지로 이동하는 리스너
function clickMember() {
    $('tbody').children().on('click', function () {
        const tr = $(this).children();
        const id = tr.first().text();
        location.href = '/admin/info?id=' + id;
    })
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
        $.ajax({
            method: "GET",
            url: "/admin/manage/sort?sort=" + sort,
            dataType: "json",
            success: function (result) {
                console.log(result.rs);
                const address = '/admin/manage?page=' + result.rs.number + '&sort=' + sort;
                location.href = address;
            }
        });
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
                align: "center"
            },
            {
                header: '가입일자',
                name: 'regDate',
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
                url: '/api/manage', method: 'GET'
            }
        }
    }
    createMemberInfoGrid(dataSource);
}

function responseHandler() {
    grid.on('response', ev => {
        console.log(ev);
        JSON.parse(ev.xhr.response).data.contents;
        const resultCnt = JSON.parse(ev.xhr.response).data.pagination.totalCount;

        if (resultCnt > 0) {
            grid.resetData();
        } else { // count가 0이라면
            alert('회원이 없습니다.');
        }
    });
}