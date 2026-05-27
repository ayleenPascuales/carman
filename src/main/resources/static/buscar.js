let vehiculos = [];
let sliderIndex = 0;
let sliderImagenes = [];
let vehiculoModalActual = null;

const carsGrid = document.getElementById("carsGrid");
const modalReserveBtn = document.getElementById("modal-reserve-btn");

const ROL_UI = {
    CLIENTE: {
        titulo: "Modo cliente",
        desc: "Explora vehículos disponibles y reserva el que prefieras.",
        cta: "Ir a mis reservas",
        panel: "/cliente",
        puedeReservar: true
    },
    CONDUCTOR: {
        titulo: "Modo conductor",
        desc: "Consulta el catálogo y gestiona tu disponibilidad desde tu panel.",
        cta: "Ir a mi panel de conductor",
        panel: "/conductor",
        puedeReservar: false
    },
    PROPIETARIO: {
        titulo: "Modo propietario",
        desc: "Revisa los vehículos publicados y administra tus autos desde tu panel.",
        cta: "Ir a mi panel de propietario",
        panel: "/mainPropietario",
        puedeReservar: false
    }
};

document.addEventListener("DOMContentLoaded", async () => {
    await cargarSesion(false);
    aplicarNavbarSesion();
    aplicarSidebarSesion();
    mostrarBannerRol();
    await cargarCatalogoDesdeApi();
});

function aplicarNavbarSesion() {
    const guest = document.getElementById("nav-guest");
    const user = document.getElementById("nav-user");
    const sesion = getSesion();

    if (!guest || !user) return;

    if (sesion) {
        guest.style.display = "none";
        user.style.display = "flex";
        const nombre = sesion.nombre ? sesion.nombre.trim() : "Usuario";
        const navName = document.getElementById("nav-user-name");
        if (navName) navName.textContent = nombre;

        const panelLink = document.getElementById("nav-panel-link");
        if (panelLink) {
            panelLink.href = urlPanelPorRol(sesion.rol);
            panelLink.textContent = sesion.rol === "CLIENTE" ? "Mis reservas" : "Mi panel";
        }
    } else {
        guest.style.display = "flex";
        user.style.display = "none";
    }
}

function aplicarSidebarSesion() {
    const loginLink = document.getElementById("sidebar-login");
    const registerLink = document.getElementById("sidebar-register");
    const panelLink = document.getElementById("sidebar-panel");
    const logoutLink = document.getElementById("sidebar-logout");
    const sesion = getSesion();

    if (!loginLink || !registerLink) return;

    if (sesion) {
        loginLink.style.display = "none";
        registerLink.style.display = "none";
        if (panelLink) {
            panelLink.style.display = "flex";
            panelLink.href = urlPanelPorRol(sesion.rol);
        }
        if (logoutLink) logoutLink.style.display = "flex";
    } else {
        loginLink.style.display = "flex";
        registerLink.style.display = "flex";
        if (panelLink) panelLink.style.display = "none";
        if (logoutLink) logoutLink.style.display = "none";
    }
}

function mostrarBannerRol() {
    const banner = document.getElementById("role-banner");
    const welcome = document.getElementById("catalog-welcome");
    const sesion = getSesion();
    const params = new URLSearchParams(window.location.search);

    if (params.has("registro") && welcome) {
        welcome.hidden = false;
        welcome.textContent = "¡Cuenta creada! Ya iniciaste sesión. Explora el catálogo.";
        welcome.classList.add("catalog-alert--success");
    }

    if (!banner || !sesion) return;

    const cfg = ROL_UI[sesion.rol] || ROL_UI.CLIENTE;
    banner.hidden = false;

    const titulo = document.getElementById("role-banner-title");
    const desc = document.getElementById("role-banner-desc");
    const cta = document.getElementById("role-banner-cta");

    if (titulo) titulo.textContent = cfg.titulo + " · " + (sesion.nombre || "").split(" ")[0];
    if (desc) desc.textContent = cfg.desc;
    if (cta) {
        cta.href = cfg.panel;
        cta.textContent = cfg.cta;
    }
}

function urlPanelPorRol(rol) {
    if (rol === "CONDUCTOR") return "/conductor";
    if (rol === "PROPIETARIO") return "/mainPropietario";
    if (rol === "CLIENTE") return "/cliente";
    return "/buscar";
}

async function cargarCatalogoDesdeApi() {
    if (!carsGrid) return;

    carsGrid.innerHTML = "<p class='catalog-loading'>Cargando vehículos...</p>";

    try {
        const res = await fetch("/api/vehiculos/catalogo");
        if (!res.ok) throw new Error("No se pudo cargar el catálogo");
        vehiculos = await res.json();
        renderizarGrid();
    } catch (e) {
        carsGrid.innerHTML = "<p class='catalog-empty'>No hay vehículos disponibles en este momento.</p>";
    }
}

function imagenConFallback(marca, modelo, src) {
    const fallback = referenciaLocal(marca, modelo);
    const safeSrc = src && src.trim() ? src : fallback;
    return `src="${safeSrc}" onerror="this.onerror=null;this.src='${fallback}'"`;
}

