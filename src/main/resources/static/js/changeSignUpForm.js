function changeSignUpForm() {

    document.body.addEventListener('change', function (e) {
        let target = e.target;
        const age = document.getElementById('ageDiv');
        const gender = document.getElementById('genderDiv');

        switch(target.id) {
            case 'member' :
                age.style.display = 'block';
                gender.style.display = 'block';
                break;
            case 'admin' :
                age.style.display = 'none';
                gender.style.display = 'none';
                break;
        }
    })
}