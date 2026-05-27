CREATE DATABASE CarmanDB;
GO

USE CarmanDB;
GO

-- =========================================
-- TABLA PERSONA
-- =========================================

CREATE TABLE Persona (
    idPersona INT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    contrasena VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    fechaRegistro DATE,
    edad INT,

    estado VARCHAR(20) NOT NULL,

    CONSTRAINT CK_Persona_Estado
    CHECK (estado IN ('ACTIVO','SUSPENDIDO','INACTIVO'))
);
GO

-- =========================================
-- TABLA LICENCIA
-- =========================================

CREATE TABLE Licencia (
    idLicencia INT PRIMARY KEY IDENTITY(1,1),
    numero VARCHAR(50) NOT NULL,
    fechaExpiracion DATE NOT NULL,
    tipoLicencia VARCHAR(50),
    foto VARCHAR(500),

    estado VARCHAR(20) NOT NULL,

    CONSTRAINT UQ_Licencia_Numero UNIQUE(numero),

    CONSTRAINT CK_Licencia_Estado
    CHECK (estado IN ('VIGENTE','VENCIDA','SUSPENDIDA'))
);
GO

-- =========================================
-- TABLA CLIENTE
-- =========================================

CREATE TABLE Cliente (
    idCliente INT PRIMARY KEY,
    idPersona INT NOT NULL,
    idLicencia INT NOT NULL,

    FOREIGN KEY (idPersona)
    REFERENCES Persona(idPersona),

    FOREIGN KEY (idLicencia)
    REFERENCES Licencia(idLicencia)
);
GO

-- =========================================
-- TABLA CONDUCTOR
-- =========================================

CREATE TABLE Conductor (
    idConductor INT PRIMARY KEY,
    idPersona INT NOT NULL,
    idLicencia INT NOT NULL,

    disponibilidad BIT NOT NULL,
    calificacion DECIMAL(3,2),

    FOREIGN KEY (idPersona)
    REFERENCES Persona(idPersona),

    FOREIGN KEY (idLicencia)
    REFERENCES Licencia(idLicencia)
);
GO

-- =========================================
-- TABLA PROPIETARIO
-- =========================================

CREATE TABLE Propietario (
    idPropietario INT PRIMARY KEY,
    idPersona INT NOT NULL,
    idLicencia INT NOT NULL,

    tarjetaPropiedad VARCHAR(MAX),
    totalPrestamos INT DEFAULT 0,

    FOREIGN KEY (idPersona)
    REFERENCES Persona(idPersona),

    FOREIGN KEY (idLicencia)
    REFERENCES Licencia(idLicencia)
);
GO

-- =========================================
-- TABLA VEHICULO
-- =========================================

CREATE TABLE Vehiculo (
    idVehiculo INT PRIMARY KEY IDENTITY(1,1),

    idPropietario INT NOT NULL,

    placa VARCHAR(20) NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(50),

    anio INT,
    capacidad INT,
    tipoVehiculo VARCHAR(50),

    estado VARCHAR(30),

    CONSTRAINT UQ_Vehiculo_Placa UNIQUE(placa),

    CONSTRAINT CK_Vehiculo_Estado
    CHECK (estado IN (
        'ACTIVO',
        'INACTIVO',
        'EN_MANTENIMIENTO'
    )),

    FOREIGN KEY (idPropietario)
    REFERENCES Propietario(idPropietario)
);
GO

-- =========================================
-- TABLA ESTADO TECNICO VEHICULO
-- =========================================

CREATE TABLE EstadoTecnicoVehiculo (
    idEstado INT PRIMARY KEY IDENTITY(1,1),

    idVehiculo INT NOT NULL,

    kilometraje DECIMAL(10,2),
    estadoMotor VARCHAR(50),
    estadoFrenos VARCHAR(50),
    estadoLlantas VARCHAR(50),
    estadoBateria DECIMAL(5,2),

    necesitaMantenimiento BIT,

    fechaRegistro DATETIME,

    FOREIGN KEY (idVehiculo)
    REFERENCES Vehiculo(idVehiculo)
);
GO

-- =========================================
-- TABLA TELEMETRIA
-- =========================================

CREATE TABLE Telemetria (
    idTelemetria INT PRIMARY KEY IDENTITY(1,1),

    idVehiculo INT NOT NULL,

    latitud DECIMAL(10,6),
    longitud DECIMAL(10,6),
    velocidad DECIMAL(10,2),

    timestampRegistro DATETIME,

    FOREIGN KEY (idVehiculo)
    REFERENCES Vehiculo(idVehiculo)
);
GO

-- =========================================
-- TABLA RUTA
-- =========================================

CREATE TABLE Ruta (
    idRuta INT PRIMARY KEY IDENTITY(1,1),

    origen VARCHAR(100),
    destino VARCHAR(100),

    origenLon DECIMAL(10,6),
    origenLat DECIMAL(10,6),

    destinoLon DECIMAL(10,6),
    destinoLat DECIMAL(10,6),

    distancia DECIMAL(10,2),
    duracion INT,

    fecha DATE
);
GO

-- =========================================
-- TABLA RESERVA
-- =========================================

