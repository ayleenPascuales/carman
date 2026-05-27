const API = '/api';

let reservasCache = [];
let ordenesCache = [];
let pagosCache = [];
let contratosCache = [];
let metodosCache = [];
let historialCache = [];
let historialFiltro = 'TODOS';
let conductorSeleccionado = null; // Conductor seleccionado desde la sección de conductores
let vehiculoSeleccionadoDesdeCatalogo = null; // Vehículo seleccionado desde autos disponibles

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
    cargarHistorial(),
    cargarAutosDisponibles(),
    cargarConductoresDisponibles()
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
  if (id === 'autos-disponibles') cargarAutosDisponibles();
  if (id === 'conductores-disponibles') cargarConductoresDisponibles();
  if (id === 'opciones') cargarOpciones();
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

// Sistema de notificaciones mejorado
function mostrarNotificacion(mensaje, tipo = 'info') {
  // Eliminar notificación anterior si existe
  const notifAnterior = document.getElementById('sistema-notificacion');
  if (notifAnterior) notifAnterior.remove();

  // Crear nueva notificación
  const notif = document.createElement('div');
  notif.id = 'sistema-notificacion';
  notif.style.cssText = `
    position: fixed;
    top: 20px;
    right: 20px;
    padding: 16px 20px;
    border-radius: 12px;
    background: white;
    box-shadow: 0 4px 20px rgba(0,0,0,0.15);
    z-index: 10000;
    display: flex;
    align-items: center;
    gap: 12px;
    animation: slideInRight 0.3s ease;
    max-width: 350px;
  `;

  const iconos = {
    success: '<i class="fa-solid fa-circle-check" style="color: var(--success); font-size: 20px;"></i>',
    error: '<i class="fa-solid fa-circle-exclamation" style="color: var(--danger); font-size: 20px;"></i>',
    info: '<i class="fa-solid fa-circle-info" style="color: var(--accent); font-size: 20px;"></i>',
    warning: '<i class="fa-solid fa-triangle-exclamation" style="color: var(--warn); font-size: 20px;"></i>'
  };

  const bordes = {
    success: '4px solid var(--success)',
    error: '4px solid var(--danger)',
    info: '4px solid var(--accent)',
    warning: '4px solid var(--warn)'
  };

  notif.style.borderLeft = bordes[tipo] || bordes.info;
  notif.innerHTML = `
    ${iconos[tipo] || iconos.info}
    <span style="font-size: 14px; font-weight: 500; color: var(--text);">${mensaje}</span>
    <button onclick="this.parentElement.remove()" style="background: none; border: none; cursor: pointer; font-size: 16px; color: var(--muted); padding: 4px;">
      <i class="fa-solid fa-times"></i>
    </button>
  `;

  document.body.appendChild(notif);

  // Agregar animación CSS si no existe
  if (!document.getElementById('notif-animation-styles')) {
    const style = document.createElement('style');
    style.id = 'notif-animation-styles';
    style.textContent = `
      @keyframes slideInRight {
        from { opacity: 0; transform: translateX(100%); }
        to { opacity: 1; transform: translateX(0); }
      }
      @keyframes slideOutRight {
        from { opacity: 1; transform: translateX(0); }
        to { opacity: 0; transform: translateX(100%); }
      }
    `;
    document.head.appendChild(style);
  }

  // Auto-eliminar después de 4 segundos
  setTimeout(() => {
    notif.style.animation = 'slideOutRight 0.3s ease';
    setTimeout(() => notif.remove(), 300);
  }, 4000);
}

// ── VEHÍCULOS ──
async function cargarVehiculos() {
  try {
    const vehiculos = await apiGet('/vehiculos/catalogo');
    const select = document.getElementById('vehiculo');
    if (!select) return;

    select.innerHTML = '<option value="">Selecciona un vehículo</option>';

    if (!vehiculos || !vehiculos.length) {
      console.warn('No hay vehículos disponibles en el catálogo');
      return;
    }

    vehiculos.forEach(v => {
      const opt = document.createElement('option');
      opt.value = v.idVehiculo;
      opt.textContent = `${v.marca} ${v.modelo} ${v.anio || ''}`.trim();
      select.appendChild(opt);
    });

    console.log(`Cargados ${vehiculos.length} vehículos en el select`);
  } catch (e) {
    console.error('Error cargando vehículos', e);
    mostrarNotificacion('Error al cargar vehículos disponibles', 'error');
  }
}

