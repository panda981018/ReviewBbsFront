let center; // 중심점
let marker; // 마커
let map; // 지도
let infoWindow; // 정보창
let customOverlay; // 현재 위치를 표시하기 위한 overlay

$('#findCurrentLocation').on('click', function () { // 현재 위치로 이동하게 하는 이벤트 리스너
    getCurrentLocation();
});

$('#submit').on('click', () => searchPlaces()); // 검색버튼

function initMap(lat, lng) { // 지도 초기화
    center = new kakao.maps.LatLng(lat, lng);
    const options = { // map 옵션
        center: center
    };
    map = new kakao.maps.Map($('#map')[0], options); // 지도 생성 및 객체 리턴턴

    marker = new kakao.maps.Marker({
        position: center
    });
    infoWindow = new kakao.maps.InfoWindow({
        // content: msg,
        position: center,
        zIndex: 200
    });
    setTimeout(() => { getCurrentLocation(); map.relayout();}, 500); // sleep(500)
}

function setCenter(message) { // 원하는 위치를 센터로 정하기
    if (message !== '') {
        infoWindow.setContent(message); // infowindow에 표시할 content
        infoWindow.setPosition(center); // infowindow를 표시할 위치
        infoWindow.open(map, marker); // infowindow를 보이게 하기
    }

    map.setLevel(4); // map 줌 레벨 설정
    marker.setMap(map); // 마커를 맵에 붙인다.
    map.panTo(center); // 중심 옮길 때 부드럽게 옮기기
}

function getCurrentLocation() { // 현재 위치의 정보를 가져온다
    if (navigator.geolocation) { // geolocation을 사용할 수 있다면
        navigator.geolocation.getCurrentPosition(function (position) {
            center = new kakao.maps.LatLng(position.coords.latitude, position.coords.longitude);
            const currHtml = '<svg style="width:40px; height: 30px;">' +
                '<circle r="10" fill="#0000FF" stroke="#ffffff" stroke-width="3" cx="20" cy="15"' +
                'style="filter: drop-shadow(0 0 .3rem dimgray)"></circle></svg>';
            customOverlay = new kakao.maps.CustomOverlay({
                map: map,
                content: currHtml,
                position: center,
                zIndex: 100
            });
            map.panTo(center); // 부드럽게 중심이동
        });
    } else {
        alert('현재 위치를 받아올 수 없음.');
    }
}

function searchPlaces() { // 장소 검색
    const searchObject = new kakao.maps.services.Places(); // 검색 객체 생성
    const keyword = $('#keyword').val(); // 검색창에 입력된 값을 가져온다.
    searchObject.keywordSearch(keyword, searchSuccess);
}

function searchSuccess(data, status, pagination) { // 키워드 검색 완료 시 호출되는 콜백함수
    if (status === kakao.maps.services.Status.OK) {
        infoWindow.close();
        customOverlay.setMap(null);
        marker.setMap(null);
        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기 위해
        // LatLngBounds 객체에 좌표를 추가한다.
        let bounds = new kakao.maps.LatLngBounds();

        for (let i = 0; i < data.length; i++) {
            displayMarker(data[i]);
            bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x)); // bounds에 좌표 추가
        }

        // 검색된 장소 위치를 기준으로 지도 범위를 재설정.
        map.setBounds(bounds);
    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
        alert('검색 결과가 없습니다.');
        return;
    } else if (status === kakao.maps.services.Status.ERROR) {
        alert('검색 도중 오류가 발생했습니다. 다시 시도해주세요.');
        return;
    }
}

function displayMarker(place) { // 검색 결과를 기반으로 마커들을 화면에 표시하는 함수
    // 마커를 생성하고 지도에 표시합니다
    let marker = new kakao.maps.Marker({
        map: map,
        position: new kakao.maps.LatLng(place.y, place.x),
    });

    // 마커에 클릭이벤트를 등록합니다
    kakao.maps.event.addListener(marker, 'click', function() {
        // 마커를 클릭하면 장소명이 인포윈도우에 표출됩니다
        const content = '<div style="width:100%;padding:5px;font-size:15px;text-align: center;">'
            + place.place_name + '</div>';
        infoWindow.setContent(content);
        infoWindow.open(map, marker);
    });
}

