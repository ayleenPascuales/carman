package com.try_1.spring.proyect.spring_app.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.try_1.spring.proyect.spring_app.models.Cliente;
import com.try_1.spring.proyect.spring_app.models.Conductor;
import com.try_1.spring.proyect.spring_app.models.EstadoLicencia;
import com.try_1.spring.proyect.spring_app.models.EstadoPersona;
import com.try_1.spring.proyect.spring_app.models.EstadoVehiculo;
import com.try_1.spring.proyect.spring_app.models.Licencia;
import com.try_1.spring.proyect.spring_app.models.Persona;
import com.try_1.spring.proyect.spring_app.models.Propietario;
import com.try_1.spring.proyect.spring_app.models.Vehiculo;
import com.try_1.spring.proyect.spring_app.services.AuthService;
import com.try_1.spring.proyect.spring_app.services.ClienteService;
import com.try_1.spring.proyect.spring_app.services.ConductorService;
import com.try_1.spring.proyect.spring_app.services.LicenciaService;
import com.try_1.spring.proyect.spring_app.services.PersonaService;
import com.try_1.spring.proyect.spring_app.services.PropietarioService;
import com.try_1.spring.proyect.spring_app.services.VehiculoService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;

@Controller
@RequestMapping("/")
public class RegistroController {

    @Autowired
    private PersonaService personaService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ConductorService conductorService;
    @Autowired
    private PropietarioService propietarioService;
    @Autowired
    private LicenciaService licenciaService;
    @Autowired
    private VehiculoService vehiculoService;
    @Autowired
    private AuthService authService;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("registro")
    public String registroFormulario() {
        return "redirect:/registro2";
    }

