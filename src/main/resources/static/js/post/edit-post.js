// 보고 있는 카테고리에서 작성을 누르면 write로 넘어갔을 때 그 카테고리로 세팅되도록 설정하는 함수
function selectCategory(categoryId) {
    $('#bbsCategory').val(categoryId);
}

function showMapModal(lat, lng) { // 지도 모달을 띄우게 하기 위한 함수
    $(document).on('click', '#mapBtn', function () {
        const hiddenLat = $('#hiddenLat')[0];
        const hiddenLng = $('#hiddenLng')[0];
        console.log('hiddenLat = ' + hiddenLat.value);
        console.log('lat = ' + lat);
        if ((hiddenLat.value === '' && hiddenLng.value === '')
            || (hiddenLat.value === '0.0' && hiddenLng.value === '0.0')) { // 게시물처음작성
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(function (position) {
                    const currLat = position.coords.latitude;
                    const currLng = position.coords.longitude;
                    initMap(currLat, currLng, 'first');
                });
            }
        } else if (hiddenLat.value !== '' && hiddenLng.value !== '') { // 새게시물이지만 장소저장을 한번이상 한 경우
            initMap(hiddenLat.value, hiddenLng.value, 'saved');
        } else if (hiddenLat.value !== lat && hiddenLng.value !== lng) { // 게시물 수정페이지에서 한번이상 수정한 경우
            initMap(hiddenLat.value, hiddenLng.value, 'saved');
        } else { // 수정페이지에서 한번도 수정 안함
            initMap(lat, lng, 'saved');
        }
        $('#mapDialog').modal('show');
    });
}