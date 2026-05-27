

const steps = document.querySelectorAll(".form-step");
let rolSeleccionado = "cliente";

function nextStep(stepNumber) {

    steps.forEach(step => {
        step.classList.remove("active");
    });

    document
        .getElementById(`step${stepNumber}`)
        .classList.add("active");

}

function prevStep(stepNumber) {

    steps.forEach(step => {
        step.classList.remove("active");
    });

    document
        .getElementById(`step${stepNumber}`)
        .classList.add("active");

}

document
    .getElementById("step1")
    .classList.add("active");

function mostrarFormularioRol() {

    steps.forEach(step => {
        step.classList.remove("active");
    });

    if (rolSeleccionado === "cliente") {
        document.getElementById("step2-cliente")
            .classList.add("active");
    }

    else if (rolSeleccionado === "propietario") {
        document.getElementById("step2-propietario")
            .classList.add("active");
    }

    else if (rolSeleccionado === "conductor") {
        document.getElementById("step2-conductor")
            .classList.add("active");
    }
}

function volverFormularioRol() {

    steps.forEach(step => {
        step.classList.remove("active");
    });

    if (rolSeleccionado === "cliente") {
        document.getElementById("step2-cliente")
            .classList.add("active");
    }

    else if (rolSeleccionado === "propietario") {
        document.getElementById("step2-propietario")
            .classList.add("active");
    }

    else if (rolSeleccionado === "conductor") {
        document.getElementById("step2-conductor")
            .classList.add("active");
    }
}

let archivosGuardados = {};

function mostrarFotos(inputId, textoId, previewId) {

    const input = document.getElementById(inputId);
    const texto = document.getElementById(textoId);
    const preview = document.getElementById(previewId);
    const nuevosArchivos = Array.from(input.files);

    if (!archivosGuardados[inputId]) {
        archivosGuardados[inputId] = [];
    }
    archivosGuardados[inputId] =
        archivosGuardados[inputId].concat(nuevosArchivos);
    renderizarFotos(inputId, textoId, previewId);

}

function renderizarFotos(inputId, textoId, previewId) {
    const texto = document.getElementById(textoId);
    const preview = document.getElementById(previewId);
    preview.innerHTML = "";
    texto.textContent =
        archivosGuardados[inputId].length +
        " archivo(s) seleccionado(s)";
    archivosGuardados[inputId].forEach((archivo, index) => {
        const reader = new FileReader();
        reader.onload = function (e) {
            const container =
                document.createElement("div");
            container.classList.add("preview-item");
            const img =
                document.createElement("img");
            img.src = e.target.result;
            img.classList.add("preview-img");
            const btn =
                document.createElement("button");
            btn.innerHTML = "✖";
            btn.classList.add("delete-btn");
            btn.onclick = function () {
                archivosGuardados[inputId].splice(index, 1);
                renderizarFotos(inputId, textoId, previewId);
            };
            container.appendChild(img);
            container.appendChild(btn);
            preview.appendChild(container);
        };
        reader.readAsDataURL(archivo);
    });
}

function validarFormulario(idFormulario) {

    const formulario = document.getElementById(idFormulario);
    const inputs = formulario.querySelectorAll("input, select");

    for (let input of inputs) {
        if (input.type === "file" && input.files.length === 0) {

            alert("Debes subir un archivo");
            return;
        }
        if (!input.checkValidity()) {

            input.reportValidity();
            return;
        }
    }
    if (idFormulario === "step3") {
        nextStep(4);
    } else {
        nextStep(3);
    }
}

function seleccionarRol(card, tipo) {

    document.querySelectorAll(".option-card")
        .forEach(c => c.classList.remove("active"));

    card.classList.add("active");

    rolSeleccionado = tipo;
    document.getElementById("tipoUsuario").value = tipo;
}