// role 선택에 따른 양식 변화
function changeSignUpForm() {

    document.getElementById('roleDiv').addEventListener('change', function (e) {
        let target = e.target;
        const birth = document.getElementById('birthDiv');
        const gender = document.getElementById('genderDiv');

        switch (target.id) {
            case 'member' :
                birth.style.display = 'block';
                gender.style.display = 'block';
                break;
            case 'admin' :
                birth.style.display = 'none';
                gender.style.display = 'none';
                break;
        }
    })
}

// birth validate
function birthValidate() {
    document.getElementById('birth').addEventListener('focusout', () => {
        const year = document.getElementById('year');
        const month = document.getElementById('month');
        const dayOfMonth = document.getElementById('day');
        const birthError = document.getElementById('birthError');

        let selectedYear = year.options[year.selectedIndex].value;
        let selectedMonth = month.options[month.selectedIndex].value;
        let selectedDayOfMonth = dayOfMonth.options[dayOfMonth.selectedIndex].value;

        if (selectedYear == ' ' || selectedMonth == ' ' || selectedDayOfMonth == ' ') {
            birthError.classList.add('alert', 'alert-danger');
            birthError.innerText = '생년월일을 모두 설정해주세요';
        } else {
            birthError.innerText = '';
            birthError.classList.remove('alert', 'alert-danger');
        }
    })
}

function validateForm() {

    const email = document.signUpForm.username;
    const emailError = document.getElementById('emailError');

    const password = document.signUpForm.password;
    const pwError = document.getElementById('passwordError');

    const nickname = document.signUpForm.nickname;
    const nicknameError = document.getElementById('nicknameError');

    if (email.value == '') {
        email.focus();
        emailError.style.display = 'block';
        emailError.innerText = '이메일은 필수 입력사항입니다.';
    } else {
        emailError.style.display = 'none';
    }

    if (password.value == '') {
        password.focus();
        pwError.style.display = 'block';
        pwError.innerText = '비밀번호는 필수 입력사항입니다.';
    } else if (password.value.length > 0 && password.value.length < 8) {
        pwError.style.display = 'block';
        pwError.innerText = '비밀번호는 8자 이상으로 입력해주세요.';
    } else {
        pwError.style.display = 'none';
    }

    if (nickname.value == '') {
        nickname.focus();
        nicknameError.style.display = 'block';
        nicknameError.innerText = '닉네임은 필수 입력사항입니다.';
        return false;
    } else {
        nicknameError.style.display = 'none';
    }

    return true;
}

window.onload = function () {
    birthValidate();
    changeSignUpForm();
}