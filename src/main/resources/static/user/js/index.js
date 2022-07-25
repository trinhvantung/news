const iconSearch = document.getElementsByClassName("header__icon__search");
const headerSearch = document.getElementsByClassName("header__search")[0];

for (let i = 0; i < iconSearch.length; i++) {
	iconSearch[i].addEventListener("click", () => {
		headerSearch.classList.toggle("active")
	})
}




const menu = document.getElementsByClassName("menu")[0];
const buttonMenu = document.getElementsByClassName("button-menu")[0];
const menuOverlay = document.getElementsByClassName("menu__overlay")[0];
const buttonCloseMenu = document.getElementsByClassName("menu__close")[0];

buttonMenu.addEventListener("click", function() {
	menu.classList.toggle("active");
})

menuOverlay.addEventListener("click", function() {
	menu.classList.toggle("active")
})

buttonCloseMenu.addEventListener("click", function() {
	menu.classList.toggle("active")
})


const buttonDropdownAccount = document.getElementById("button-drop-down-account");
const dropdownAccount = document.getElementById("drop-down-account");

buttonDropdownAccount.addEventListener("click", function() {
	dropdownAccount.classList.toggle("active");
})

const queryInputHeader = document.querySelector("#form-search-header>input");
const buttonSearchHeader = document.querySelector("#form-search-header>button");

queryInputHeader.addEventListener("keyup", function() {
	if (queryInputHeader.value.length < 2) {
		buttonSearchHeader.disabled = true;
	} else {
		buttonSearchHeader.disabled = false;
	}
})

const btnDropdownButton = document.getElementsByClassName("drop-down-button");
if (btnDropdownButton) {
	for (const btn of btnDropdownButton) {
		btn.addEventListener("click", function() {
			const btnDropdownMenu = this.closest(".dropdown").querySelector(".drop-down-menu");
			btnDropdownMenu.classList.toggle("active");
		})
	}
}

