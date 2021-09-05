function clickMember() {
    $('tbody').children().on('click', function () {
        const tr = $(this).children();
        const id = tr.first().text();
        location.href = '/admin/info?id=' + id;
    })
}
function selectOrder() {
    const urlParams = new URLSearchParams(window.location.search);
    const sorted = urlParams.get('sort');
    if (sorted == null) {
        $('#sortSelect').val('id');
    } else {
        $('#sortSelect').val(sorted);
    }
}

function sortBy() {
    $('#sortSelect').on('change', function () {
        let sort = $('#sortSelect option:selected').val().toLowerCase();
        $.ajax({
            method: "GET",
            url: "/admin/manage/sort?sort=" + sort,
            dataType: "json",
            success: function (result) {
                const address = '/admin/manage?page=' + result.rs.number + '&sort=' + sort;
                location.href = address;
            }
        });
    });
}

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