// ── AUTOS DISPONIBLES ──
async function cargarAutosDisponibles() {
  const cont = document.getElementById('autos-disponibles-grid');
  try {
    const vehiculos = await apiGet('/vehiculos/catalogo');

    if (!vehiculos.length) {
      cont.innerHTML = '<div class="empty-state"><i class="fa-solid fa-car"></i><p>No hay vehículos disponibles</p></div>';
      return;
    }

    cont.innerHTML = `
      <div class="vehicles-grid">
        ${vehiculos.map(v => `
          <div class="vehicle-card">
            <img src="${v.imagen || '/img/cars/generico.jpg'}" alt="${v.nombre}" onerror="this.src='/img/cars/generico.jpg'">
            <div class="vehicle-info">
              <h3>${v.nombre}</h3>
              <p class="vehicle-details">${v.marca} ${v.modelo} · ${v.anio || '—'}</p>
              <div class="vehicle-specs">
                <span><i class="fa-solid fa-users"></i> ${v.capacidad || '—'} plazas</span>
                <span><i class="fa-solid fa-cog"></i> ${v.transmision || 'Automático'}</span>
                <span><i class="fa-solid fa-car"></i> ${v.tipoVehiculo || 'Vehículo'}</span>
              </div>
              <div class="vehicle-price">
                <span class="price">${v.precio}</span>
                <span class="period">/día</span>
              </div>
              <button class="primary-btn" onclick="seleccionarVehiculo(${v.idVehiculo})">
                <i class="fa-solid fa-calendar-plus"></i> Reservar
              </button>
            </div>
          </div>
        `).join('')}
      </div>
    `;

    // Agregar event listeners para filtros
    const filtroInput = document.getElementById('filtro-autos');
    const filtroTipo = document.getElementById('filtro-tipo-auto');
    if (filtroInput) filtroInput.addEventListener('input', filtrarAutos);
    if (filtroTipo) filtroTipo.addEventListener('change', filtrarAutos);

  } catch (e) {
    console.error('Error cargando autos disponibles', e);
    cont.innerHTML = '<div class="empty-state"><p>Error al cargar vehículos</p></div>';
  }
}

function filtrarAutos() {
  const texto = document.getElementById('filtro-autos').value.toLowerCase();
  const tipo = document.getElementById('filtro-tipo-auto').value;
  const cards = document.querySelectorAll('.vehicle-card');

  cards.forEach(card => {
    const nombre = card.querySelector('h3').textContent.toLowerCase();
    const detalles = card.querySelector('.vehicle-details').textContent.toLowerCase();
    const tipoVehiculo = card.querySelector('.vehicle-specs span:last-child').textContent.toLowerCase();
    const matchTexto = nombre.includes(texto) || detalles.includes(texto);
    const matchTipo = !tipo || tipoVehiculo.includes(tipo.toLowerCase());
    card.style.display = matchTexto && matchTipo ? 'block' : 'none';
  });
}

function seleccionarVehiculo(idVehiculo) {
  // Guardar el vehículo seleccionado
  vehiculoSeleccionadoDesdeCatalogo = idVehiculo;

  // Mostrar notificación de transición
  mostrarNotificacion('Vehículo seleccionado. Redirigiendo a nueva reserva...', 'info');

  // Cambiar a la sección de nueva reserva
  showSection('nueva-reserva', document.querySelector('[data-section="nueva-reserva"]'));

  // Esperar a que la sección sea visible y luego seleccionar el vehículo
  setTimeout(() => {
    const select = document.getElementById('vehiculo');
    if (select) {
      select.value = idVehiculo;
      select.dispatchEvent(new Event('change'));

      // Resaltar el campo del vehículo
      select.style.borderColor = 'var(--accent)';
      select.style.boxShadow = '0 0 0 3px rgba(53, 99, 233, 0.2)';
      setTimeout(() => {
        select.style.borderColor = '';
        select.style.boxShadow = '';
      }, 2000);
    }
  }, 100);
}

