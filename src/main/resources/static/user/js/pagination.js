function Pagination({ selector, totalItem, itemPerPage, currentPage, pageParam }) {
	const pagination = document.querySelector(selector);
	const totalPage = Math.ceil(totalItem / itemPerPage);


	const buttons = []
	if (totalPage <= 5) {
		for (let i = 1; i <= totalPage; i++) {
			buttons.push(i)
		}
	} else {
		if (currentPage <= 3) {
			buttons.push(1, 2, 3, 4, 0, totalPage)
		} else if (currentPage >= totalPage - 2) {
			buttons.push(1, 0, totalPage - 3, totalPage - 2, totalPage - 1, totalPage)
		} else {
			buttons.push(1, 0, currentPage - 1, currentPage, currentPage + 1, 0, totalPage)
		}
	}

	const pageList = document.createElement("ul");
	pageList.classList.add("page-list")

	const url = new URL(window.location);
	buttons.forEach(button => {
		if (button === 0) {
			pageList.innerHTML += `<li class="page-item noactive"><span>...</span></li>`
		} else {
			url.searchParams.set(pageParam, button + "");
			const urlResult = url.toString();

			pageList.innerHTML += `<li class='page-item${currentPage === button ? ' active' : ''}'><a href="${urlResult}">${button}</a></li>`
		}
	})

	pagination.append(pageList)
}