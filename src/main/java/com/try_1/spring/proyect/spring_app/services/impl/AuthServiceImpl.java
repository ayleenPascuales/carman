package com.try_1.spring.proyect.spring_app.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.dto.LoginRequest;
import com.try_1.spring.proyect.spring_app.dto.SesionUsuarioDTO;
import com.try_1.spring.proyect.spring_app.services.AuthService;

import jakarta.servlet.http.HttpSession;
import jakarta.persistence.EntityManager;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String SESSION_KEY = "sesionUsuario";

    @Autowired
    private EntityManager entityManager;

    @Override
    public SesionUsuarioDTO login(LoginRequest request, HttpSession session) {
        if (request.getEmail() == null || request.getContrasena() == null) {
            return null;
        }

        String email = request.getEmail().trim();
        String contrasena = request.getContrasena().trim();
        if (email.isEmpty() || contrasena.isEmpty()) {
            return null;
        }

        Object[] row = buscarCredencialesPorEmail(email);
        if (row == null) {
            return null;
        }

        String contrasenaBd = safeString(row[7]);
        if (contrasenaBd == null || !contrasenaBd.equals(contrasena)) {
            return null;
        }

        String estado = safeString(row[6]);
        if (estado != null && estado.equalsIgnoreCase("INACTIVO")) {
            return null;
        }

        SesionUsuarioDTO sesion = toDto(row);
        session.setAttribute(SESSION_KEY, sesion);
        return sesion;
    }

    @Override
    public SesionUsuarioDTO obtenerSesion(HttpSession session) {
        Object value = session.getAttribute(SESSION_KEY);
        if (value instanceof SesionUsuarioDTO sesion) {
            return sesion;
        }
        return null;
    }

    @Override
    public void logout(HttpSession session) {
        session.invalidate();
    }

    private Object[] buscarCredencialesPorEmail(String email) {
        @SuppressWarnings("unchecked")
        java.util.List<Object[]> rows = entityManager.createNativeQuery("""
            SELECT c.idCliente, p.idPersona, p.nombre, p.apellido, p.email, p.telefono, p.estado, p.contrasena
            FROM Persona p
            INNER JOIN Cliente c ON c.idPersona = p.idPersona
            WHERE LOWER(p.email) = LOWER(:email)
            """)
            .setParameter("email", email)
            .setMaxResults(1)
            .getResultList();

        return rows.isEmpty() ? null : rows.get(0);
    }

    private SesionUsuarioDTO toDto(Object[] row) {
        SesionUsuarioDTO dto = new SesionUsuarioDTO();
        dto.setIdCliente(((Number) row[0]).intValue());
        dto.setIdPersona(((Number) row[1]).intValue());

        String nombre = safeString(row[2]);
        String apellido = safeString(row[3]);
        dto.setNombre((nombre != null ? nombre : "") + " " + (apellido != null ? apellido : ""));
        dto.setEmail(safeString(row[4]));
        dto.setTelefono(safeString(row[5]));
        dto.setRol("CLIENTE");
        return dto;
    }

    private String safeString(Object value) {
        return value == null ? null : value.toString();
    }
}
