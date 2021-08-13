function askAdmin(adminId) {
    let result = prompt('비밀번호를 입력해주세요');

    $.ajax({
        type: 'POST',
        data: { 'username' : adminId, 'password' : result },
        dataType: "json",
        url: '/check/admin',
        success: function (checkResult) {
            if (checkResult.result !== true) {
                console.log(checkResult.result);
                window.history.back();
                alert('비밀번호가 틀렸습니다!');
            }
        }
    })
}