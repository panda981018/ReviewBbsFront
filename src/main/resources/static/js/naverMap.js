let map;
let infoWindow;
let currentPosition;
let marker;
let currLat;
let currLng;

function initMap(latitude, longitude) {
    const size = new naver.maps.Size(800-32, 500-32);
    map = new naver.maps.Map('map');
    map.setSize(size);

    if (latitude === 0.0 && longitude === 0.0) {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(onSuccessGeolocation, onErrorGeolocation);
        } else {
            alert('현재 위치 사용 불가');
        }
    } else {
        const latLngObj = new naver.maps.LatLng(latitude, longitude);
        map.setCenter(latLngObj);
    }

    naver.maps.Event.addListener(map, 'click', function (e) { // 지도를 클릭하면 이벤트 발생
        marker.setPosition(e.coord);
        currLat = e.coord.latitude;
        currLng = e.coord.longitude;

        console.log("currLat: " + currLat);
        console.log("currLng: " + currLng);
        marker.setAnimation(naver.maps.Animation.DROP);
    });
}

function onSuccessGeolocation(position) {
    currentPosition = new naver.maps.LatLng(position.coords.latitude, position.coords.longitude);
    let contentString = '<div>' + '<span>위도: ' + position.coords.latitude
        + '경도: ' + position.coords.longitude + '</span></div>';

    console.log("type of content : " + typeof contentString + "\ncontent: " + contentString);
    map.setCenter(currentPosition); // 얻은 좌표를 지도의 중심으로 설정합니다.
    map.setZoom(15); // 지도의 줌 레벨을 변경합니다.

    marker = new naver.maps.Marker({
        position: currentPosition,
        map: map,
        animation: naver.maps.Animation.DROP
    });
    let infoWindowElement = $([
        '<div>',
        '       <span class="pin_txt">위도 : ',
        position.coords.latitude,
        '경도: ',
        position.coords.longitude,
        '</span>',
        '       <span class="spr spr_arr"></span>',
        '</div>'].join(''));

    infoWindow = new naver.maps.InfoWindow({
        content: infoWindowElement[0],
        maxWidth: 140,
        backgroundColor: "#eee",
        borderWidth: 1,
        anchorSize: new naver.maps.Size(10, 10),
        anchorSkew: true,
        anchorColor: "#eee",
        pixelOffset: new naver.maps.Point(20, -20)
    });

    if (infoWindow.getMap()) {
        infoWindow.close();
    } else {
        infoWindow.open(map, marker);
    }
}

function onErrorGeolocation() {
    let center = map.getCenter();
    infoWindow.setContent("<div id='' style='padding:20px;'>" +
        "<h5 style='margin-bottom:5px;color:#f00;'>Geolocation failed!</h5>" +
        "latitude: " + center.lat() + "<br />" +
        "longitude: " + center.lng() + "</div>");

    infoWindow.open(map, center);
}

