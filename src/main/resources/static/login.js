document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('login-form');
  if (!form) return;
  const submitBtn = form.querySelector('button[type="submit"]');
  const errorEl = document.getElementById('login-error');

  fetch('/api/auth/me')
    .then(res => {
      if (res.ok) window.location.href = '/cliente';
    })
    .catch(() => {});

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const email = document.getElementById('login-email').value.trim();
    const contrasena = document.getElementById('login-password').value;
    if (submitBtn) {
      submitBtn.disabled = true;
      submitBtn.textContent = 'Validando...';
    }

    errorEl.classList.remove('show');
    errorEl.textContent = 'Correo o contraseña incorrectos';

    try {
      const res = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, contrasena })
      });

      if (!res.ok) {
        if (res.status >= 500) {
          errorEl.textContent = 'No fue posible validar en este momento.';
        }
        errorEl.classList.add('show');
        return;
      }

      window.location.href = '/cliente';
    } catch (err) {
      errorEl.textContent = 'Error de conexión con el servidor.';
      errorEl.classList.add('show');
    } finally {
      if (submitBtn) {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Ingresar';
      }
    }
  });
});