// ── CONDUCTORES DISPONIBLES ──
async function cargarConductoresDisponibles() {
  const cont = document.getElementById('conductores-disponibles-grid');
  try {
    const conductores = await apiGet('/conductor');

    if (!conductores.length) {
      cont.innerHTML = '<div class="empty-state"><i class="fa-solid fa-user-tie"></i><p>No hay conductores disponibles</p></div>';
      return;
    }

    cont.innerHTML = `
      <div class="conductors-grid">
        ${conductores.map(c => `
          <div class="conductor-card">
            <div class="conductor-avatar">
              <i class="fa-solid fa-user-tie"></i>
            </div>
            <div class="conductor-info">
              <h3>${c.persona ? c.persona.nombre + ' ' + c.persona.apellido : 'Conductor'}</h3>
              <p class="conductor-details">
                <span><i class="fa-solid fa-star"></i> Calificación: ${c.calificacion || '5.0'}/5.0</span>
                <span><i class="fa-solid fa-check-circle"></i> ${c.disponibilidad ? 'Disponible' : 'No disponible'}</span>
              </p>
              <p class="conductor-licencia">Licencia: ${c.licenciaConductor ? c.licenciaConductor.numero : '—'}</p>
              <button class="primary-btn" onclick="seleccionarConductor(${c.idConductor})" ${!c.disponibilidad ? 'disabled style="opacity:0.5;cursor:not-allowed;"' : ''}>
                <i class="fa-solid fa-user-plus"></i> ${c.disponibilidad ? 'Seleccionar' : 'No disponible'}
              </button>
            </div>
          </div>
        `).join('')}
      </div>
    `;
  } catch (e) {
    console.error('Error cargando conductores', e);
    cont.innerHTML = '<div class="empty-state"><p>Error al cargar conductores</p></div>';
  }
}

function seleccionarConductor(idConductor) {
  // Buscar el conductor en el cache
  const conductor = conductoresCache ? conductoresCache.find(c => c.idConductor === idConductor) : null;
  conductorSeleccionado = conductor;

  // Mostrar notificación de transición
  const nombreConductor = conductor && conductor.persona
    ? `${conductor.persona.nombre} ${conductor.persona.apellido}`
    : 'Conductor seleccionado';
  mostrarNotificacion(`${nombreConductor} seleccionado. Habilitando servicio con conductor...`, 'success');

  // Cambiar a la sección de nueva reserva
  showSection('nueva-reserva', document.querySelector('[data-section="nueva-reserva"]'));

  // Esperar a que la sección sea visible y luego configurar
  setTimeout(() => {
    const tipoSelect = document.getElementById('tipo');
    if (tipoSelect) {
      tipoSelect.value = 'con';
      tipoSelect.dispatchEvent(new Event('change'));

      // Resaltar el campo de tipo de servicio
      tipoSelect.style.borderColor = 'var(--accent)';
      tipoSelect.style.boxShadow = '0 0 0 3px rgba(53, 99, 233, 0.2)';
      setTimeout(() => {
        tipoSelect.style.borderColor = '';
        tipoSelect.style.boxShadow = '';
      }, 2000);
    }

    // Agregar indicador visual del conductor seleccionado
    agregarIndicadorConductorSeleccionado(conductor);
  }, 100);
}

function agregarIndicadorConductorSeleccionado(conductor) {
  const formCard = document.querySelector('#nueva-reserva .form-card');
  if (!formCard || !conductor) return;

  // Eliminar indicador anterior si existe
  const indicadorAnterior = document.getElementById('conductor-seleccionado-indicador');
  if (indicadorAnterior) indicadorAnterior.remove();

  // Crear nuevo indicador
  const indicador = document.createElement('div');
  indicador.id = 'conductor-seleccionado-indicador';
  indicador.style.cssText = `
    background: linear-gradient(135deg, #667eea, #764ba2);
    color: white;
    padding: 12px 16px;
    border-radius: 10px;
    margin-bottom: 16px;
    display: flex;
    align-items: center;
    gap: 12px;
    animation: slideDown 0.3s ease;
  `;
  indicador.innerHTML = `
    <i class="fa-solid fa-user-tie" style="font-size: 20px;"></i>
    <div>
      <strong style="display: block; font-size: 14px;">Conductor seleccionado</strong>
      <span style="font-size: 12px; opacity: 0.9;">
        ${conductor.persona ? conductor.persona.nombre + ' ' + conductor.persona.apellido : 'Conductor'}
      </span>
    </div>
    <button onclick="quitarConductorSeleccionado()" style="background: rgba(255,255,255,0.2); border: none; color: white; padding: 6px 10px; border-radius: 6px; cursor: pointer; font-size: 12px; margin-left: auto;">
      <i class="fa-solid fa-times"></i>
    </button>
  `;

  // Insertar antes del formulario
  const formGrid = formCard.querySelector('.form-grid');
  if (formGrid) {
    formCard.insertBefore(indicador, formGrid);
  }

  // Agregar animación CSS si no existe
  if (!document.getElementById('conductor-indicator-styles')) {
    const style = document.createElement('style');
    style.id = 'conductor-indicator-styles';
    style.textContent = `
      @keyframes slideDown {
        from { opacity: 0; transform: translateY(-10px); }
        to { opacity: 1; transform: translateY(0); }
      }
    `;
    document.head.appendChild(style);
  }
}

