let myChart; // chart 객체를 가리킬 변수
const CTX = document.getElementById('chart').getContext('2d');

function setDate() { // 현재 기준 연도와 월
    const now = new Date();
    const year = now.getFullYear();
    const month = now.getMonth() + 1;
    $('#yearSelect').val(year).prop('selected', true);
    $('#monthSelect').val(month).prop('selected', true);

    makeChart(year, month);
}

$('#monthSelect').on('change', function () {
    const year = $('#yearSelect option:selected').val();
    const month = $('#monthSelect option:selected').val();

    makeChart(year, month);
});

function makeChart(year, month) {
    if (myChart !== undefined) {
        myChart.destroy();
    }

    $.ajax({
        method: 'GET',
        url: '/api/getData?year=' + year + '&month=' + month,
        dataType: "json",
        success: function (result) {

            let labels = new Set(); // 데이터가 있는 모든 날짜들을 담을 set 객체
            let dataSets = [];

            const batchResults = new Map(Object.entries(result));
            const categoryList = Object.keys(result);
            const length = categoryList.length;

            for (let i = 0; i < length; i++) {
                let data = {};
                const categoryName = categoryList.pop();
                let mapValue = batchResults.get(categoryName); // [날짜, 카운트]의 배열

                let label = []; // x 축에 대한 배열
                let counts = []; // 카운트에 대한 배열
                for (let j = 0; j < mapValue.length; j++) {
                    const arr = mapValue[j];
                    label.push(arr[0]);
                    labels.add(arr[0]); // 나올 수 있는 모든 날짜 데이터 추가
                    counts.push(arr[1]);
                }

                data.label = categoryName; // 라인의 이름(항목)
                data.backgroundColor = '#' + Math.round(Math.random() * 0xffffff).toString(16);
                data.borderColor = data.backgroundColor;
                data.data = counts;
                dataSets.push(data);
            }

            const data = {
                labels: Array.from(labels),
                datasets: dataSets
            };

            const config = {
                type: 'line',
                data: data,
                options: {
                    scales: {
                        x: {
                            display: true,
                            title: {
                                display: true,
                                text: 'Date',
                                color: 'rgb(0, 0, 0)',
                                padding: {top: 20, left: 0, right: 0, bottom: 15},
                                font: {
                                    family: 'Gothic',
                                    size: 20,
                                    weight: 'bold',
                                    lineHeight: 1.2
                                }
                            }
                        },
                        y: {
                            suggestedMin: 0,
                            ticks: {
                                stepSize: 1
                            },
                            display: true,
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: 'Count',
                                color: 'rgb(0, 0, 0)',
                                margin: {top: 30, left: 0, right: 0, bottom: 0},
                                font: {
                                    family: 'Gothic',
                                    size: 20,
                                    weight: 'bold',
                                    lineHeight: 1.2
                                }
                            }
                        }
                    }
                }
            };

            myChart = new Chart(
                CTX,
                config
            );
        }
    });
}