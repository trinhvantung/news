
const buttonReply = document.getElementsByClassName("comment__reply__button");
const comment = document.getElementsByClassName("comment")

const formReplyCommnet = `
    <form id="form-reply-comment">
        <textarea name="" id="" rows="2"></textarea>
        <div class="d-flex">
            <button type="button" class="btn-reply-submit">Trả lời</button>
            <button type="button" class="btn ml-2 button-close-reply-comment">Đóng</button>
        </div>
    </form>
    `

for (let i = 0; i < buttonReply.length; i++) {
    buttonReply[i].addEventListener("click", function () {
        for (let j = 0; j < comment.length; j++) {
            comment[j].getElementsByClassName("form-reply-comment")[0].innerHTML = ""
        }

        let element = document.createElement("div");
        element.innerHTML = formReplyCommnet;
        const buttonCloseReplyComment = element.getElementsByClassName("button-close-reply-comment")[0]
        buttonCloseReplyComment.addEventListener("click", function () {
            this.closest(".form-reply-comment").innerHTML = ""
        })


        this.closest(".comment").getElementsByClassName("form-reply-comment")[0].append(element)

    })
}