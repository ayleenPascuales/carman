const API = '/api';

document.addEventListener('DOMContentLoaded', async () => {
  const sesion = await cargarSesion();
  if (!sesion) return;
  await cargarResumenCliente();
});

function showSection(id, btn) {
  document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
  document.querySelectorAll('.menu-btn').forEach(b => {
    if (b.tagName === 'BUTTON') b.classList.remove('active');
  });
  document.getElementById(id).classList.add('active');
  if (btn) btn.classList.add('active');
}

function formatMoney(value) {
  const num = parseFloat(value);
  if (isNaN(num)) return '$0';
  return '$' + Math.round(num).toLocaleString('es-CO');
}

function formatFecha(fecha) {
  if (!fecha) return '—';
  const d = new Date(fecha + (fecha.includes('T') ? '' : 'T00:00:00'));
  if (isNaN(d.getTime())) return fecha;
  return d.toLocaleDateString('es-CO', { day: 'numeric', month: 'short', year: 'numeric' });
}

async function cargarResumenCliente() {
  const idCliente = getClienteId();
  if (!idCliente) return;

  try {
    const [reservas, ordenes] = await Promise.all([
      fetch(`${API}/reservas/cliente/${idCliente}`).then(r => r.ok ? r.json() : []),
      fetch(`${API}/ordenPago/cliente/${idCliente}`).then(r => r.ok ? r.json() : [])
    ]);

    const activas = reservas.filter(r =>
      ['Pendiente', 'Confirmada', 'EnCurso'].includes(r.estado)
    );

    const cardActiva = document.getElementById('card-reserva-activa');
    const cardEstado = document.getElementById('card-reserva-estado');
    if (activas.length) {
      const r = activas[0];
      cardActiva.textContent = r.vehiculo || 'Reserva activa';
      cardEstado.textContent = 'Estado: ' + (r.estado || '—');
    } else {
      cardActiva.textContent = 'Sin reservas activas';
      cardEstado.textContent = 'Crea una en el módulo de reservas';
    }

    const futuras = reservas
      .filter(r => r.fechaServicio)
      .sort((a, b) => a.fechaServicio.localeCompare(b.fechaServicio));

    const cardProximaFecha = document.getElementById('card-proxima-fecha');
    const cardProximaDetalle = document.getElementById('card-proxima-detalle');
    if (futuras.length) {
      const p = futuras[0];
      cardProximaFecha.textContent = formatFecha(p.fechaServicio);
      cardProximaDetalle.textContent = `${p.vehiculo || 'Vehículo'} · ${p.horaEntrega || ''}`;
    }

    const ordenesActivas = ordenes.filter(o => o.estado === 'ACTIVA');
    const totalPendiente = ordenesActivas.reduce((sum, o) => sum + (parseFloat(o.total) || 0), 0);

    document.getElementById('card-pago-pendiente').textContent = formatMoney(totalPendiente);
    document.getElementById('card-pago-detalle').textContent = ordenesActivas.length
      ? `${ordenesActivas.length} orden(es) pendiente(s)`
      : 'Sin pagos pendientes';

    const badge = document.getElementById('pagos-pendientes-badge');
    if (badge && ordenesActivas.length > 0) {
      badge.textContent = ordenesActivas.length;
      badge.style.display = '';
    }
  } catch (e) {
    console.error('Error cargando resumen', e);
  }
}

function guardarPerfil() {
  const nombre = document.getElementById('p-nombre').value;
  document.getElementById('p-nombre').dataset.edited = '1';
  document.querySelectorAll('.sidebar-name').forEach(el => el.textContent = nombre);
  document.querySelector('.profile-meta h2').textContent = nombre;
  document.querySelectorAll('.profile-photo, .sidebar-avatar').forEach(el => {
    el.textContent = nombre.charAt(0).toUpperCase();
  });
  mostrarToast('toast-perfil');
}

function mostrarToast(id) {
  const toast = document.getElementById(id);
  toast.classList.add('show');
  setTimeout(() => toast.classList.remove('show'), 3000);
}

let sinLeer = 3;

function marcarLeida(id) {
  const el = document.getElementById(id);
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
    badge.textContent = sinLeer;
    badge.style.display = sinLeer === 0 ? 'none' : '';
  }

  const sub = document.getElementById('notif-sub');
  if (sub && sinLeer > 0) {
    sub.textContent = sinLeer + ' sin leer';
  }
}
