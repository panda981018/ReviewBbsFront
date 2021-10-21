let map;
let center;
let infoWindow;
let customOverlay;
let markers = [];

$('#findCurrentLocation').on('click', function () { // 현재 위치로 이동하게 하는 이벤트 리스너
    getCurrentLocation();
});

function initMap() {
    getCurrentLocation(); // 현재 위치를 받아서 표시한 후 db에 저장된 좌표들을 지도에 표시
    infoWindow = new kakao.maps.InfoWindow({
        zIndex: 200
    });
}

// 현재 위치의 정보를 가져온다
function getCurrentLocation() {
    if (navigator.geolocation) { // geolocation을 사용할 수 있다면
        navigator.geolocation.getCurrentPosition(function (position) {
            center = new kakao.maps.LatLng(position.coords.latitude, position.coords.longitude);
            if (map == null)
                map = new kakao.maps.Map($('#map')[0], { center : center });

            const currHtml = '<svg style="width:40px; height: 30px;">' +
                '<circle r="10" fill="#0000FF" stroke="#ffffff" stroke-width="3" cx="20" cy="15"' +
                'style="filter: drop-shadow(0 0 .3rem dimgray)"></circle></svg>';
            customOverlay = new kakao.maps.CustomOverlay({
                map: map,
                content: currHtml,
                position: center,
                zIndex: 100
            });
            map.panTo(center);

            getData();
        });
    } else {
        alert('현재 위치를 받아올 수 없음.');
    }
}
function getData() {
    let placeList;
    $.ajax({
        method: 'GET',
        url: '/map/getMarkers',
        dataType: "json",
        success: function (result) {
            if (result.response) {
                placeList = result.response;
                displayMarkers(placeList);
            }
        }
    })
}
function displayMarkers(placeSet) {
    //let fragment = $(document.createDocumentFragment()); // <li>해당 위치의 건물 이름?</li>
    let bounds = new kakao.maps.LatLngBounds();
    for (let i = 0; i < placeSet.length; i++) {
        const latitude = placeSet[i].latitude;
        const longitude = placeSet[i].longitude;
        const placeName = placeSet[i].placeName;
        const position = new kakao.maps.LatLng(latitude, longitude);
        let marker = new kakao.maps.Marker({
            position: position,
            zIndex: 100
        });

        bounds.extend(position);

        if (placeName !== null) {
            const content = '<div style="width: 200px;padding:3px; z-index:100;">' + placeName + '</div>';
            infoWindow.setContent(content);
            infoWindow.setPosition(position);

            kakao.maps.event.addListener(marker, 'mouseover', function () {
                infoWindow.open(map, marker);
            });
            kakao.maps.event.addListener(marker, 'mouseout', function () {
                infoWindow.close();
            });
        }

        marker.setMap(map);
        markers.push(marker);
    }
    map.setBounds(bounds);
    console.log(placeSet);
}