CREATE TABLE Reserva (
    idReserva INT PRIMARY KEY IDENTITY(1,1),

    idCliente INT NOT NULL,
    idVehiculo INT NOT NULL,
    idConductor INT NULL,
    idRuta INT NOT NULL,

    horaEntrega TIME,

    fechaReserva DATE,
    fechaServicio DATE,

    precioEstimado DECIMAL(10,2),

    estado VARCHAR(20),

    CONSTRAINT CK_Reserva_Estado
    CHECK (estado IN (
        'PENDIENTE',
        'CONFIRMADA',
        'RECHAZADA',
        'FINALIZADA',
        'CANCELADA',
        'EN_CURSO'
    )),

    FOREIGN KEY (idCliente)
    REFERENCES Cliente(idCliente),

    FOREIGN KEY (idVehiculo)
    REFERENCES Vehiculo(idVehiculo),

    FOREIGN KEY (idConductor)
    REFERENCES Conductor(idConductor),

    FOREIGN KEY (idRuta)
    REFERENCES Ruta(idRuta)
);
GO

-- =========================================
-- TABLA CONTRATO ALQUILER
-- =========================================

CREATE TABLE ContratoAlquiler (
    idContratoAlquiler INT PRIMARY KEY IDENTITY(1,1),

    idReserva INT UNIQUE,
    idCliente INT,
    idVehiculo INT,
    idPropietario INT,
    idConductor INT NULL,

    fechaInicio DATE,
    fechaFin DATE,

    condiciones VARCHAR(255),

    estado VARCHAR(30),

    archivoPdf VARCHAR(500),
    firmaCliente VARCHAR(500),

    fechaGeneracion DATE,

    CONSTRAINT CK_ContratoAlquiler_Estado
    CHECK (estado IN (
        'PENDIENTE_FIRMA',
        'ACTIVO',
        'FINALIZADO',
        'CANCELADO'
    )),

    FOREIGN KEY (idReserva)
    REFERENCES Reserva(idReserva),

    FOREIGN KEY (idCliente)
    REFERENCES Cliente(idCliente),

    FOREIGN KEY (idVehiculo)
    REFERENCES Vehiculo(idVehiculo),

    FOREIGN KEY (idPropietario)
    REFERENCES Propietario(idPropietario),

    FOREIGN KEY (idConductor)
    REFERENCES Conductor(idConductor)
);
GO

-- =========================================
-- TABLA ORDEN DE PAGO
-- =========================================

CREATE TABLE OrdenDePago (
    idOrdenPago INT PRIMARY KEY IDENTITY(1,1),

    idReserva INT UNIQUE,

    fechaEmision DATE,

    total DECIMAL(10,2),

    detalles VARCHAR(255),

    estado VARCHAR(20),

    rutaPdf VARCHAR(500),

    CONSTRAINT CK_OrdenPago_Estado
    CHECK (estado IN (
        'ACTIVA',
        'PAGADA',
        'ANULADA'
    )),

    FOREIGN KEY (idReserva)
    REFERENCES Reserva(idReserva)
);
GO

-- =========================================
-- TABLA METODO PAGO
-- =========================================

CREATE TABLE MetodoPago (
    idMetodo INT PRIMARY KEY IDENTITY(1,1),

    tipo VARCHAR(50) NOT NULL,
    descripcion VARCHAR(100),

    activo BIT NOT NULL,

    CONSTRAINT UQ_MetodoPago_Tipo UNIQUE(tipo)
);
GO

-- =========================================
-- TABLA PAGO
-- =========================================

CREATE TABLE Pago (
    idPago INT PRIMARY KEY IDENTITY(1,1),

    idReserva INT,
    idOrdenPago INT,
    idMetodo INT,

    monto DECIMAL(10,2),

    fechaHoraPago DATETIME,

    estado VARCHAR(20),

    CONSTRAINT CK_Pago_Estado
    CHECK (estado IN (
        'PENDIENTE',
        'PAGADO',
        'RECHAZADO',
        'REEMBOLSADO',
        'CANCELADO'
    )),

    FOREIGN KEY (idReserva)
    REFERENCES Reserva(idReserva),

    FOREIGN KEY (idOrdenPago)
    REFERENCES OrdenDePago(idOrdenPago),

    FOREIGN KEY (idMetodo)
    REFERENCES MetodoPago(idMetodo)
);
GO

-- =========================================
-- TABLA NOTIFICACION
-- =========================================

CREATE TABLE Notificacion (
    idNotificacion INT PRIMARY KEY IDENTITY(1,1),

    idReserva INT NULL,
    idPago INT NULL,

    tipo VARCHAR(50),
    mensaje VARCHAR(255),

    fechaHoraEnvio DATETIME,

    leida BIT DEFAULT 0,

    FOREIGN KEY (idReserva)
    REFERENCES Reserva(idReserva),

    FOREIGN KEY (idPago)
    REFERENCES Pago(idPago)
);
GO

-- =========================================
-- TABLA TEMPORAL
-- =========================================

CREATE TABLE #TempMetodoPago (
    tipo VARCHAR(50),
    descripcion VARCHAR(100),
    activo BIT
);
GO

-- =========================================
-- CARGAR CSV
-- =========================================

BULK INSERT #TempMetodoPago
FROM 'C:\Users\Boris Jimenez Herrer\Downloads\carman\Database\metodos_pago_bulk.csv'
WITH (
    FORMAT = 'CSV',
    FIRSTROW = 2,
    CODEPAGE = '65001'
);
GO

-- =========================================
-- INSERTAR EN TABLA REAL
-- =========================================

INSERT INTO MetodoPago (
    tipo,
    descripcion,
    activo
)
SELECT
    tipo,
    descripcion,
    activo
FROM #TempMetodoPago;
GO

-- =========================================
-- VERIFICAR
-- =========================================

SELECT * FROM MetodoPago;
GO

