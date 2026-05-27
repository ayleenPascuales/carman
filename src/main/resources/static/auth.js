let sesionActual = null;

async function cargarSesion(redirigirSiNoAuth) {
  try {
    const res = await fetch('/api/auth/me');
    if (!res.ok) {
      sesionActual = null;
      if (redirigirSiNoAuth !== false) {
        window.location.href = '/InicioSesion';
      }
      return null;
    }
    sesionActual = await res.json();
    aplicarSesionUI();
    return sesionActual;
  } catch (e) {
    sesionActual = null;
    if (redirigirSiNoAuth !== false) {
      window.location.href = '/InicioSesion';
    }
    return null;
  }
}

function getClienteId() {
  return sesionActual ? sesionActual.idCliente : null;
}

function getSesion() {
  return sesionActual;
}

function getRol() {
  return sesionActual ? sesionActual.rol : null;
}

function urlPanelPorRol(rol) {
  if (rol === 'CONDUCTOR') return '/conductor';
  if (rol === 'PROPIETARIO') return '/mainPropietario';
  if (rol === 'CLIENTE') return '/cliente';
  return '/buscar';
}

function redirectSegunRol(sesion) {
  window.location.href = urlPanelPorRol(sesion?.rol);
}

function aplicarSesionUI() {
  if (!sesionActual) return;

  const nombre = sesionActual.nombre ? sesionActual.nombre.trim() : 'Cliente';
  const inicial = nombre.charAt(0).toUpperCase();
  const primerNombre = nombre.split(' ')[0];

  document.querySelectorAll('.sidebar-name').forEach(el => {
    el.textContent = nombre;
  });

  document.querySelectorAll('.sidebar-avatar, .profile-photo').forEach(el => {
    el.textContent = inicial;
  });

  const welcome = document.getElementById('welcome-name');
  if (welcome) welcome.textContent = primerNombre;

  const profileName = document.getElementById('p-nombre');
  if (profileName && !profileName.dataset.edited) {
    profileName.value = nombre;
  }

  const profileEmail = document.getElementById('p-email');
  if (profileEmail && sesionActual.email) {
    profileEmail.value = sesionActual.email;
  }

  const profileTel = document.getElementById('p-tel');
  if (profileTel && sesionActual.telefono) {
    profileTel.value = sesionActual.telefono;
  }

  const profileMeta = document.querySelector('.profile-meta h2');
  if (profileMeta) profileMeta.textContent = nombre;
}

async function cerrarSesion() {
  await fetch('/api/auth/logout', { method: 'POST' });
  window.location.href = '/InicioSesion';
}
