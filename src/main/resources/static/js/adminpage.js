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