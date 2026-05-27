const API = '/api';

let reservasCache = [];
let ordenesCache = [];
let pagosCache = [];
let contratosCache = [];
let metodosCache = [];
let historialCache = [];
let historialFiltro = 'TODOS';

// ── Tarifa por ruta (mapa + ubicación) ──
let mapaRuta = null;
let marcadorOrigen = null;
let marcadorDestino = null;
let origenCoords = null;   // { lat, lon }
let destinoCoords = null;  // { lat, lon }
let tarifaActual = null;   // TarifaRutaResponse

document.addEventListener('DOMContentLoaded', async () => {
  const sesion = await cargarSesion();
  if (!sesion) return;

  await cargarDatosIniciales();
  abrirSeccionDesdeHash();
  setupTarifaUIEventos();
});

function abrirSeccionDesdeHash() {
  const hash = window.location.hash.replace('#', '');
  if (!hash) return;
  const btn = document.querySelector(`[data-section="${hash}"]`);
  if (btn) showSection(hash, btn);
}

function clienteId() {
  return getClienteId();
}

async function cargarDatosIniciales() {
  await Promise.all([
    cargarVehiculos(),
    cargarReservas(),
    cargarOrdenes(),
    cargarPagos(),
    cargarContratos(),
    cargarMetodos(),
    cargarHistorial()
  ]);
  actualizarResumen();
}

async function apiGet(url) {
  const res = await fetch(API + url);
  if (!res.ok) throw new Error('Error en la petición');
  return res.json();
}

async function apiPost(url, body) {
  const res = await fetch(API + url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: body ? JSON.stringify(body) : undefined
  });
  if (!res.ok) throw new Error('Error en la petición');
  return res.json();
}

async function apiPatch(url) {
  const res = await fetch(API + url, { method: 'PATCH' });
  if (!res.ok) throw new Error('Error en la petición');
  return res.ok;
}

