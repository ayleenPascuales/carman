package com.try_1.spring.proyect.spring_app.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.try_1.spring.proyect.spring_app.dto.LoginRequest;
import com.try_1.spring.proyect.spring_app.dto.SesionUsuarioDTO;
import com.try_1.spring.proyect.spring_app.services.AuthService;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;

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

        Object[] row = buscarPersonaPorEmail(email);
        if (row == null) {
            return null;
        }

        // Índices alineados con el SELECT: 0-6 Persona, 7 Cliente, 8 Conductor, 9 Propietario
        String contrasenaBd = safeString(row[6]);
        if (contrasenaBd == null || !contrasenaBd.equals(contrasena)) {
            return null;
        }

        String estado = safeString(row[5]);
        if (estado != null && estado.equalsIgnoreCase("INACTIVO")) {
            return null;
        }

        SesionUsuarioDTO sesion = toDto(row, null);
        session.setAttribute(SESSION_KEY, sesion);
        return sesion;
    }

    @Override
    public SesionUsuarioDTO iniciarSesionPorEmail(String email, String tipoUsuarioPreferido, HttpSession session) {
        if (email == null || email.isBlank()) {
            return null;
        }
        Object[] row = buscarPersonaPorEmail(email.trim());
        if (row == null) {
            return null;
        }
        SesionUsuarioDTO sesion = toDto(row, tipoUsuarioPreferido);
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

    @Override
    public String urlPanelPorRol(String rol) {
        if (rol == null) {
            return "/buscar";
        }
        return switch (rol.toUpperCase()) {
            case "CLIENTE" -> "/cliente";
            case "CONDUCTOR" -> "/conductor";
            case "PROPIETARIO" -> "/mainPropietario";
            default -> "/buscar";
        };
    }

    private Object[] buscarPersonaPorEmail(String email) {
        @SuppressWarnings("unchecked")
        // TOP 1 + ORDER BY evita que Hibernate en SQL Server añada "ORDER BY @@version" con setMaxResults.
        java.util.List<Object[]> rows = entityManager.createNativeQuery("""
            SELECT TOP 1 p.idPersona, p.nombre, p.apellido, p.email, p.telefono, p.estado, p.contrasena,
                   c.idCliente, co.idConductor, pr.idPropietario
            FROM Persona p
            LEFT JOIN Cliente c ON c.idPersona = p.idPersona
            LEFT JOIN Conductor co ON co.idPersona = p.idPersona
            LEFT JOIN Propietario pr ON pr.idPersona = p.idPersona
            WHERE LOWER(p.email) = LOWER(:email)
            ORDER BY p.idPersona
            """)
            .setParameter("email", email)
            .getResultList();

        return rows.isEmpty() ? null : rows.get(0);
    }

    private SesionUsuarioDTO toDto(Object[] row, String tipoUsuarioPreferido) {
        SesionUsuarioDTO dto = new SesionUsuarioDTO();
        dto.setIdPersona(((Number) row[0]).intValue());

        String nombre = safeString(row[1]);
        String apellido = safeString(row[2]);
        dto.setNombre(((nombre != null ? nombre : "") + " " + (apellido != null ? apellido : "")).trim());
        dto.setEmail(safeString(row[3]));
        dto.setTelefono(safeString(row[4]));

        if (row[7] != null) {
            dto.setIdCliente(((Number) row[7]).intValue());
        }
        if (row[8] != null) {
            dto.setIdConductor(((Number) row[8]).intValue());
        }
        if (row[9] != null) {
            dto.setIdPropietario(((Number) row[9]).intValue());
        }

        dto.setRol(resolverRol(dto, tipoUsuarioPreferido));
        return dto;
    }

    private String resolverRol(SesionUsuarioDTO dto, String preferido) {
        if (preferido != null && !preferido.isBlank()) {
            String p = preferido.trim().toLowerCase();
            if ("cliente".equals(p) && dto.getIdCliente() != null) {
                return "CLIENTE";
            }
            if ("conductor".equals(p) && dto.getIdConductor() != null) {
                return "CONDUCTOR";
            }
            if ("propietario".equals(p) && dto.getIdPropietario() != null) {
                return "PROPIETARIO";
            }
        }
        if (dto.getIdCliente() != null) {
            return "CLIENTE";
        }
        if (dto.getIdPropietario() != null) {
            return "PROPIETARIO";
        }
        if (dto.getIdConductor() != null) {
            return "CONDUCTOR";
        }
        return "INVITADO";
    }

    private String safeString(Object value) {
        return value == null ? null : value.toString();
    }
}
