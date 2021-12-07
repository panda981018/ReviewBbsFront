$('#cancelBtn').on('click', function () {
    window.history.back();
});
$('input').on('keydown', function (e) {
    if (e.keyCode == 13) {
        e.preventDefault();
    }
});

// role 선택에 따른 양식 변화
function changeSignUpForm() {

    $('#roleDiv').on('change', function (e) {
        let target = e.target;
        const birthDiv = $('#birthDiv');
        const genderDiv = $('#genderDiv');

        switch (target.id) {
            case 'member' :
                if (birthDiv.hasClass('valid') && genderDiv.hasClass('valid')) {
                    birthDiv.removeClass('valid');
                    genderDiv.removeClass('valid');
                }
                break;
            case 'admin' :
                birthDiv.addClass('valid');
                genderDiv.addClass('valid');
                break;
        }
    })
}

// email validate
function emailValidate() {
    $('#signUpEmail').on('focusout', function () {
        const username = $('#signUpEmail').val();
        const emailError = $('#emailError');
        if (username == '') { // 비어있을 때
            emailError.removeClass('valid');
            emailError.addClass('error');
            emailError.text('이메일은 필수 항목입니다.');
        } else if (username.indexOf('@') == -1
            || username.indexOf('.') == -1) { // @ 혹은 .이 없을 때
            emailError.removeClass('valid');
            emailError.addClass('error');
            emailError.text('이메일 형식을 지켜주십시오.');
        } else { // 비어있지도 않고 이메일 형식일 경우
            $.ajax({
                type: "POST",
                url: "/api/check/email",
                data: JSON.stringify({"username": username}),
                contentType: 'application/json;charset=utf-8;',
                dataType: "json",
                success: function (result) {
                    console.log(result);
                    if (result.result === true) { // 존재한다면
                        emailError.removeClass('valid');
                        emailError.addClass('error');
                        emailError.text('중복된 이메일입니다. 다른 이메일을 입력해주세요');
                    } else { // 존재하지 않는다면
                        emailError.removeClass('error');
                        emailError.text('');
                        emailError.addClass('valid');
                    }
                },
                error: function () {
                    alert("오류가 발생했습니다.");
                }
            });
        } // else 끝
    }); // 이벤트 리스너 함수 끝
}

function passwordValidate(view) {
    const passwordObj = $('#signUpPassword');
    const passwordError = $('#passwordError');

    const reg = /(?=.*[^a-zA-Z0-9\s])(?=.*[a-zA-Z])(?=.*[0-9]).{8,20}/g;

    passwordObj.on('focusout', function () {
        const password = passwordObj.val().toString();

        passwordObj.removeAttr('data-bs-toggle');
        passwordObj.removeAttr('title');
        passwordObj.removeAttr('data-placement');
        passwordObj.removeAttr('data-html');
        passwordObj.removeAttr('onmouseover');

        if (view !== 'myInfo') {
            if (password === '') {
                passwordError.removeClass('valid');
                passwordError.addClass('error');
                passwordError.text('비밀번호는 필수 입력사항입니다.');
            }
        }

        if (reg.test(password)) { // 정규식 통과
            passwordError.removeClass('error');
            passwordError.text('');
            passwordError.addClass('valid');
        } else {
            passwordError.removeClass('valid');
            passwordError.text('비밀번호는 8자 이상 20자 이하로 입력해주세요.');
            passwordError.addClass('error');
        }
    })
}

function passwordTooltip() {
    const passwordObj = $('#signUpPassword');

    passwordObj.on('focusin', function () {
        passwordObj.attr('data-bs-toggle', 'tooltip');
        passwordObj.attr('title', '<b>비밀번호 설정 조건</b><br>' +
            '<small>영문, 숫자, 특수문자를 최소 1개 이상 포함</small><br>' +
            '<small>8자 이상 20자 이하</small>');
        passwordObj.attr('onmouseover', "title=''");
        passwordObj.tooltip({
            animation: true,
            html: true,
            template: "<div role='tooltip'><div class='tooltip-inner'></div></div>",
            placement: 'bottom',
            trigger: 'focus'
        });

    })
}

// nickname validate 성공
function nicknameValidate(view) {

    $('#nickname').on('focusout', function () {
        let id = $('#hiddenId').val();
        const nickname = $('#nickname').val();
        const nicknameError = $('#nicknameError');

        if (id == null) {
            id = 'null';
        }

        if (nickname == '') {
            nicknameError.removeClass('valid');
            nicknameError.addClass('error');
            nicknameError.text('닉네임은 필수 항목입니다.');
        } else if (nickname.length < 4) {
            nicknameError.removeClass('valid');
            nicknameError.addClass('error');
            nicknameError.text('닉네임은 4자 이상으로 해주세요.');
        } else {
            $.ajax({
                type: 'POST',
                url: '/api/check/nickname',
                data: JSON.stringify({ "id" : id, "nickname" : nickname, "view" : view }),
                contentType: "application/json; charset=utf-8;",
                dataType: 'json',
                success: function (result) {
                    console.log(result);
                    if (result.result === true) {
                        nicknameError.removeClass('valid');
                        nicknameError.addClass('error');
                        nicknameError.text('중복된 닉네임입니다. 다른 닉네임을 입력해주세요.');
                    } else {
                        nicknameError.removeClass('error');
                        nicknameError.text('');
                        nicknameError.addClass('valid');
                    }
                },
                error: function () {
                    alert("오류가 발생했습니다.");
                }
            })
        } // else 끝
    }) // 이벤트 리스너 끝
}

// birth validate 성공
function birthValidate() {
    let birthError = $('#birthError');

    $('#birth').on('focusout', function () {
        let selectedYear = $('#selectYear option:selected').val();
        let selectedMonth = $('#selectMonth option:selected').val();
        let selectedDayOfMonth = $('#selectDay option:selected').val();

        if (selectedYear != '' && selectedMonth != '' && selectedDayOfMonth != '') { // 생년월일을 잘 선택한 경우
            switch(selectedMonth) {
                case '02':
                    if (selectedDayOfMonth > 28) {
                        birthError.removeClass('valid');
                        birthError.text('2월은 28일까지입니다. 다시 선택해주세요.');
                        birthError.addClass('error');
                    } else {
                        birthError.text('');
                        birthError.removeClass('error');
                        birthError.addClass('valid');
                    }
                    break;
                case '04':
                case '06':
                case '09':
                case '11':
                    if (selectedDayOfMonth > 30) {
                        birthError.removeClass('valid');
                        birthError.addClass('error');
                        birthError.text(selectedMonth + '월은 30일까지입니다. 다시 선택해주세요.');
                    } else {
                        birthError.removeClass('error');
                        birthError.text('');
                        birthError.addClass('valid');
                    }
                    break;
                default:
                    birthError.text('');
                    birthError.removeClass('error');
                    birthError.addClass('valid');
                    break;
            }
        } else { // 하나라도 제대로 선택하지 않은 경우
            birthError.removeClass('valid');
            birthError.addClass('alert alert-danger error');
            birthError.text('생년월일을 모두 설정해주세요');
        }
    })
}
let submitCallback = function (event) {
    event.preventDefault();

    const emailError = $('#emailError');
    const pwError = $('#passwordError');
    const nicknameError = $('#nicknameError');
    const birthError = $('#birthError');

    if (emailError.hasClass('error')
        || pwError.hasClass('error')
        || nicknameError.hasClass('error')
        || birthError.hasClass('error')) {
        return false;
    } else {
        $('form').eq(0).submit();
    }
}
function validateForm() {
    $('button :submit').on('submit', submitCallback);
}

