document.addEventListener("DOMContentLoaded",() => {
    changeTheme();
});


function setTheme(theme){
    localStorage.setItem("theme",theme);
}

function getTheme(){
    let theme = localStorage.getItem("theme");
    if(theme) return theme;
    return "light";
}

let currentTheme=getTheme();

function changeTheme(){
    setTheme(currentTheme);
    document.getElementById('theme-change-button').querySelector('span').textContent=(currentTheme=="light")?"Dark":"Light";
    document.querySelector('html').classList.add(currentTheme);
    const changeThemeButton = document.getElementById('theme-change-button');
    changeThemeButton.addEventListener("click", (event) => {
        const oldTheme = currentTheme;
        currentTheme = (currentTheme=="light")?"dark":"light";
        setTheme(currentTheme);
        document.querySelector('html').classList.remove(oldTheme);
        document.querySelector('html').classList.add(currentTheme);
        changeThemeButton.querySelector('span').textContent=(currentTheme=="light")?"Dark":"Light";
    })
}