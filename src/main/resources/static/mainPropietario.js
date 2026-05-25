// propietario.js

function showSection(sectionId, button){

    // ocultar todas las secciones
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });

    // mostrar seleccionada
    document.getElementById(sectionId).classList.add('active');

    // quitar active botones
    document.querySelectorAll('.menu-btn').forEach(btn => {
        btn.classList.remove('active');
    });

    // activar actual
    button.classList.add('active');

}