function quitarConductorSeleccionado() {
  conductorSeleccionado = null;
  const indicador = document.getElementById('conductor-seleccionado-indicador');
  if (indicador) {
    indicador.style.animation = 'slideDown 0.2s ease reverse';
    setTimeout(() => indicador.remove(), 200);
  }

  const tipoSelect = document.getElementById('tipo');
  if (tipoSelect) {
    tipoSelect.value = 'sin';
    tipoSelect.dispatchEvent(new Event('change'));
  }

  mostrarNotificacion('Conductor removido de la reserva', 'info');
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
    mostrarNotificacion('Tu navegador no soporta geolocalización', 'error');
    return;
  }

  const status = document.getElementById('origen-coords');
  const btn = document.querySelector('button[onclick="usarUbicacion()"]');

  if (status) status.textContent = '📍 Obteniendo ubicación...';
  if (btn) {
    btn.disabled = true;
    btn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Obteniendo...';
  }

  mostrarNotificacion('Obteniendo tu ubicación actual...', 'info');

  navigator.geolocation.getCurrentPosition(
    (pos) => {
      const lat = pos.coords.latitude;
      const lon = pos.coords.longitude;
      const accuracy = pos.coords.accuracy; // Precisión en metros

      origenCoords = { lat, lon };

      if (document.getElementById('origen-coords')) {
        document.getElementById('origen-coords').textContent =
          `📍 Origen: ${lat.toFixed(5)}, ${lon.toFixed(5)} (Precisión: ±${Math.round(accuracy)}m)`;
      }

      if (!mapaRuta) {
        inicializarMapa(lat, lon);
      } else {
        mapaRuta.setView([lat, lon], Math.max(mapaRuta.getZoom(), 13));
        if (marcadorOrigen) marcadorOrigen.setLatLng([lat, lon]);
      }

      if (!marcadorOrigen && mapaRuta) {
        marcadorOrigen = L.marker([lat, lon], {
          title: 'Origen (tu ubicación)',
          icon: L.divIcon({
            className: 'custom-marker-icon',
            html: '<div style="background: var(--accent); width: 30px; height: 30px; border-radius: 50%; border: 3px solid white; box-shadow: 0 2px 8px rgba(0,0,0,0.3);"></div>',
            iconSize: [30, 30],
            iconAnchor: [15, 15]
          })
        }).addTo(mapaRuta);
      }

      // Agregar círculo de precisión
      if (mapaRuta) {
        const precisionCircle = L.circle([lat, lon], {
          radius: accuracy,
          color: 'var(--accent)',
          fillColor: 'var(--accent)',
          fillOpacity: 0.1,
          weight: 2
        }).addTo(mapaRuta);
      }

      if (btn) {
        btn.disabled = false;
        btn.innerHTML = '<i class="fa-solid fa-location-dot"></i> Usar mi ubicación';
        btn.style.background = 'var(--success)';
        setTimeout(() => btn.style.background = '', 2000);
      }

      mostrarNotificacion('¡Ubación obtenida correctamente!', 'success');
      calcularTarifaRutaUI();
    },
    (err) => {
      console.error(err);
      let mensaje = 'No se pudo obtener ubicación.';
      if (err.code === 1) mensaje = 'Permiso de ubicación denegado. Por favor habilita la geolocalización.';
      else if (err.code === 2) mensaje = 'Ubicación no disponible. Verifica tu GPS.';
      else if (err.code === 3) mensaje = 'Tiempo de espera agotado. Intenta nuevamente.';

      if (status) status.textContent = '❌ ' + mensaje;
      const noteEl = document.getElementById('ruta-note');
      if (noteEl) noteEl.textContent = mensaje;

      if (btn) {
        btn.disabled = false;
        btn.innerHTML = '<i class="fa-solid fa-location-dot"></i> Usar mi ubicación';
      }

      mostrarNotificacion(mensaje, 'error');
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

  marcadorOrigen = L.marker([lat, lon], {
    title: 'Origen (tu ubicación)',
    icon: L.divIcon({
      className: 'custom-marker-icon',
      html: '<div style="background: var(--accent); width: 30px; height: 30px; border-radius: 50%; border: 3px solid white; box-shadow: 0 2px 8px rgba(0,0,0,0.3);"></div>',
      iconSize: [30, 30],
      iconAnchor: [15, 15]
    })
  }).addTo(mapaRuta);

  // Agregar círculo de precisión inicial
  L.circle([lat, lon], {
    radius: 100,
    color: 'var(--accent)',
    fillColor: 'var(--accent)',
    fillOpacity: 0.1,
    weight: 2
  }).addTo(mapaRuta);

  mapaRuta.on('click', (e) => {
    const destLat = e.latlng.lat;
    const destLon = e.latlng.lng;
    destinoCoords = { lat: destLat, lon: destLon };

    if (marcadorDestino) {
      marcadorDestino.setLatLng([destLat, destLon]);
    } else {
      marcadorDestino = L.marker([destLat, destLon], {
        title: 'Destino',
        icon: L.divIcon({
          className: 'custom-marker-icon',
          html: '<div style="background: var(--warn); width: 30px; height: 30px; border-radius: 50%; border: 3px solid white; box-shadow: 0 2px 8px rgba(0,0,0,0.3);"></div>',
          iconSize: [30, 30],
          iconAnchor: [15, 15]
        })
      }).addTo(mapaRuta);
    }

    // Dibujar línea entre origen y destino
    if (marcadorOrigen && marcadorDestino) {
      // Eliminar línea anterior si existe
      mapaRuta.eachLayer(layer => {
        if (layer instanceof L.Polyline) {
          mapaRuta.removeLayer(layer);
        }
      });

      const linea = L.polyline([
        [origenCoords.lat, origenCoords.lon],
        [destLat, destLon]
      ], {
        color: 'var(--accent)',
        weight: 4,
        opacity: 0.7,
        dashArray: '10, 10'
      }).addTo(mapaRuta);

      // Ajustar vista para mostrar ambos puntos
      const bounds = L.latLngBounds([
        [origenCoords.lat, origenCoords.lon],
        [destLat, destLon]
      ]);
      mapaRuta.fitBounds(bounds, { padding: [50, 50] });
    }

    const destinoTxt = document.getElementById('destino-coords');
    if (destinoTxt) {
      destinoTxt.textContent = `🎯 Destino: ${destLat.toFixed(5)}, ${destLon.toFixed(5)}`;
      destinoTxt.style.color = 'var(--success)';
    }

    const errMapa = document.getElementById('err-destino-mapa');
    if (errMapa) errMapa.classList.remove('show');

    mostrarNotificacion('Destino seleccionado. Calculando tarifa...', 'info');
    calcularTarifaRutaUI();
  });

  // Agregar tooltip de ayuda
  const helpTooltip = L.tooltip({
    permanent: true,
    direction: 'top',
    className: 'map-help-tooltip'
  }).setContent('Haz clic en el mapa para seleccionar el destino');

  mapaRuta.on('mousemove', (e) => {
    if (!destinoCoords) {
      helpTooltip.setLatLng(e.latlng).addTo(mapaRuta);
    }
  });

  mapaRuta.on('click', () => {
    mapaRuta.closeTooltip();
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
  if (!valido) {
    mostrarNotificacion('Por favor completa todos los campos requeridos', 'error');
    return;
  }

  // Validación de mapa
  const errMapa = document.getElementById('err-destino-mapa');
  if (!origenCoords) {
    const noteEl = document.getElementById('ruta-note');
    if (noteEl) noteEl.textContent = '⚠️ Primero selecciona el origen con "Usar mi ubicación".';
    mostrarNotificacion('Selecciona tu ubicación primero', 'error');
    return;
  }
  if (!destinoCoords) {
    if (errMapa) errMapa.classList.add('show');
    const noteEl = document.getElementById('ruta-note');
    if (noteEl) noteEl.textContent = '⚠️ Selecciona el destino haciendo click en el mapa.';
    mostrarNotificacion('Selecciona el destino en el mapa', 'error');
    return;
  }
  if (errMapa) errMapa.classList.remove('show');
  if (!tarifaActual || !tarifaActual.precioEstimado) {
    const noteEl = document.getElementById('ruta-note');
    if (noteEl) noteEl.textContent = '⚠️ Aún no se pudo calcular la tarifa. Espera a que aparezca el precio.';
    mostrarNotificacion('Espera a que se calcule la tarifa', 'error');
    return;
  }

  const dias = parseInt(document.getElementById('dias').value) || 1;
  const conConductor = document.getElementById('tipo').value === 'con';

  // Usar el conductor seleccionado si existe
  const idConductor = conductorSeleccionado ? conductorSeleccionado.idConductor : null;

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
    idConductor: idConductor,
    dias: dias
  };

  try {
    mostrarNotificacion('Procesando reserva...', 'info');
    const reserva = await apiPost('/reservas/completa', payload);
    if (!reserva) throw new Error('No se pudo crear la reserva');

    mostrarToast('toast-reserva');
    mostrarNotificacion('¡Reserva confirmada exitosamente!', 'success');

    // Limpiar selecciones
    conductorSeleccionado = null;
    vehiculoSeleccionadoDesdeCatalogo = null;
    const indicador = document.getElementById('conductor-seleccionado-indicador');
    if (indicador) indicador.remove();

    // Limpiar mapa
    if (mapaRuta) {
      mapaRuta.eachLayer(layer => {
        if (layer instanceof L.Polyline || layer instanceof L.Circle) {
          mapaRuta.removeLayer(layer);
        }
      });
      if (marcadorDestino) {
        mapaRuta.removeLayer(marcadorDestino);
        marcadorDestino = null;
      }
    }
    destinoCoords = null;
    tarifaActual = null;

    // Resetear campos
    document.getElementById('fecha').value = '';
    document.getElementById('hora').value = '';
    document.getElementById('dias').value = '1';
    document.getElementById('destino-coords').textContent = 'Haz clic en el mapa para fijar el destino.';
    document.getElementById('precio-estimado').textContent = '—';
    document.getElementById('ruta-note').textContent = 'Selecciona el destino para calcular la tarifa.';

    await cargarReservas();
    actualizarResumen();
  } catch (e) {
    console.error(e);
    mostrarNotificacion('No se pudo confirmar la reserva. Verifica los datos e intenta de nuevo.', 'error');
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

// ── OPCIONES ──
async function cargarOpciones() {
  try {
    // Cargar métodos de pago para el select de preferencias
    const metodos = await apiGet('/metodosPago');
    const select = document.getElementById('pref-metodo-pago');
    if (select) {
      select.innerHTML = '<option value="">Seleccionar método</option>';
      metodos.forEach(m => {
        const opt = document.createElement('option');
        opt.value = m.idMetodo;
        opt.textContent = `${m.tipo} — ${m.descripcion || ''}`;
        select.appendChild(opt);
      });
    }
  } catch (e) {
    console.error('Error cargando opciones', e);
  }
}

function guardarOpcionesNotificacion() {
  const email = document.getElementById('notif-email').checked;
  const sms = document.getElementById('notif-sms').checked;
  const push = document.getElementById('notif-push').checked;

  // Aquí se podría guardar en localStorage o enviar al backend
  localStorage.setItem('opciones-notif', JSON.stringify({ email, sms, push }));
  alert('Preferencias de notificación guardadas');
}

function guardarPreferenciasReserva() {
  const tipo = document.getElementById('pref-tipo-servicio').value;
  const duracion = document.getElementById('pref-duracion').value;
  const autoConfirmar = document.getElementById('pref-auto-confirmar').checked;

  localStorage.setItem('opciones-reserva', JSON.stringify({ tipo, duracion, autoConfirmar }));
  alert('Preferencias de reserva guardadas');
}

function guardarMetodoPredeterminado() {
  const metodo = document.getElementById('pref-metodo-pago').value;
  if (!metodo) {
    alert('Selecciona un método de pago');
    return;
  }

  localStorage.setItem('metodo-predeterminado', metodo);
  alert('Método de pago predeterminado guardado');
}
