let center; // 중심점
let map; // 지도
let infoWindow; // 정보창
let customOverlay; // 현재 위치를 표시하기 위한 overlay
let saveLat; // 저장할 위도
let saveLng; //  저장할 경도
let savePlaceName; // 저장할 장소 이름
let currPositionOnAndOff = true; // 현재 위치 버튼 on/off 여부

let markers = []; // 마커를 담을 배열

$('#findCurrentLocation').on('click', function () { // 현재 위치로 이동하게 하는 이벤트 리스너
    if (currPositionOnAndOff) { // 현재 위치 버튼이 켜져있는걸 off 할 생각
        currPositionOnAndOff = false;
        $('#currentImg').css('filter', 'opacity(0.5) drop-shadow(0 0 0 lightgray)');
    } else {
        $('#currentImg').css('filter', 'opacity(0.5) drop-shadow(0 0 0 #0000FFFF)');
    }
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
    map = null;
    center = null;
    infoWindow = null;
    customOverlay = null;

    center = new kakao.maps.LatLng(lat, lng);
    const options = { // map 옵션
        center: center
    };

    map = new kakao.maps.Map($('#map')[0], options); // 지도 생성 및 객체 리턴턴
    $('#currentImg').css('filter', 'opacity(0.5) drop-shadow(0 0 0 #0000FFFF)'); // 현재 위치를 받는 중

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
            map.relayout();
            map.panTo(center); // 부드럽게 중심이동
        }, 500); // sleep(500)
    } else {
        setTimeout(() => {
            const marker = savedPositionMarker(center);
            map.relayout();
            map.panTo(center);
        }, 500);
    }
}

// 현재 위치의 정보를 가져온다
function getCurrentLocation() {
    if (navigator.geolocation) { // geolocation을 사용할 수 있다면
        navigator.geolocation.getCurrentPosition(function (position) { // 현재 위치를 받는 중
            $('#currentImg').css('filter', 'opacity(0.5) drop-shadow(0 0 0 #0000FFFF)');
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

            kakao.maps.event.addListener(map, 'dragStart', function () {
                if (customOverlay !== null) {
                    customOverlay.setMap(null);
                }
                currPositionOnAndOff = false;
                $('#currentImg').css('filter', 'opacity(0.5) drop-shadow(0 0 0 lightgray)');
            });
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
    searchObject.keywordSearch(keyword, searchSuccess, {// 검색 option
        location: location, // 현재 맵의 center 기준
        radius: 8000, // 검색 반경 설정
        sort: kakao.maps.services.SortBy.DISTANCE // 거리순 정렬
    });
}

// 키워드 검색 완료 시 호출되는 콜백함수
function searchSuccess(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {
        if (customOverlay !== null) {
            customOverlay.setMap(null);
            $('#currentImg').css('filter', 'lightgray');
        }
        $('#searchResult').css('display', 'block');
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

    removeAllChildNodes(listEle);
    removeMarker();

    for (let i = 0; i < places.length; i++) {
        // 마커를 생성하고 지도에 표시
        let placePosition = new kakao.maps.LatLng(places[i].y, places[i].x);
        let marker = addMarker(placePosition, i);
        let itemEle = getListItem(i, places[i]); // 검색 결과 항목 element 를 생성함.

        bounds.extend(placePosition); // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기 위해
        (function (marker, title, position) {
            kakao.maps.event.addListener(marker, 'mouseover', function () { // mouseover 했을 때
                map.panTo(placePosition); // 해당 포지션으로 이동
                displayInfoWindow(marker, title);
            });

            kakao.maps.event.addListener(marker, 'mouseout', function () { // mouseout을 했을 때
                infoWindow.close();
            });

            itemEle[0].onmouseover = function () {
                map.panTo(placePosition);
                displayInfoWindow(marker, title);
            }

            itemEle[0].onmouseout = function () {
                infoWindow.close();
            }

            itemEle[0].onclick = function () {
                displayInfoWindow(marker, title);
                savePosition(marker);
            }
        })(marker, places[i].place_name, placePosition);

        fragment[0].append(itemEle[0]);
    }
    // 검색결과 항목들을 검색결과 목록 element에 추가함.
    listEle.append(fragment[0]);
    menuEle.scrollTop();
    // 검색된 장소 위치를 기준으로 지도 범위를 재설정.
    map.setBounds(bounds);
}

// 해당 위치를 저장
function savePosition(marker) {
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
    }
}

// 검색결과 항목을 Element로 반환하는 함수입니다
function getListItem(index, places) {

    let el = $('<li>', {});
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
function addMarker(position, idx) {
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
        savePosition(marker);
    });
    marker.setMap(map); // 지도 위에 마커를 표출합니다
    markers.push(marker);  // 배열에 생성된 마커를 추가합니다

    return marker;
}

function displayInfoWindow(marker, title) {
    const content = '<div style="padding:3px; z-index:100;">' + title + '</div>';

    infoWindow.setContent(content);
    infoWindow.open(map, marker);
}

// 검색 결과 페이지네이션 함수
function displayPagination(pagination) {
    const paginationEle = $('#placePagination');
    let fragment = $(document.createDocumentFragment());
    let i;

    // 기존에 추가된 페이지 번호를 삭제
    removeAllChildNodes(paginationEle[0]);

    for (i = 1; i <= pagination.last; i++) {
        const ele = document.createElement('button');
        ele.type = 'button';
        ele.innerHTML = i; // 페이지 넘버
        ele.classList.add('page-btn');

        if (i === pagination.current) {
            ele.classList.add('on');
        } else {
            ele.onclick = (function (i) {
                return function () {
                    pagination.gotoPage(i);

                }
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
        if (savePlaceName != undefined) {
            const mapBtn = $('#mapBtn')[0];
            const bEle = $('b.me-2')[0];
            $('#hiddenLat').val(saveLat);
            $('#hiddenLng').val(saveLng);
            $('#hiddenPlaceName').val(savePlaceName);
            bEle.innerHTML = savePlaceName;
        }
        $('#keyword').val('');
        const placeList = $('#placeList')[0];
        if (placeList != undefined) {
            while (placeList.hasChildNodes()) {
                placeList.removeChild(placeList.lastChild);
            }
            while ($('#placePagination')[0].hasChildNodes()) {
                $('#placePagination')[0].removeChild($('#placePagination')[0].lastChild);
            }
        }
        removeMarker();
        removeAllChildNodes($('#map')[0]);
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
function removeAllChildNodes(el) {
    while (el.hasChildNodes()) {
        el.removeChild(el.lastChild);
    }
}