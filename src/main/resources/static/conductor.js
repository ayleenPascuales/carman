function loadSection(section){

    const content = document.getElementById("content");

    if(section === "inicio"){

        alert("Inicio");

    }

    if(section === "servicio"){

        alert("Servicio activo");

    }

    if(section === "proximos"){

        alert("Próximos servicios");

    }

    if(section === "historial"){

        alert("Historial");

    }

    if(section === "ganancias"){

        alert("Ganancias");

    }

    if(section === "calificaciones"){

        alert("Calificaciones");

    }

    if(section === "perfil"){

        alert("Perfil");

    }

    if(section === "incidencias"){

        alert("Incidencias");

    }

    if(section === "notificaciones"){

        alert("Notificaciones");

    }

}

/* ACTIVAR MENU */

const items = document.querySelectorAll(".menu li");

items.forEach(item => {

    item.addEventListener("click", () => {

        items.forEach(i => i.classList.remove("active"));

        item.classList.add("active");

    });

});