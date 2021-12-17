$('#testBtn').on('click', function () {
    $.ajax({
        method: 'GET',
        url: 'http://localhost:8000/hello',
        contentType: 'text/html; charset=utf-8',
        dataType: 'json',
        success: function (response) {
            console.log(response);
            alert(response);
        },
        error: function (xhr, error) {
            console.log(xhr);
            alert(`xhr=${xhr}, error=${error}`);
        }
    });
});