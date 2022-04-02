function goTo(url) {
    window.location = url
}

function appendToObject(src, name, value) {
    src[name] = value;
}

function loginRequest(url, afterLoginUrl, inputs) {
    let data = {}
    let inputNodes = new Map()
    inputs.forEach(input => inputNodes.set(input, document.getElementById(input)))
    inputNodes.forEach((inputNode, index) => {
        appendToObject(data, inputNode.getAttribute('name'), inputNode.value)
    })
    fetch(url, {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        window.location = afterLoginUrl
    })
}

function formAjaxQuery(formId) {
    const form = document.getElementById(formId)
    const $form = $('#' + formId)

    form.onsubmit = function (event) {
        event.preventDefault()

        const url = form.getAttribute("action") + "?" + $form.serialize()
        fetch(url, {
            method: 'POST',
            headers: {
                'Accept': 'text/html'
            }
        })
        .then((response) => {
            response
                .text()
                .then(txt => $form.replaceWith(txt))
        })
    }
}

function createToast(toast) {
    let div = document.createElement('div')
    div.innerHTML = toast
    document.body.append(div)
}

function toastAjaxQuery(url, method) {
    fetch(url, {
        method: method,
        headers: {
            'Accept': 'text/html'
        }
    })
    .then((response) => {
        response
            .text()
            .then(txt => createToast(txt))
    })
}

function replaceAjaxQuery(url, elementToReplace, method) {
    const target = $('#' + elementToReplace)

    fetch(url, {
        method: method,
        headers: {
            'Accept': 'text/html'
        }
    })
    .then((response) => {
        response
            .text()
            .then(txt => target.replaceWith(txt))
    })
}