function showSection(id, btn) {
  document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
  document.querySelectorAll('.menu-btn').forEach(b => b.classList.remove('active'));
  document.getElementById(id).classList.add('active');
  if (btn) btn.classList.add('active');

  if (id === 'ordenes-pago') cargarOrdenes();
  if (id === 'pagos') { cargarMetodos(); cargarPagos(); cargarOrdenes(); }
  if (id === 'contrato') cargarContratos();
  if (id === 'historial') cargarHistorial();
  if (id === 'resumen') actualizarResumen();
  if (id === 'nueva-reserva' && mapaRuta) {
    // Leaflet necesita recalcular el tamaño cuando el contenedor se hace visible.
    setTimeout(() => mapaRuta.invalidateSize(), 150);
  }
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

function badgeEstado(estado) {
  if (!estado) return '<span class="status-badge status-badge--info">—</span>';
  const e = estado.toUpperCase();
  let cls = 'status-badge--info';
  if (['PAGADO', 'PAGADA', 'CONFIRMADA', 'FINALIZADA', 'FINALIZADO', 'ACTIVO', 'COMPLETADA'].includes(e)) cls = 'status-badge--ok';
  else if (['PENDIENTE', 'ACTIVA', 'PENDIENTE_FIRMA', 'ENCURSO'].includes(e)) cls = 'status-badge--warn';
  else if (['RECHAZADO', 'RECHAZADA', 'CANCELADA', 'CANCELADO', 'ANULADA'].includes(e)) cls = 'status-badge--danger';
  return `<span class="status-badge ${cls}">${estado.replace(/_/g, ' ')}</span>`;
}

function mostrarToast(id, esError) {
  const toast = document.getElementById(id);
  if (!toast) return;
  toast.classList.remove('toast--error');
  if (esError) toast.classList.add('toast--error');
  toast.classList.add('show');
  setTimeout(() => toast.classList.remove('show'), 3500);
}

// ── VEHÍCULOS ──
async function cargarVehiculos() {
  try {
    const vehiculos = await apiGet('/vehiculos');
    const select = document.getElementById('vehiculo');
    select.innerHTML = '<option value="">Selecciona un vehículo</option>';
    vehiculos.forEach(v => {
      const opt = document.createElement('option');
      opt.value = v.idVehiculo;
      opt.textContent = `${v.marca} ${v.modelo} ${v.anio || ''}`.trim();
      select.appendChild(opt);
    });
  } catch (e) {
    console.error('Error cargando vehículos', e);
  }
}

// ── RESERVAS ──
async function cargarReservas() {
  try {
    reservasCache = await apiGet(`/reservas/cliente/${clienteId()}`);
    poblarSelectReservas();
    renderResumenReservas();
  } catch (e) {
    console.error('Error cargando reservas', e);
    reservasCache = [];
  }
}

function poblarSelectReservas() {
  ['select-reserva-orden', 'select-reserva-contrato'].forEach(id => {
    const select = document.getElementById(id);
    if (!select) return;
    select.innerHTML = '<option value="">Selecciona una reserva</option>';
    reservasCache.forEach(r => {
      const opt = document.createElement('option');
      opt.value = r.idReserva;
      opt.textContent = `#${r.idReserva} · ${r.vehiculo || 'Vehículo'} · ${formatFecha(r.fechaServicio)}`;
      select.appendChild(opt);
    });
  });
}

function renderResumenReservas() {
  const cont = document.getElementById('resumen-reservas-list');
  if (!cont) return;

  if (!reservasCache.length) {
    cont.innerHTML = '<div class="empty-state"><i class="fa-solid fa-calendar-xmark"></i><p>No hay reservas registradas</p></div>';
    return;
  }

  const ultimas = reservasCache.slice(0, 5);
  cont.innerHTML = `
    <div class="data-table-wrap">
      <table class="data-table">
        <thead><tr><th>ID</th><th>Vehículo</th><th>Ruta</th><th>Fecha</th><th>Total</th><th>Estado</th></tr></thead>
        <tbody>
          ${ultimas.map(r => `
            <tr>
              <td>#${r.idReserva}</td>
              <td>${r.vehiculo || '—'}</td>
              <td>${r.origen || '—'} → ${r.destino || '—'}</td>
              <td>${formatFecha(r.fechaServicio)}</td>
              <td>${formatMoney(r.precioEstimado)}</td>
              <td>${badgeEstado(r.estado)}</td>
            </tr>
          `).join('')}
        </tbody>
      </table>
    </div>`;
}

function setupTarifaUIEventos() {
  const tipoEl = document.getElementById('tipo');
  const diasEl = document.getElementById('dias');
  if (tipoEl) tipoEl.addEventListener('change', () => calcularTarifaRutaUI());
  if (diasEl) diasEl.addEventListener('change', () => calcularTarifaRutaUI());
}

async function calcularTarifaRutaUI() {
  const precioEl = document.getElementById('precio-estimado');
  const noteEl = document.getElementById('ruta-note');
  if (!precioEl || !noteEl) return;

  if (!origenCoords || !destinoCoords) {
    precioEl.textContent = '—';
    noteEl.textContent = 'Selecciona el origen y el destino para calcular la tarifa.';
    tarifaActual = null;
    return;
  }

  try {
    const dias = Math.max(1, parseInt(document.getElementById('dias').value) || 1);
    const conConductor = document.getElementById('tipo').value === 'con';

    const payload = {
      origenLon: origenCoords.lon,
      origenLat: origenCoords.lat,
      destinoLon: destinoCoords.lon,
      destinoLat: destinoCoords.lat,
      conConductor: conConductor,
      dias: dias
    };

    const resp = await apiPost('/reservas/tarifa-ruta', payload);
    if (!resp || !resp.precioEstimado) {
      precioEl.textContent = '—';
      noteEl.textContent = 'No se pudo calcular la ruta. Intenta con otro destino.';
      tarifaActual = null;
      return;
    }

    tarifaActual = resp;
    precioEl.textContent = formatMoney(resp.precioEstimado);
    const distTxt = resp.distanciaKm ? resp.distanciaKm.toString() : '?';
    const durTxt = resp.duracionMin != null ? resp.duracionMin : '?';
    noteEl.textContent = `Ruta estimada: ${distTxt} km · ${durTxt} min`;
  } catch (e) {
    console.error(e);
    precioEl.textContent = '—';
    noteEl.textContent = 'Error calculando la tarifa. Reintenta.';
    tarifaActual = null;
  }
}

window.usarUbicacion = function usarUbicacion() {
  if (!navigator.geolocation) {
    const noteEl = document.getElementById('ruta-note');
    if (noteEl) noteEl.textContent = 'Tu navegador no soporta geolocalización.';
    return;
  }

  const status = document.getElementById('origen-coords');
  if (status) status.textContent = 'Obteniendo ubicación...';

  navigator.geolocation.getCurrentPosition(
    (pos) => {
      const lat = pos.coords.latitude;
      const lon = pos.coords.longitude;
      origenCoords = { lat, lon };

      if (document.getElementById('origen-coords')) {
        document.getElementById('origen-coords').textContent =
          `Origen: ${lat.toFixed(5)}, ${lon.toFixed(5)}`;
      }

      if (!mapaRuta) {
        inicializarMapa(lat, lon);
      } else {
        mapaRuta.setView([lat, lon], Math.max(mapaRuta.getZoom(), 13));
        if (marcadorOrigen) marcadorOrigen.setLatLng([lat, lon]);
      }

      if (!marcadorOrigen && mapaRuta) {
        marcadorOrigen = L.marker([lat, lon], { title: 'Origen (entrega)' }).addTo(mapaRuta);
      }

      calcularTarifaRutaUI();
    },
    (err) => {
      console.error(err);
      if (status) status.textContent = 'No se pudo obtener ubicación.';
      const noteEl = document.getElementById('ruta-note');
      if (noteEl) noteEl.textContent = 'Permite la ubicación para calcular la tarifa.';
    },
    { enableHighAccuracy: true, maximumAge: 15000, timeout: 10000 }
  );
};

function inicializarMapa(lat, lon) {
  const mapEl = document.getElementById('ruta-map');
  if (!mapEl || !window.L) return;

  mapaRuta = L.map('ruta-map').setView([lat, lon], 13);
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; OpenStreetMap contributors'
  }).addTo(mapaRuta);

  marcadorOrigen = L.marker([lat, lon], { title: 'Origen (entrega)' }).addTo(mapaRuta);

  mapaRuta.on('click', (e) => {
    const destLat = e.latlng.lat;
    const destLon = e.latlng.lng;
    destinoCoords = { lat: destLat, lon: destLon };

    if (marcadorDestino) marcadorDestino.setLatLng([destLat, destLon]);
    else marcadorDestino = L.marker([destLat, destLon], { title: 'Destino' }).addTo(mapaRuta);

    const destinoTxt = document.getElementById('destino-coords');
    if (destinoTxt) destinoTxt.textContent = `Destino: ${destLat.toFixed(5)}, ${destLon.toFixed(5)}`;

    const errMapa = document.getElementById('err-destino-mapa');
    if (errMapa) errMapa.classList.remove('show');

    calcularTarifaRutaUI();
  });
}

