let center; // 중심점
let map; // 지도
let infoWindow; // 정보창
let customOverlay; // 현재 위치를 표시하기 위한 overlay
let geocoder; // 주소-좌표 변환 객체
let saveLat; // 저장할 위도
let saveLng; //  저장할 경도
let savePlaceName; // 저장할 장소 이름

let markers = []; // 마커를 담을 배열

$('#findCurrentLocation').on('click', function () { // 현재 위치로 이동하게 하는 이벤트 리스너
    getCurrentLocation();
});

$('#keyword').on('keydown', function (e) {
    if (e.keyCode === 13) {
        searchPlaces();
    }
});
$('#submit').on('click', () => searchPlaces()); // 검색버튼

// 여기서부터는 함수
// 지도 초기화
function initMap(lat, lng, type) {
    center = new kakao.maps.LatLng(lat, lng);
    const options = { // map 옵션
        center: center
    };

    map = new kakao.maps.Map($('#map')[0], options); // 지도 생성 및 객체 리턴턴
    geocoder = new kakao.maps.services.Geocoder();

    infoWindow = new kakao.maps.InfoWindow({
        // content: msg,
        position: center,
        zIndex: 200
    });

    if (type === 'first') {
        setTimeout(() => {
            center = new kakao.maps.LatLng(lat, lng);

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
            map.relayout();
        }, 500); // sleep(500)
    } else {
        setTimeout(() => {
            const marker = savedPositionMarker(center);
            map.relayout();
            map.panTo(center);
        }, 500);
    }
}

// function displayAddress(result, status) {
//     if (status === kakao.maps.services.Status.OK) {
//         console.log(result);
//     }
// }

// 원하는 위치를 센터로 정하기
// function setCenter(message, marker) {
//     if (message !== '') {
//         infoWindow.setContent(message); // infowindow에 표시할 content
//         infoWindow.setPosition(center); // infowindow를 표시할 위치
//         infoWindow.open(map, marker); // infowindow를 보이게 하기
//     }
//
//     map.setLevel(4); // map 줌 레벨 설정
//     marker.setMap(map); // 마커를 맵에 붙인다.
//     map.panTo(marker.getPosition()); // 중심 옮길 때 부드럽게 옮기기
// }

