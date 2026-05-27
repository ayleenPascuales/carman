const vehiculos = [

{
    placa: "ABC123",
    marca: "Toyota",
    modelo: "Yaris",
    anio: 2022,
    capacidad: 5,
    tipoVehiculo: "Sedán",
    estado: "Activo",

    nombre: "Toyota Yaris",
    precio: "42.000",
    transmision: "Manual",

    descripcion:
    "Vehículo económico, cómodo y perfecto para ciudad.",

    imagenes: [
       "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?q=80&w=1200",
        "https://images.unsplash.com/photo-1492144534655-ae79c964c9d7?q=80&w=1200",
        "https://images.unsplash.com/photo-1503376780353-7e6692767b70?q=80&w=1200"
    ]
},

{
    placa: "DEF456",
    marca: "Honda",
    modelo: "Civic",
    anio: 2023,
    capacidad: 5,
    tipoVehiculo: "Sedán",
    estado: "Activo",

    nombre: "Honda Civic",
    precio: "52.000",
    transmision: "Automático",

    descripcion:
    "Diseño deportivo y excelente rendimiento.",

    imagenes: [
       "https://images.unsplash.com/photo-1553440569-bcc63803a83d?q=80&w=1200",
        "https://images.unsplash.com/photo-1580273916550-e323be2ae537?q=80&w=1200",
        "https://images.unsplash.com/photo-1502877338535-766e1452684a?q=80&w=1200"
    ]
},

{
    placa: "GHI789",
    marca: "Mazda",
    modelo: "CX-3",
    anio: 2021,
    capacidad: 5,
    tipoVehiculo: "SUV",
    estado: "Activo",

    nombre: "Mazda CX-3",
    precio: "58.000",
    transmision: "Automático",

    descripcion:
    "SUV moderna ideal para viajes largos.",

    imagenes: [
       
        "https://images.unsplash.com/photo-1511919884226-fd3cad34687c?q=80&w=1200",
        "https://images.unsplash.com/photo-1502161254066-6c74afbf07aa?q=80&w=1200",
        "https://images.unsplash.com/photo-1494976388531-d1058494cdd8?q=80&w=1200"
    ]
},

{
    placa: "JKL321",
    marca: "BMW",
    modelo: "X5",
    anio: 2024,
    capacidad: 7,
    tipoVehiculo: "SUV",
    estado: "Activo",

    nombre: "BMW X5",
    precio: "80.000",
    transmision: "Automático",

    descripcion:
    "SUV premium con gran potencia y lujo.",

    imagenes: [
        
        "https://images.unsplash.com/photo-1555215695-3004980ad54e?q=80&w=1200",
        "https://images.unsplash.com/photo-1503736334956-4c8f8e92946d?q=80&w=1200",
        "https://images.unsplash.com/photo-1494905998402-395d579af36f?q=80&w=1200"
    ]
},

{
    placa: "MNO654",
    marca: "Kia",
    modelo: "Sportage",
    anio: 2020,
    capacidad: 5,
    tipoVehiculo: "SUV",
    estado: "Activo",

    nombre: "Kia Sportage",
    precio: "55.000",
    transmision: "Manual",

    descripcion:
    "Espaciosa y perfecta para carretera.",

    imagenes: [
         "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?q=80&w=1200",
        "https://images.unsplash.com/photo-1544636331-e26879cd4d9b?q=80&w=1200",
        "https://images.unsplash.com/photo-1504215680853-026ed2a45def?q=80&w=1200"
    ]
},

{
    placa: "PQR987",
    marca: "Tesla",
    modelo: "Model 3",
    anio: 2025,
    capacidad: 5,
    tipoVehiculo: "Eléctrico",
    estado: "Activo",

    nombre: "Tesla Model 3",
    precio: "110.000",
    transmision: "Eléctrico",

    descripcion:
    "Tecnología avanzada y conducción eléctrica.",

    imagenes: [
         "https://images.unsplash.com/photo-1560958089-b8a1929cea89?q=80&w=1200",
        "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?q=80&w=1200",
        "https://images.unsplash.com/photo-1493238792000-8113da705763?q=80&w=1200"

    ]
}

];

let sliderIndex = 0;
let sliderImagenes = [];

const carsGrid = document.getElementById("carsGrid");

vehiculos.forEach((vehiculo, index) => {

    carsGrid.innerHTML += `

    <div class="car-card">

        <img src="${vehiculo.imagenes[0]}" 
        alt="${vehiculo.nombre}">

        <div class="car-content">

            <div class="car-top">

                <h3>${vehiculo.nombre}</h3>

                <div class="price">
                    $${vehiculo.precio}
                    <span>/día</span>
                </div>

            </div>

            <div class="specs">

                <span>
                    👥 ${vehiculo.capacidad} plazas
                </span>

                <span>
                    ⚙️ ${vehiculo.transmision}
                </span>

            </div>

            <button
                class="details-btn"
                onclick="abrirModal(${index})">

                Ver detalles

            </button>

        </div>

    </div>

    `;
});

function abrirModal(index) {

    const vehiculo = vehiculos[index];

    sliderImagenes = vehiculo.imagenes;
    sliderIndex = 0;

    document.getElementById("modal-title").innerText =
    vehiculo.nombre;

    document.getElementById("modal-description").innerText =
    vehiculo.descripcion;

    document.getElementById("modal-precio").innerHTML =
    `$${vehiculo.precio} <span>/día</span>`;

    document.getElementById("modal-capacidad").innerText =
    `👥 ${vehiculo.capacidad} plazas`;

    document.getElementById("modal-transmision").innerText =
    `⚙️ ${vehiculo.transmision}`;

    document.getElementById("modal-placa").innerText =
    `Placa: ${vehiculo.placa}`;

    document.getElementById("modal-marca").innerText =
    `Marca: ${vehiculo.marca}`;

    document.getElementById("modal-modelo").innerText =
    `Modelo: ${vehiculo.modelo}`;

    document.getElementById("modal-anio").innerText =
    `Año: ${vehiculo.anio}`;

    document.getElementById("modal-tipo").innerText =
    `🚗 Tipo: ${vehiculo.tipoVehiculo}`;

    document.getElementById("modal-estado").innerText =
    `✅ Estado: ${vehiculo.estado}`;

    

    renderSlider();

    document.getElementById("modal").style.display = "flex";
}

function renderSlider() {

    const track =
    document.getElementById("slider-track");

    const dots =
    document.getElementById("slider-dots");

    track.innerHTML = sliderImagenes
        .map(src => `<img src="${src}" alt="">`)
        .join("");

    dots.innerHTML = sliderImagenes
        .map((_, i) =>
            `<div class="dot ${i === 0 ? "active" : ""}"
            onclick="goTo(${i})"></div>`
        )
        .join("");

    track.style.transform = "translateX(0)";
}

function goTo(n) {

    sliderIndex =
    (n + sliderImagenes.length)
    % sliderImagenes.length;

    document.getElementById("slider-track").style.transform =
    `translateX(-${sliderIndex * 100}%)`;

    document.querySelectorAll(".dot")
    .forEach((d, i) =>
        d.classList.toggle("active", i === sliderIndex)
    );
}

function slideModal(dir) {
    goTo(sliderIndex + dir);
}

function cerrarModal() {
    document.getElementById("modal").style.display = "none";
}

function toggleMenu() {

    document
    .getElementById("sidebar")
    .classList
    .toggle("active");

    document
    .getElementById("overlay")
    .classList
    .toggle("active");
}

window.onclick = function(event) {

    const modal =
    document.getElementById("modal");

    if(event.target == modal){
        cerrarModal();
    }
};