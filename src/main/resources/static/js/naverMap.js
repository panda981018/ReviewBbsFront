let map;
let infoWindow;

function onSuccessGeolocation(position) {
    let location = new naver.maps.LatLng(position.coords.latitude, position.coords.longitude);

    map.setCenter(location); // 얻은 좌표를 지도의 중심으로 설정합니다.
    map.setZoom(15); // 지도의 줌 레벨을 변경합니다.

    infoWindow.setContent("<div style='padding:20px;'>위도:" + position.coords.latitude + "경도:" + position.coords.longitude + "</div>");

    infoWindow.open(map, location);
    console.log('Coordinates: ' + location.toString());
}

function onErrorGeolocation() {
    let center = map.getCenter();

    infoWindow.setContent("<div id='' style='padding:20px;'>" +
        "<h5 style='margin-bottom:5px;color:#f00;'>Geolocation failed!</h5>latitude: " + center.lat() + "<br />longitude: " + center.lng() + "</div>");

    infoWindow.open(map, center);
}


function initMap() {
    map = new naver.maps.Map('map');
    infoWindow = new naver.maps.InfoWindow({
        maxWidth: 100
    });

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(onSuccessGeolocation, onErrorGeolocation);
    } else {
        let center = map.getCenter();
        infoWindow.setContent("<div style='padding:20px;'>" +
            "<h5 style='margin-bottom:5px;color:#f00;'>Geolocation not supported</h5></div>");
        infoWindow.open(map, center);
    }
}