// 현재 위치의 정보를 가져온다
function getCurrentLocation() {
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

// 장소 검색
function searchPlaces() {
    const searchObject = new kakao.maps.services.Places(); // 검색 객체 생성
    const keyword = $('#keyword').val(); // 검색창에 입력된 값을 가져온다.
    const location = map.getCenter();
    searchObject.keywordSearch(keyword, searchSuccess, {
        location: location,
        radius: 8000,
        sort: kakao.maps.services.SortBy.DISTANCE
    });
}

// 키워드 검색 완료 시 호출되는 콜백함수
function searchSuccess(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {
        if (customOverlay !== null)
            customOverlay.setMap(null);

        displayPlaces(data); // 검색 목록과 마커를 표시
        displayPagination(pagination); // 페이지 번호를 표출
    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
        alert('검색 결과가 없습니다.');
        return;
    } else if (status === kakao.maps.services.Status.ERROR) {
        alert('검색 도중 오류가 발생했습니다. 다시 시도해주세요.');
        return;
    }
}

// 검색 결과를 기반으로 마커들을 화면에 표시하는 함수
function displayPlaces(places) {
    const listEle = $('#placeList')[0];
    let fragment = $(document.createDocumentFragment());
    let menuEle = $('#menu_wrap');
    let bounds = new kakao.maps.LatLngBounds();

    removeAllChildNods(listEle);
    removeMarker();

    for (let i = 0; i < places.length; i++) {
        // 마커를 생성하고 지도에 표시
        let placePosition = new kakao.maps.LatLng(places[i].y, places[i].x);
        let marker = addMarker(placePosition, i);
        let itemEle = getListItem(i, places[i]); // 검색 결과 항목 element를 생성함.

        bounds.extend(placePosition); // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기 위해
        (function (marker, title) {
            kakao.maps.event.addListener(marker, 'mouseover', function () { // mouseover 했을 때
                displayInfowindow(marker, title);
            });

            kakao.maps.event.addListener(marker, 'mouseout', function () { // mouseout을 했을 때
                infoWindow.close();
            });

            itemEle[0].onmouseout = function () {
                infoWindow.close();
            }
        })(marker, places[i].place_name);

        fragment[0].append(itemEle[0]);
    }
    // 검색결과 항목들을 검색결과 목록 element에 추가함.
    listEle.append(fragment[0]);
    menuEle.scrollTop();
    // 검색된 장소 위치를 기준으로 지도 범위를 재설정.
    map.setBounds(bounds);
}

// 검색결과 항목을 Element로 반환하는 함수입니다
function getListItem(index, places) {

    let el = $('<tr>', {});
    let itemStr = '<span class="markerbg marker_' + (index + 1) + '"></span>' +
        '<div class="info">' +
        '   <h6>' + places.place_name + '</h6>';

    if (places.road_address_name) {
        itemStr += '    <span>' + places.road_address_name + '</span>' +
            '   <span class="jibun gray">' + places.address_name + '</span>';
    } else {
        itemStr += '    <span>' + places.address_name + '</span>';
    }

    itemStr += '  <span class="tel">' + places.phone + '</span>' +
        '</div>';
    el.append(itemStr);
    el[0].className = 'item';

    return el;
}

// 게시글 작성 시 저장했던 장소에 대한 마커 생성
function savedPositionMarker(position) {
    const imageSrc = '/img/marker.png';
    const imageSize = new kakao.maps.Size(36, 36);
    const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
    let marker = new kakao.maps.Marker({
        position: position,
        image: markerImage
    });

    marker.setMap(map);
    return marker;
}

// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
function addMarker(position, idx, title) {
    // 마커 이미지 url, 스프라이트 이미지를 씁니다
    const imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png';
    const imageSize = new kakao.maps.Size(36, 37);  // 마커 이미지의 크기
    const imgOptions = {
        spriteSize: new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
        spriteOrigin: new kakao.maps.Point(0, (idx * 46) + 10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
        offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
    };
    const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions);
    let marker = new kakao.maps.Marker({
        position: position, // 마커의 위치
        image: markerImage
    });

    kakao.maps.event.addListener(marker, 'click', function () {
        if (confirm('이 위치를 저장하시겠습니까?')) {
            const position = marker.getPosition();
            const parser = new DOMParser();
            saveLat = position.getLat();
            saveLng = position.getLng();
            if (infoWindow.getContent() !== undefined) {
                const placeName = parser.parseFromString(infoWindow.getContent(), 'text/html');
                savePlaceName = placeName.body.children[0].innerHTML;
                console.log("infowindow contents: " + placeName.body.children[0].innerHTML);
            }

            console.log("위도: " + saveLat + "경도: " + saveLng);
            closeMapModal();
        } else {

        }
    });
    marker.setMap(map); // 지도 위에 마커를 표출합니다
    markers.push(marker);  // 배열에 생성된 마커를 추가합니다

    return marker;
}

function displayInfowindow(marker, title) {
    const content = '<div style="padding:3px; z-index:100; width: 220px;">' + title + '</div>';

    infoWindow.setContent(content);
    infoWindow.open(map, marker);
}

// 검색 결과 페이지네이션 함수
function displayPagination(pagination) {
    const paginationEle = $('#placePagination');
    let fragment = $(document.createDocumentFragment());
    let i;

    // 기존에 추가된 페이지 번호를 삭제
    while (paginationEle[0].hasChildNodes()) {
        paginationEle[0].removeChild(paginationEle[0].lastChild);
    }

    for (i = 1; i <= pagination.last; i++) {
        const ele = document.createElement('a');
        ele.href = '#';
        ele.innerHTML = i; // 페이지 넘버

        if (i === pagination.current) {
            ele.className = 'on';
        } else {
            ele.onclick = (function (i) {
                return function () {
                    pagination.gotoPage(i)
                };
            })(i);
        }
        fragment[0].append(ele);
    }
    paginationEle[0].append(fragment[0]);
}

// modal close 버튼 눌렀을 때 리스너
function closeMapModal() {
    $('#mapDialog').modal('hide');
    $('#mapDialog').on('hide.bs.modal', function () {
        $('#hiddenLat').val(saveLat);
        $('#hiddenLng').val(saveLng);
        if (savePlaceName !== null) {
            const mapBtn = $('#mapBtn')[0];
            const bEle = $('b.me-2')[0];
            $('#hiddenPlaceName').val(savePlaceName);
            bEle.innerHTML = savePlaceName;
        }

        map = null;
        center = null;
        infoWindow = null;
        customOverlay = null;
        $('#keyword').val('');
        const placeList = $('#placeList')[0];
        while (placeList.hasChildNodes()) {
            placeList.removeChild(placeList.lastChild);
        }

        while ($('#placePagination')[0].hasChildNodes()) {
            $('#placePagination')[0].removeChild($('#placePagination')[0].lastChild);
        }
        removeMarker();
        removeAllChildNods($('#map')[0]);
    });
}

// 모든 마커를 삭제
function removeMarker() {
    for (let i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
    }
    markers = [];
}

// 모든 자식 element를 삭제하는 함수
function removeAllChildNods(el) {
    while (el.hasChildNodes()) {
        el.removeChild(el.lastChild);
    }
}