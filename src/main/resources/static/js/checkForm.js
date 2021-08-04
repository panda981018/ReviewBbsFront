function checkForm() {

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