async function confirmarReserva() {
  const campos = [
    { id: 'vehiculo', errId: 'err-vehiculo' },
    { id: 'fecha',   errId: 'err-fecha' },
    { id: 'hora',    errId: 'err-hora' },
  ];

  let valido = true;
  campos.forEach(({ id, errId }) => {
    const el = document.getElementById(id);
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

  // Validación de mapa
  const errMapa = document.getElementById('err-destino-mapa');
  if (!origenCoords) {
    const noteEl = document.getElementById('ruta-note');
    if (noteEl) noteEl.textContent = 'Primero selecciona el origen con "Usar mi ubicación".';
    return;
  }
  if (!destinoCoords) {
    if (errMapa) errMapa.classList.add('show');
    const noteEl = document.getElementById('ruta-note');
    if (noteEl) noteEl.textContent = 'Selecciona el destino haciendo click en el mapa.';
    return;
  }
  if (errMapa) errMapa.classList.remove('show');
  if (!tarifaActual || !tarifaActual.precioEstimado) {
    const noteEl = document.getElementById('ruta-note');
    if (noteEl) noteEl.textContent = 'Aún no se pudo calcular la tarifa. Espera a que aparezca el precio antes de confirmar.';
    return;
  }

  const dias = parseInt(document.getElementById('dias').value) || 1;
  const conConductor = document.getElementById('tipo').value === 'con';

  const payload = {
    idCliente: clienteId(),
    idVehiculo: parseInt(document.getElementById('vehiculo').value),
    origen: 'Ubicación actual',
    destino: 'Destino marcado',
    origenLon: origenCoords.lon,
    origenLat: origenCoords.lat,
    destinoLon: destinoCoords.lon,
    destinoLat: destinoCoords.lat,
    fechaServicio: document.getElementById('fecha').value,
    horaEntrega: document.getElementById('hora').value,
    precioEstimado: tarifaActual ? tarifaActual.precioEstimado : null,
    conConductor: conConductor,
    idConductor: null,
    dias: dias
  };

  try {
    const reserva = await apiPost('/reservas/completa', payload);
    if (!reserva) throw new Error('No se pudo crear la reserva');
    mostrarToast('toast-reserva');
    await cargarReservas();
    actualizarResumen();
  } catch (e) {
    console.error(e);
    alert('No se pudo confirmar la reserva. Verifica los datos e intenta de nuevo.');
  }
}

// ── ÓRDENES DE PAGO ──
async function cargarOrdenes() {
  const cont = document.getElementById('ordenes-list');
  try {
    ordenesCache = await apiGet(`/ordenPago/cliente/${clienteId()}`);
    poblarSelectOrdenes();

    if (!ordenesCache.length) {
      cont.innerHTML = '<div class="empty-state"><i class="fa-solid fa-file-invoice"></i><p>No hay órdenes de pago</p></div>';
      return;
    }

    cont.innerHTML = `
      <div class="data-table-wrap">
        <table class="data-table">
          <thead><tr><th>ID</th><th>Reserva</th><th>Emisión</th><th>Total</th><th>Estado</th><th>Acciones</th></tr></thead>
          <tbody>
            ${ordenesCache.map(o => `
              <tr>
                <td>#${o.idOrdenPago}</td>
                <td>${o.reserva ? '#' + o.reserva.idReserva : '—'}</td>
                <td>${formatFecha(o.fechaEmision)}</td>
                <td>${formatMoney(o.total)}</td>
                <td>${badgeEstado(o.estado)}</td>
                <td class="table-actions">
                  <button class="outline-btn outline-btn--sm" onclick="descargarOrdenPdf(${o.idOrdenPago})">
                    <i class="fa-solid fa-download"></i> PDF
                  </button>
                </td>
              </tr>
            `).join('')}
          </tbody>
        </table>
      </div>`;
  } catch (e) {
    cont.innerHTML = '<div class="empty-state"><p>Error al cargar órdenes</p></div>';
  }
}

function poblarSelectOrdenes() {
  const select = document.getElementById('select-orden-pago');
  if (!select) return;
  select.innerHTML = '<option value="">Selecciona una orden activa</option>';
  ordenesCache
    .filter(o => o.estado === 'ACTIVA')
    .forEach(o => {
      const opt = document.createElement('option');
      opt.value = o.idOrdenPago;
      opt.textContent = `#${o.idOrdenPago} · ${formatMoney(o.total)} · Reserva #${o.reserva?.idReserva || '—'}`;
      select.appendChild(opt);
    });
}

async function generarOrdenPago() {
  const idReserva = document.getElementById('select-reserva-orden').value;
  if (!idReserva) { alert('Selecciona una reserva'); return; }

  try {
    await apiPost(`/ordenPago/desdeReserva/${idReserva}`);
    await cargarOrdenes();
    await cargarHistorial();
    actualizarResumen();
    alert('Orden de pago generada correctamente');
  } catch (e) {
    alert('No se pudo generar la orden de pago');
  }
}

function descargarOrdenPdf(id) {
  window.open(`${API}/ordenPago/${id}/ordenPago.pdf`, '_blank');
}

// ── PAGOS Y MÉTODOS ──
async function cargarMetodos() {
  const cont = document.getElementById('metodos-list');
  try {
    metodosCache = await apiGet('/metodosPago/activos');
    if (!metodosCache.length) {
      metodosCache = await apiGet('/metodosPago');
    }

    const select = document.getElementById('select-metodo-pago');
    select.innerHTML = '<option value="">Selecciona un método</option>';

    if (!metodosCache.length) {
      cont.innerHTML = '<div class="empty-state"><p>No hay métodos de pago registrados</p></div>';
      return;
    }

    cont.innerHTML = metodosCache.map(m => `
      <div class="payment-method">
        <i class="fa-solid fa-credit-card"></i>
        <div>
          <strong>${m.tipo}</strong>
          <p style="font-size:12px;color:var(--muted);">${m.descripcion || ''}</p>
        </div>
      </div>
    `).join('');

    metodosCache.forEach(m => {
      const opt = document.createElement('option');
      opt.value = m.idMetodo;
      opt.textContent = `${m.tipo} — ${m.descripcion || ''}`;
      select.appendChild(opt);
    });
  } catch (e) {
    cont.innerHTML = '<div class="empty-state"><p>Error al cargar métodos</p></div>';
  }
}

async function cargarPagos() {
  const cont = document.getElementById('pagos-list');
  try {
    pagosCache = await apiGet(`/pagos/cliente/${clienteId()}`);

    if (!pagosCache.length) {
      cont.innerHTML = '<div class="empty-state"><i class="fa-solid fa-receipt"></i><p>No hay pagos registrados</p></div>';
      return;
    }

    cont.innerHTML = `
      <div class="data-table-wrap">
        <table class="data-table">
          <thead><tr><th>ID</th><th>Reserva</th><th>Método</th><th>Monto</th><th>Fecha</th><th>Estado</th></tr></thead>
          <tbody>
            ${pagosCache.map(p => `
              <tr>
                <td>#${p.idPago}</td>
                <td>${p.reserva ? '#' + p.reserva.idReserva : '—'}</td>
                <td>${p.metodo ? p.metodo.tipo : '—'}</td>
                <td>${formatMoney(p.monto)}</td>
                <td>${p.fechaHoraPago ? formatFecha(p.fechaHoraPago.split('T')[0]) : '—'}</td>
                <td>${badgeEstado(p.estado)}</td>
              </tr>
            `).join('')}
          </tbody>
        </table>
      </div>`;
  } catch (e) {
    cont.innerHTML = '<div class="empty-state"><p>Error al cargar pagos</p></div>';
  }
}

async function procesarPago() {
  const idOrden = document.getElementById('select-orden-pago').value;
  const idMetodo = document.getElementById('select-metodo-pago').value;

  if (!idOrden || !idMetodo) {
    alert('Selecciona una orden y un método de pago');
    return;
  }

  try {
    const pago = await apiPost('/pagos/registrar', {
      idOrdenPago: parseInt(idOrden),
      idMetodo: parseInt(idMetodo)
    });

    if (!pago) throw new Error('Pago fallido');

    if (pago.estado === 'PAGADO') {
      mostrarToast('toast-pago');
    } else {
      mostrarToast('toast-pago', true);
      alert('El pago fue rechazado. Intenta con otro método.');
    }

    await Promise.all([cargarPagos(), cargarOrdenes(), cargarHistorial()]);
    actualizarResumen();
  } catch (e) {
    alert('Error al procesar el pago');
  }
}

// ── CONTRATOS ──
async function cargarContratos() {
  const cont = document.getElementById('contratos-list');
  try {
    contratosCache = await apiGet(`/contratoAlquiler/cliente/${clienteId()}`);

    if (!contratosCache.length) {
      cont.innerHTML = '<div class="empty-state"><i class="fa-solid fa-file-contract"></i><p>No hay contratos generados</p></div>';
      return;
    }

    cont.innerHTML = `
      <div class="data-table-wrap">
        <table class="data-table">
          <thead><tr><th>ID</th><th>Reserva</th><th>Vehículo</th><th>Generación</th><th>Estado</th><th>Acciones</th></tr></thead>
          <tbody>
            ${contratosCache.map(c => `
              <tr>
                <td>#${c.idContratoAlquiler}</td>
                <td>${c.reserva ? '#' + c.reserva.idReserva : '—'}</td>
                <td>${c.vehiculo ? c.vehiculo.marca + ' ' + c.vehiculo.modelo : '—'}</td>
                <td>${formatFecha(c.fechaGeneracion)}</td>
                <td>${badgeEstado(c.estado)}</td>
                <td class="table-actions">
                  <button class="outline-btn outline-btn--sm" onclick="descargarContratoPdf(${c.idContratoAlquiler})">
                    <i class="fa-solid fa-download"></i> PDF
                  </button>
                  ${c.estado === 'PENDIENTE_FIRMA' ? `
                    <button class="primary-btn primary-btn--sm" onclick="activarContrato(${c.idContratoAlquiler})">
                      <i class="fa-solid fa-signature"></i> Firmar
                    </button>` : ''}
                </td>
              </tr>
            `).join('')}
          </tbody>
        </table>
      </div>`;
  } catch (e) {
    cont.innerHTML = '<div class="empty-state"><p>Error al cargar contratos</p></div>';
  }
}

async function generarContrato() {
  const idReserva = document.getElementById('select-reserva-contrato').value;
  if (!idReserva) { alert('Selecciona una reserva'); return; }

  try {
    await apiPost(`/contratoAlquiler/desdeReserva/${idReserva}`);
    await cargarContratos();
    await cargarHistorial();
    actualizarResumen();
    alert('Contrato generado correctamente');
  } catch (e) {
    alert('No se pudo generar el contrato');
  }
}

function descargarContratoPdf(id) {
  window.open(`${API}/contratoAlquiler/${id}/contrato.pdf`, '_blank');
}

async function activarContrato(id) {
  try {
    await apiPatch(`/contratoAlquiler/${id}/activar`);
    await cargarContratos();
    await cargarHistorial();
    alert('Contrato firmado y activado');
  } catch (e) {
    alert('No se pudo activar el contrato');
  }
}

// ── HISTORIAL ──
async function cargarHistorial() {
  const cont = document.getElementById('historial-list');
  try {
    historialCache = await apiGet(`/reservas/cliente/${clienteId()}/historial`);
    renderHistorial();
  } catch (e) {
    cont.innerHTML = '<div class="empty-state"><p>Error al cargar historial</p></div>';
  }
}

function filtrarHistorial(tipo, btn) {
  historialFiltro = tipo;
  document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
  if (btn) btn.classList.add('active');
  renderHistorial();
}

function renderHistorial() {
  const cont = document.getElementById('historial-list');
  let items = historialCache;

  if (historialFiltro !== 'TODOS') {
    items = items.filter(i => i.tipo === historialFiltro);
  }

  if (!items.length) {
    cont.innerHTML = '<div class="empty-state"><i class="fa-solid fa-inbox"></i><p>No hay registros en el historial</p></div>';
    return;
  }

  const iconos = {
    RESERVA: 'fa-calendar',
    ORDEN_PAGO: 'fa-file-invoice-dollar',
    PAGO: 'fa-credit-card',
    CONTRATO: 'fa-file-contract'
  };

  cont.innerHTML = items.map(item => `
    <div class="history-item">
      <div class="history-info">
        <h3>
          <span class="history-type">${item.tipo.replace(/_/g, ' ')}</span>
          ${item.titulo}
        </h3>
        <p>${item.detalle || ''} · ${formatFecha(item.fecha)}${item.monto ? ' · ' + formatMoney(item.monto) : ''}</p>
      </div>
      <div style="display:flex;align-items:center;gap:10px;">
        ${badgeEstado(item.estado)}
        <i class="fa-solid ${iconos[item.tipo] || 'fa-circle'}" style="color:var(--accent);opacity:0.5;"></i>
      </div>
    </div>
  `).join('');
}

// ── RESUMEN ──
function actualizarResumen() {
  document.getElementById('stat-reservas').textContent = reservasCache.length;
  document.getElementById('stat-ordenes').textContent = ordenesCache.filter(o => o.estado === 'ACTIVA').length;
  document.getElementById('stat-pagos').textContent = pagosCache.filter(p => p.estado === 'PAGADO').length;
  document.getElementById('stat-contratos').textContent = contratosCache.length;
  renderResumenReservas();
}
