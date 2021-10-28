let map;
let center;
let customOverlay;
let geocoder;
let markers = [];
let infoWindow;
let detailAddr;
let itemListEle;
let currPage;

$('#findCurrentLocation').on('click', function () { // 현재 위치로 이동하게 하는 이벤트 리스너
    getCurrentLocation();
});

function initMap() {
    getCurrentLocation(); // 현재 위치를 받아서 표시한 후 db에 저장된 좌표들을 지도에 표시
    geocoder = new kakao.maps.services.Geocoder();
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
                ' style="filter: drop-shadow(0 0 .3rem dimgray)"></circle></svg>';
            customOverlay = new kakao.maps.CustomOverlay({
                map: map,
                content: currHtml,
                position: center,
                zIndex: 100
            });
            getData(1);
        });
    } else {
        alert('현재 위치를 받아올 수 없음.');
    }
}

function getData(pageNum) {
    $.ajax({
        method: 'GET',
        url: '/map/getMarkers?page=' + pageNum,
        dataType: "json",
        success: function (result) {
            if (result.data.length > 0) {
                currPage = pageNum;
                console.log(result);
                displayPlaces(result.data);
                displayPagination(result.totalPages);
            }
        }
    });
}

function displayPlaces(favList) { // 마커 mouseover, mouseout 리스너는 여기서 등록
    const placeListEle = $('#placeList')[0];
    let menuWrapEle = $('#menu_wrap');
    let bounds = new kakao.maps.LatLngBounds();
    infoWindow = new kakao.maps.InfoWindow({
        zIndex: 200
    });

    removeAllChildNods(placeListEle);
    removeMarker();

    for (let i = 0; i < favList.length; i++) {
        const latitude = favList[i].latitude;
        const longitude = favList[i].longitude;
        const position = new kakao.maps.LatLng(latitude, longitude); // 위치
        let marker = addMarkers(position, i); // 마커 생성

        searchDetailAddrFromCoords(marker, favList[i], placeListEle);

        bounds.extend(position);
    }
    menuWrapEle.scrollTop();
    map.setBounds(bounds);
}

function displayPagination(totalPages) {
    const paginationEle = $('#placePagination')[0];

    removeAllChildNods(paginationEle);

    for(let i = 0; i < totalPages; i++) {
        const ele = document.createElement('button');
        ele.innerHTML = i+1;
        ele.className = 'page-btn';

        ele.onclick = function () {
            infoWindow.close();
            if (currPage === parseInt(ele.innerHTML)+1) {
                ele.classList.add('on');
            }
            currPage = parseInt(ele.innerHTML);
            getData(ele.innerHTML);
        };

        if (parseInt(currPage) === i+1) {
            ele.classList.add('on');
        }
        paginationEle.append(ele);
    }
}

function addMarkers(position) { // 마커를 맵에 생성하고 리턴
    // 마커 위치 지정
    let marker = new kakao.maps.Marker({
        position: position,
        zIndex: 100
    });
    marker.setMap(map);
    markers.push(marker);

    return marker;
}

function addListItem(placeName) { // map 리스트 각 요소를 만들 함수
    let el = $('<li>', {});
    let placeNameStr = '<div><span class="fav-info-title">' + placeName + '</span></div>';
    el.append(placeNameStr);
    el.append(detailAddr);
    el[0].className = 'item';

    return el;
}

function searchDetailAddrFromCoords(marker, favObj, placeListEle) {
    geocoder.coord2Address(favObj.longitude, favObj.latitude, function (result, status) {
        if (status === kakao.maps.services.Status.OK) {
            detailAddr = !!result[0].road_address ? '<div>도로명 | ' + result[0].road_address.address_name + '</div>'
                : '';
            if (result[0].address.address_name) {
                detailAddr += '<div class="jibun-div">지번     | ' + result[0].address.address_name + '</div>';
            }
            itemListEle = addListItem(favObj.placeName);
            placeListEle.append(itemListEle[0]);

            (function (marker, bbsId, placeName, detailAddr) {
                displayInfoWindow(marker, bbsId, placeName, detailAddr);
                kakao.maps.event.addListener(marker, 'mouseover', function () {
                    displayInfoWindow(marker, bbsId, placeName, detailAddr);
                    infoWindow.open(map, marker);
                });

                kakao.maps.event.addListener(map, 'click', function () {
                    infoWindow.close();
                });

                itemListEle[0].onclick = function () {
                    map.panTo(marker.getPosition());
                    displayInfoWindow(marker, bbsId, placeName, detailAddr);
                    infoWindow.open(map, marker);
                }
            })(marker, favObj.bbsId, favObj.placeName, detailAddr);
        }
    });
}

function displayInfoWindow(marker, bbsId, placeName, detailAddr) {
    const content = '<div class="bAddr">' +
        '<div class="d-flex justify-content-between align-items-center">' +
        '<input type="hidden" id="hiddenBbsId" value="' + bbsId + '">' +
        '<span class="fav-info-title">' + placeName + '</span>' +
        '<a id="moveToBbs" class="btn btn-outline-primary" href="/post/bbs/view?id=' + bbsId +
        '">게시물로 이동</a>' +
        '</div>' +
        detailAddr + '</div>';

    infoWindow.setContent(content);
}

// 모든 마커를 삭제
function removeMarker() {
    let length = markers.length;
    for (let i = length - 1; i >= 0; i--) {
        markers[i].setMap(null);
        markers.pop();
    }
}

// 모든 자식 element를 삭제하는 함수
function removeAllChildNods(el) {
    while (el.hasChildNodes()) {
        el.removeChild(el.lastChild);
    }
}