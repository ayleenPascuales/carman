// NAVEGACIÓN
function showSection(id, btn) {
  document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
  document.querySelectorAll('.menu-btn').forEach(b => b.classList.remove('active'));
  document.getElementById(id).classList.add('active');
  btn.classList.add('active');
}

//PRECIO DINÁMICO 
function calcularPrecio() {
  const tipo  = document.getElementById('tipo').value;
  const dias  = Math.max(1, parseInt(document.getElementById('dias').value) || 1);
  const base  = tipo === 'con' ? 110000 : 75000;

  // Descuento del 10% si alquila 7 días o más
  const descuento = dias >= 7 ? 0.9 : 1;
  const total = Math.round(base * dias * descuento);

  document.getElementById('precio-estimado').textContent =
    '$' + total.toLocaleString('es-CO');
}

//VALIDACIÓN Y CONFIRMACIÓN DE RESERVA 
function confirmarReserva() {
  const campos = [
    { id: 'origen',  errId: 'err-origen' },
    { id: 'destino', errId: 'err-destino' },
    { id: 'fecha',   errId: 'err-fecha' },
    { id: 'hora',    errId: 'err-hora' },
  ];

  let valido = true;

  campos.forEach(({ id, errId }) => {
    const el  = document.getElementById(id);
    const err = document.getElementById(errId);
    if (!el.value.trim()) {
      el.classList.add('error');
      err.classList.add('show');
      valido = false;
    } else {
      el.classList.remove('error');
      err.classList.remove('show');
    }
  });

  if (!valido) return;
  mostrarToast('toast-reserva');
}

//GUARDAR PERFIL 
function guardarPerfil() {
  const nombre = document.getElementById('p-nombre').value;

  // Actualiza el nombre en el sidebar y en el encabezado del perfil
  document.querySelector('.sidebar-name').textContent      = nombre;
  document.querySelector('.profile-meta h2').textContent   = nombre;

  mostrarToast('toast-perfil');
}

// TOAST 
function mostrarToast(id) {
  const toast = document.getElementById(id);
  toast.classList.add('show');
  setTimeout(() => toast.classList.remove('show'), 3000);
}

//HISTORIAL: VOLVER A RESERVAR 
function repetirReserva(nombreAuto) {
  // Navega a la sección de reservas
  document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
  document.querySelectorAll('.menu-btn').forEach(b => b.classList.remove('active'));
  document.getElementById('reservas').classList.add('active');
  document.querySelectorAll('.menu-btn')[1].classList.add('active');

  // Pre-llena los campos con la info del viaje anterior
  document.getElementById('origen').value  = 'Medellín';
  document.getElementById('destino').value = nombreAuto + ' — misma ruta';

  calcularPrecio();
}

// NOTIFICACIONES 
let sinLeer = 3;

function marcarLeida(id) {
  const el  = document.getElementById(id);
  const dot = el.querySelector('.notif-dot');
  const btn = el.querySelector('.mark-read-btn');

  el.classList.remove('notif-item--unread');
  if (dot) dot.remove();
  if (btn) btn.remove();

  sinLeer = Math.max(0, sinLeer - 1);
  actualizarBadges();
}

function marcarTodasLeidas() {
  document.querySelectorAll('.notif-item--unread').forEach(el => {
    el.classList.remove('notif-item--unread');
    const dot = el.querySelector('.notif-dot');
    const btn = el.querySelector('.mark-read-btn');
    if (dot) dot.remove();
    if (btn) btn.remove();
  });

  sinLeer = 0;
  actualizarBadges();

  const sub = document.getElementById('notif-sub');
  if (sub) sub.textContent = 'Todo al día ✓';
}

function actualizarBadges() {
  const badge = document.getElementById('notif-count');
  if (badge) {
    badge.textContent    = sinLeer;
    badge.style.display  = sinLeer === 0 ? 'none' : '';
  }

  const sub = document.getElementById('notif-sub');
  if (sub && sinLeer > 0) {
    sub.textContent = sinLeer + ' sin leer';
  }
}