function referenciaLocal(marca, modelo) {
    const t = ((marca || "") + " " + (modelo || "")).toLowerCase();
    if (t.includes("tesla")) return "/img/cars/tesla.jpg";
    if (t.includes("bmw")) return "/img/cars/bmw.jpg";
    if (t.includes("corolla") || t.includes("toyota")) return "/img/cars/toyota-corolla.jpg";
    if (t.includes("suv") || t.includes("rav4") || t.includes("mazda")) return "/img/cars/suv.jpg";
    return "/img/cars/generico.jpg";
}

function renderizarGrid() {
    if (!vehiculos.length) {
        carsGrid.innerHTML = "<p class='catalog-empty'>Aún no hay vehículos registrados. Los propietarios pueden publicar autos desde su registro.</p>";
        return;
    }

    carsGrid.innerHTML = vehiculos.map((vehiculo, index) => `
        <div class="car-card">
            <img ${imagenConFallback(vehiculo.marca, vehiculo.modelo, vehiculo.imagen)} alt="${vehiculo.nombre}" loading="lazy">
            <div class="car-content">
                <div class="car-top">
                    <h3>${vehiculo.nombre}</h3>
                    <div class="price">
                        $${vehiculo.precio}
                        <span>/día</span>
                    </div>
                </div>
                <div class="specs">
                    <span>👥 ${vehiculo.capacidad || "—"} plazas</span>
                    <span>⚙️ ${vehiculo.transmision || "Automático"}</span>
                    <span>🚗 ${vehiculo.tipoVehiculo || "Vehículo"}</span>
                </div>
                <button class="details-btn" type="button" onclick="abrirModal(${index})">
                    Ver detalles
                </button>
            </div>
        </div>
    `).join("");
}

function abrirModal(index) {
    const vehiculo = vehiculos[index];
    if (!vehiculo) return;

    vehiculoModalActual = vehiculo;
    sliderImagenes = [vehiculo.imagen || referenciaLocal(vehiculo.marca, vehiculo.modelo)];
    sliderIndex = 0;

    document.getElementById("modal-title").innerText = vehiculo.nombre;
    document.getElementById("modal-description").innerText = vehiculo.descripcion;
    document.getElementById("modal-precio").innerHTML = `$${vehiculo.precio} <span>/día</span>`;
    document.getElementById("modal-capacidad").innerText = `👥 ${vehiculo.capacidad} plazas`;
    document.getElementById("modal-transmision").innerText = `⚙️ ${vehiculo.transmision}`;
    document.getElementById("modal-placa").innerText = `Placa: ${vehiculo.placa}`;
    document.getElementById("modal-marca").innerText = `Marca: ${vehiculo.marca}`;
    document.getElementById("modal-modelo").innerText = `Modelo: ${vehiculo.modelo}`;
    document.getElementById("modal-anio").innerText = `Año: ${vehiculo.anio}`;
    document.getElementById("modal-tipo").innerText = `🚗 Tipo: ${vehiculo.tipoVehiculo}`;
    document.getElementById("modal-estado").innerText = `✅ Estado: ${vehiculo.estado}`;

    configurarBotonReserva();
    renderSlider();
    document.getElementById("modal").style.display = "flex";
}

function configurarBotonReserva() {
    if (!modalReserveBtn) return;
    const sesion = getSesion();
    const cfg = sesion ? (ROL_UI[sesion.rol] || ROL_UI.CLIENTE) : null;

    if (cfg && cfg.puedeReservar && sesion.idCliente) {
        modalReserveBtn.textContent = "Reservar ahora";
        modalReserveBtn.onclick = () => { window.location.href = "/reserva"; };
    } else if (sesion) {
        modalReserveBtn.textContent = cfg.cta;
        modalReserveBtn.onclick = () => { window.location.href = cfg.panel; };
    } else {
        modalReserveBtn.textContent = "Iniciar sesión para reservar";
        modalReserveBtn.onclick = () => { window.location.href = "/InicioSesion"; };
    }
}

function renderSlider() {
    const track = document.getElementById("slider-track");
    const dots = document.getElementById("slider-dots");

    track.innerHTML = sliderImagenes.map(src => {
        const fb = referenciaLocal(vehiculoModalActual?.marca, vehiculoModalActual?.modelo);
        const safe = src || fb;
        return `<img src="${safe}" alt="" onerror="this.onerror=null;this.src='${fb}'">`;
    }).join("");
    dots.innerHTML = sliderImagenes
        .map((_, i) => `<div class="dot ${i === 0 ? "active" : ""}" onclick="goTo(${i})"></div>`)
        .join("");

    track.style.transform = "translateX(0)";
}

function goTo(n) {
    sliderIndex = (n + sliderImagenes.length) % sliderImagenes.length;
    document.getElementById("slider-track").style.transform = `translateX(-${sliderIndex * 100}%)`;
    document.querySelectorAll(".dot").forEach((d, i) => d.classList.toggle("active", i === sliderIndex));
}

function slideModal(dir) {
    goTo(sliderIndex + dir);
}

function cerrarModal() {
    document.getElementById("modal").style.display = "none";
}

function toggleMenu() {
    document.getElementById("sidebar").classList.toggle("active");
    document.getElementById("overlay").classList.toggle("active");
}

window.onclick = function (event) {
    const modal = document.getElementById("modal");
    if (event.target === modal) {
        cerrarModal();
    }
};
