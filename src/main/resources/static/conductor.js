// ── Navegación entre secciones
function showSection(id, element) {
    document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
    document.querySelectorAll('.menu li').forEach(li => li.classList.remove('active'));
    document.getElementById(id).classList.add('active');
    if (element) element.classList.add('active');
}

// ── Toggle disponibilidad
let activo = true;

function toggleStatus() {
    activo = !activo;
    const btn   = document.getElementById('toggleBtn');
    const dot   = document.getElementById('statusDot');
    const label = document.getElementById('statusLabel');

    if (activo) {
        btn.textContent = 'Desactivarse';
        btn.classList.remove('inactive');
        dot.classList.remove('offline');
        label.textContent = 'Disponible';
    } else {
        btn.textContent = 'Activarse';
        btn.classList.add('inactive');
        dot.classList.add('offline');
        label.textContent = 'No disponible';
    }
}

// ── Countdown timer (próximo servicio)
let seconds = 42;
const countdownEl = document.getElementById('countdown');

setInterval(() => {
    if (seconds > 0) seconds--;
    const m = String(Math.floor(seconds / 60)).padStart(2, '0');
    const s = String(seconds % 60).padStart(2, '0');
    if (countdownEl) countdownEl.textContent = `${m}:${s}`;
}, 1000);