// role 선택에 따른 양식 변화
function changeSignUpForm() {

    document.getElementById('roleDiv').addEventListener('change', function (e) {
        let target = e.target;
        const birthDiv = document.getElementById('birthDiv');
        const genderDiv = document.getElementById('genderDiv');

        switch (target.id) {
            case 'member' :
                if (birthDiv.classList.contains('valid') && genderDiv.classList.contains('valid')) {
                    birthDiv.classList.remove('valid');
                    genderDiv.classList.remove('valid');
                }
                break;
            case 'admin' :
                birthDiv.classList.add('valid');
                genderDiv.classList.add('valid');
                break;
        }
    })
}

// email validate
function emailValidate() {
    const email = document.getElementById('signUpEmail');
    const emailError = document.getElementById('emailError');

    email.addEventListener('focusout', () => {
        if (email.value == '') {
            if (emailError.classList.contains('valid')) {
                emailError.classList.remove('valid');
            }
            emailError.classList.add('error');
            emailError.innerText = '이메일은 필수 입력사항입니다.';
        } else if (email.value.indexOf('@') == -1
            || email.value.indexOf('.') == -1) {
            if (emailError.classList.contains('valid')) {
                emailError.classList.remove('valid');
            }
            emailError.classList.add('error');
            emailError.innerText = '이메일 형식을 지켜주십시오.';
        } else {
            if (emailError.classList.contains('error')) {
                emailError.classList.remove('error');
            }
            emailError.innerText = '';
            emailError.classList.add('valid');
        }
    })
}

function passwordValidate(view) {
    const password = document.getElementById('signUpPassword');
    const passwordError = document.getElementById('passwordError');

    password.addEventListener('focusout', () => {
        if (view != 'myInfo') {
            if (password.value == '') {
                if (passwordError.classList.contains('valid')) {
                    passwordError.classList.remove('valid');
                }
                passwordError.classList.add('error');
                passwordError.innerText = '비밀번호는 필수 입력사항입니다.';
            }
        }
        if (password.value.length > 0 && password.value.length < 8) {
            if (passwordError.classList.contains('valid')) {
                passwordError.classList.remove('valid');
            }
            passwordError.classList.add('error');
            passwordError.innerText = '비밀번호는 8자 이상으로 입력해주세요.';
        } else {
            passwordError.classList.remove('error');
            passwordError.classList.add('valid');
        }
    })
}

// nickname validate 성공
function nicknameValidate() {
    let nickname = document.getElementById('nickname');
    let nicknameError = document.getElementById('nicknameError');

    nickname.addEventListener('focusout', () => {
        if (nickname.value == '') { // 비어있으면!
            if (nicknameError.classList.contains('valid')) {
                nicknameError.classList.remove('valid');
            }
            nicknameError.classList.add('error');
            nicknameError.innerText = '닉네임은 필수 입력사항입니다.';
        } else if (nickname.value.length < 5) {
            if (nicknameError.classList.contains('valid')) {
                nicknameError.classList.remove('valid');
            }
            nicknameError.classList.add('error');
            nicknameError.innerText = '닉네임은 5자 이상으로 해주세요';
        } else {
            if (nicknameError.classList.contains('error')) {
                nicknameError.innerText = '';
                nicknameError.classList.remove('error');
                nicknameError.classList.add('valid');
            } else {
                nicknameError.innerText = '';
                nicknameError.classList.add('valid');
            }
        }
    })
}

// birth validate 성공
function birthValidate() {
    document.getElementById('birth').addEventListener('focusout', () => {
        let year = document.getElementById('selectYear');
        let month = document.getElementById('selectMonth');
        let dayOfMonth = document.getElementById('selectDay');
        let birthError = document.getElementById('birthError');

        let selectedYear = year.options[year.selectedIndex].value;
        let selectedMonth = month.options[month.selectedIndex].value;
        let selectedDayOfMonth = dayOfMonth.options[dayOfMonth.selectedIndex].value;

        if (selectedYear != '' && selectedMonth != '' && selectedDayOfMonth != '') { // 생년월일을 잘 선택한 경우
            birthError.innerText = '';
            if (birthError.classList.contains('error')) {
                birthError.classList.remove('error');
            }
            birthError.classList.toggle('valid');
        } else { // 하나라도 제대로 선택하지 않은 경우
            if (birthError.classList.contains('valid')) {
                birthError.classList.remove('valid');
            }
            birthError.classList.add('alert', 'alert-danger', 'error');
            birthError.innerText = '생년월일을 모두 설정해주세요';
        }
    })
}

function validateForm() {

    const emailError = document.getElementById('emailError');
    const pwError = document.getElementById('passwordError');
    const nicknameError = document.getElementById('nicknameError');
    const birthError = document.getElementById('birthError');

    console.log('email error: ' + emailError.classList.contains('error'));
    console.log('password error: ' + pwError.classList.contains('error'));
    console.log('nickname error: ' + nicknameError.classList.contains('error'));
    console.log('birth error: ' + birthError.classList.contains('error'));

    // email validator
    if (emailError.classList.contains('error')
        || pwError.classList.contains('error')
        || nicknameError.classList.contains('error')
        || birthError.classList.contains('error')) {
        return false;
    } else {
        return true;
    }
}

