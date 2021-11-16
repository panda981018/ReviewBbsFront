$('#categorySelect').on('change', function () {
    const category = $('#categorySelect option:selected').val();
    $('#chart').empty();
    if (category != null || category != undefined) {
        initChart(category);
    }
});

function initChart(category) {
    const ctx = document.getElementById('chart').getContext('2d');
    $.ajax({
        method: 'GET',
        url: '/api/getData?category=' + category,
        contentType: 'string',
        success: function () {
            console.log("call BatchApiController");
        }
    });

    const labels = [
        'January',
        'February',
        'March',
        'April',
        'May',
        'June'
    ];
    const data = {
        labels: labels,
        datasets: [{
            label: 'My First dataset',
            backgroundColor: 'rgb(255, 99, 132)',
            borderColor: 'rgb(255, 99, 132)',
            data: [0, 10, 5, 2, 20, 30, 45],
        }]
    };
    const config = {
        type: 'line',
        data: data,
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    };

    const myChart = new Chart(
        ctx,
        config
    );
}