    @PostMapping("registro")
    @Transactional
    public String registrar(
            @RequestParam("tipoUsuario") String tipoUsuario,
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("edad") Integer edad,
            @RequestParam("email") String email,
            @RequestParam("telefono") String telefono,
            @RequestParam("ciudad") String ciudad,
            @RequestParam("usuario") String usuario,
            @RequestParam("contrasena") String contrasena,

            // Propietario (vehículo + tarjeta)
            @RequestParam(value = "placa", required = false) String placa,
            @RequestParam(value = "modelo", required = false) String modelo,
            @RequestParam(value = "marca", required = false) String marca,
            @RequestParam(value = "anio", required = false) Integer anio,
            @RequestParam(value = "capacidad", required = false) String capacidad,
            @RequestParam(value = "tipoVehiculo", required = false) String tipoVehiculo,

            @RequestParam(value = "foto", required = false) MultipartFile[] fotosVehiculo,
            @RequestParam(value = "tarjetaPropiedad", required = false) MultipartFile[] tarjetasPropiedad,

            // Verificación (para todos los tipos)
            @RequestParam(value = "idPersona", required = false) MultipartFile[] docsIdentidad,
            @RequestParam(value = "fotoLicencia", required = false) MultipartFile[] fotosLicencia,
            @RequestParam(value = "tipoLicencia", required = false) String tipoLicencia,
            @RequestParam(value = "numeroLicencia", required = false) String numeroLicencia,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        try {
            email = email.trim();
            contrasena = contrasena == null ? null : contrasena.trim();
            tipoUsuario = tipoUsuario == null ? "" : tipoUsuario.trim();

            if (!"cliente".equalsIgnoreCase(tipoUsuario)
                    && !"conductor".equalsIgnoreCase(tipoUsuario)
                    && !"propietario".equalsIgnoreCase(tipoUsuario)) {
                redirectAttributes.addFlashAttribute("error", "Selecciona un tipo de usuario válido.");
                return "redirect:/registro2";
            }

            if (esEmailExistente(email)) {
                redirectAttributes.addFlashAttribute("error", "El correo ya está registrado.");
                return "redirect:/registro2";
            }

            if (isBlank(tipoLicencia)) {
                redirectAttributes.addFlashAttribute("error", "Falta el tipo de licencia.");
                return "redirect:/registro2";
            }
            if (isBlank(numeroLicencia)) {
                redirectAttributes.addFlashAttribute("error", "Falta el número de licencia.");
                return "redirect:/registro2";
            }
            if (esNumeroLicenciaExistente(numeroLicencia)) {
                redirectAttributes.addFlashAttribute("error", "Ese número de licencia ya está registrado.");
                return "redirect:/registro2";
            }

            if (!tieneArchivo(docsIdentidad) || !tieneArchivo(fotosLicencia)) {
                redirectAttributes.addFlashAttribute("error", "Debes subir documento de identidad y foto de licencia.");
                return "redirect:/registro2";
            }

            if ("propietario".equalsIgnoreCase(tipoUsuario)) {
                if (esBlank(placa) || esBlank(modelo) || esBlank(marca) || anio == null || esBlank(capacidad)
                        || esBlank(tipoVehiculo)) {
                    redirectAttributes.addFlashAttribute("error", "Completa los datos del vehículo para propietario.");
                    return "redirect:/registro2";
                }
                if (parseFirstInt(capacidad) == null) {
                    redirectAttributes.addFlashAttribute("error", "Capacidad del vehículo inválida.");
                    return "redirect:/registro2";
                }
                if (!tieneArchivo(tarjetasPropiedad)) {
                    redirectAttributes.addFlashAttribute("error", "Debes subir la tarjeta de propiedad.");
                    return "redirect:/registro2";
                }
                if (!tieneArchivo(fotosVehiculo)) {
                    redirectAttributes.addFlashAttribute("error", "Debes subir al menos una foto del vehículo.");
                    return "redirect:/registro2";
                }
                if (esPlacaExistente(placa)) {
                    redirectAttributes.addFlashAttribute("error", "Esa placa ya está registrada.");
                    return "redirect:/registro2";
                }
            }

            // 1) Guardar Persona
            int idPersona = nextId("Persona", "idPersona");
            Persona persona = new Persona();
            persona.setIdPersona(idPersona);
            persona.setNombre(nombre);
            persona.setApellido(apellido);
            persona.setEdad(edad);
            persona.setEmail(email);
            persona.setContrasena(contrasena);
            persona.setTelefono(telefono);
            persona.setUsuario(usuario);
            persona.setEstado(EstadoPersona.ACTIVO);
            persona.setFechaRegistro(LocalDate.now());
            persona.setCiudad(ciudad);
            personaService.guardar(persona);

            // 2) Guardar Licencia
            String fotoDocPath = saveFirstFile(docsIdentidad, "docs");
            String fotoLicPath = saveFirstFile(fotosLicencia, "licencia");

            Licencia licencia = new Licencia();
            licencia.setNumero(numeroLicencia);
            licencia.setTipoLicencia(tipoLicencia);
            licencia.setFechaExpiracion(LocalDate.now().plusYears(5));
            licencia.setEstado(EstadoLicencia.VIGENTE);
            if (fotoLicPath != null) {
                licencia.setFotoLicencia(fotoLicPath);
            }
            licencia = licenciaService.guardar(licencia);

            // 3) Guardar rol
            if ("cliente".equalsIgnoreCase(tipoUsuario)) {
                int idCliente = nextId("Cliente", "idCliente");
                Cliente cliente = new Cliente();
                cliente.setIdCliente(idCliente);
                cliente.setPersona(persona);
                cliente.setLicenciaCliente(licencia);
                clienteService.guardar(cliente);
            } else if ("conductor".equalsIgnoreCase(tipoUsuario)) {
                int idConductor = nextId("Conductor", "idConductor");
                Conductor conductor = new Conductor();
                conductor.setIdConductor(idConductor);
                conductor.setPersona(persona);
                conductor.setLicenciaConductor(licencia);
                conductor.setDisponibilidad(true);
                conductor.setCalificacion(new BigDecimal("5.0"));
                conductorService.guardar(conductor);
            } else if ("propietario".equalsIgnoreCase(tipoUsuario)) {
                int idPropietario = nextId("Propietario", "idPropietario");
                Propietario propietario = new Propietario();
                propietario.setIdPropietario(idPropietario);
                propietario.setPersona(persona);
                propietario.setLicenciaPropietario(licencia);

                String tarjetaPath = saveFirstFile(tarjetasPropiedad, "tarjeta");
                propietario.setTarjetaPropiedad(tarjetaPath);
                propietario.setTotalPrestamos(0);
                propietarioService.guardar(propietario);

                Vehiculo vehiculo = new Vehiculo();
                vehiculo.setPropietario(propietario);
                vehiculo.setPlaca(placa.toUpperCase());
                vehiculo.setMarca(marca);
                vehiculo.setModelo(modelo);
                vehiculo.setAnio(anio);
                vehiculo.setTipoVehiculo(tipoVehiculo);
                vehiculo.setCapacidad(parseFirstInt(capacidad));
                vehiculo.setEstado(EstadoVehiculo.ACTIVO);
                vehiculo.setFoto(saveFirstFile(fotosVehiculo, "vehiculo"));

                vehiculoService.guardar(vehiculo);
            } else {
                redirectAttributes.addFlashAttribute("error", "Tipo de usuario inválido.");
                return "redirect:/registro2";
            }

            authService.iniciarSesionPorEmail(email, tipoUsuario, session);
            return "redirect:/buscar?registro=ok";
        } catch (Exception ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "No se pudo registrar el usuario: " + ex.getMessage());
            return "redirect:/registro2";
        }
    }

    private boolean esEmailExistente(String email) {
        Number count = (Number) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM Persona WHERE LOWER(email) = LOWER(:email)")
                .setParameter("email", email)
                .getSingleResult();
        return count != null && count.intValue() > 0;
    }

    private boolean esNumeroLicenciaExistente(String numero) {
        Number count = (Number) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM Licencia WHERE numero = :numero")
                .setParameter("numero", numero.trim())
                .getSingleResult();
        return count != null && count.intValue() > 0;
    }

    private boolean esPlacaExistente(String placa) {
        Number count = (Number) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM Vehiculo WHERE placa = :placa")
                .setParameter("placa", placa.trim().toUpperCase())
                .getSingleResult();
        return count != null && count.intValue() > 0;
    }

    private boolean tieneArchivo(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return false;
        }
        MultipartFile file = files[0];
        return file != null && !file.isEmpty();
    }

    private int nextId(String tableName, String idColumn) {
        Number n = (Number) entityManager
                .createNativeQuery("SELECT COALESCE(MAX(" + idColumn + "), 0) + 1 FROM " + tableName)
                .getSingleResult();
        return n.intValue();
    }

    private Integer parseFirstInt(String value) {
        if (value == null) return null;
        Matcher m = Pattern.compile("(\\d+)").matcher(value);
        if (m.find()) return Integer.parseInt(m.group(1));
        return null;
    }

    private String combinePaths(String p1, String p2) {
        if (p1 == null && p2 == null) return null;
        if (p1 != null && p2 != null) return p1 + "|" + p2;
        return p1 != null ? p1 : p2;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean esBlank(String value) {
        return isBlank(value);
    }

    private String saveFirstFile(MultipartFile[] files, String subfolder) throws IOException {
        if (files == null || files.length == 0) return null;
        MultipartFile file = files[0];
        if (file == null || file.isEmpty()) return null;

        String original = file.getOriginalFilename();
        String safeOriginal = original == null ? "file" : original.replaceAll("[^a-zA-Z0-9._-]", "_");
        String ext = "";
        int dot = safeOriginal.lastIndexOf('.');
        if (dot >= 0) ext = safeOriginal.substring(dot);

        String filename = UUID.randomUUID().toString() + ext;

        // Usar ruta absoluta basada en el directorio de trabajo actual
        Path currentDir = Paths.get("").toAbsolutePath();
        Path baseDir = currentDir.resolve("uploads/registro").resolve(subfolder == null ? "" : subfolder);
        Files.createDirectories(baseDir);
        Path target = baseDir.resolve(filename);
        file.transferTo(target.toFile());

        // Copia en static para que esté disponible al reiniciar (desarrollo).
        Path staticDir = currentDir.resolve("src/main/resources/static/uploads/registro")
                .resolve(subfolder == null ? "" : subfolder);
        try {
            Files.createDirectories(staticDir);
            Files.copy(target, staticDir.resolve(filename), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            // Si falla la copia a static, continuamos (puede ser en producción)
            System.err.println("No se pudo copiar a static: " + e.getMessage());
        }

        // Ruta accesible desde el navegador como archivo estático.
        String cleanSub = subfolder == null ? "" : subfolder;
        if (cleanSub.isEmpty()) {
            return "/uploads/registro/" + filename;
        }
        return "/uploads/registro/" + cleanSub + "/" + filename;
    }
}

