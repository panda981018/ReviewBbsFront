// 보고 있는 카테고리에서 작성을 누르면 write로 넘어갔을 때 그 카테고리로 세팅되도록 설정하는 함수
function selectCategory(categoryId) {
    $('#bbsCategory').val(categoryId);
}

function showMapModal() { // 지도 모달을 띄우게 하기 위한 함수
    $(document).on('click', '#mapBtn', function () {
        $('#mapDialog').modal('show');
    })
}