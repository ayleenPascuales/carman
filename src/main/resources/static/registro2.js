

const steps = document.querySelectorAll(".form-step");
let rolSeleccionado = "cliente";
const formRegistro = document.querySelector(".register-box form");
const roleStepIds = ["step2-cliente", "step2-propietario", "step2-conductor"];

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

// Mantener sincronizado el rol por defecto con el input oculto del form.
document.getElementById("tipoUsuario").value = rolSeleccionado;

function syncRoleFields() {
    roleStepIds.forEach(stepId => {
        const step = document.getElementById(stepId);
        if (!step) return;

        const isSelectedRoleStep = stepId === `step2-${rolSeleccionado}`;
        const fields = step.querySelectorAll("input, select, textarea");
        fields.forEach(field => {
            field.disabled = !isSelectedRoleStep;
        });
    });
}

// El registro exitoso redirige a /buscar con sesión iniciada.

syncRoleFields();

if (formRegistro) {
    formRegistro.addEventListener("submit", function (event) {
        syncRoleFields();
        if (!formRegistro.checkValidity()) {
            event.preventDefault();
            formRegistro.reportValidity();
        }
    });
}

function mostrarFormularioRol() {

    steps.forEach(step => {
        step.classList.remove("active");
    });

    syncRoleFields();

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

    syncRoleFields();

    const formulario = document.getElementById(idFormulario);
    const inputs = formulario.querySelectorAll("input, select, textarea");

    for (let input of inputs) {
        if (input.disabled) {
            continue;
        }
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
    syncRoleFields();
}