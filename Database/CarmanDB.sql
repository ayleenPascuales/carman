CREATE DATABASE CarmanDB;
GO

USE CarmanDB;
GO

CREATE TABLE Persona (
    idPersona INT PRIMARY KEY,
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    email VARCHAR(100),
    contraseña VARCHAR(100),
    telefono VARCHAR(20),
    fechaRegistro DATE,
    estado VARCHAR(50) Check (estado in('Activo','Suspendido','Inactivo'))
);
ALTER TABLE Persona
ADD edad INT; 
ALTER TABLE Persona
ADD ciudad VARCHAR(50); 
EXEC sp_rename 'Persona.contraseña', 'contrasena', 'COLUMN';

GO

CREATE TABLE Licencia (
    idLicencia INT PRIMARY KEY IDENTITY(1,1),
    numero VARCHAR(50),
    fechaExpiracion DATE,
    estado VARCHAR(50) check (estado in('Vigente','Vencida','Suspendida')),
    tipoLicencia VARCHAR(50)
);
ALTER TABLE Licencia
ADD foto VARCHAR(500);
ALTER TABLE Licencia
ADD CONSTRAINT uk_licencia_numero UNIQUE (numero);
EXEC sp_rename 'Licencia.foto', 'fotoLicencia', 'COLUMN';
ALTER TABLE Licencia
DROP COLUMN foto;

GO

CREATE TABLE Cliente (
    idCliente INT PRIMARY KEY,
    idPersona INT,
    idLicencia INT,

    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona),
    FOREIGN KEY (idLicencia) REFERENCES Licencia(idLicencia)
);

GO

CREATE TABLE Conductor (
    idConductor INT PRIMARY KEY,
    idPersona INT,
    idLicencia INT,
    disponibilidad BIT,
    calificacion DECIMAL(3,2),

    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona),
    FOREIGN KEY (idLicencia) REFERENCES Licencia(idLicencia)
);

GO

CREATE TABLE Propietario (
    idPropietario INT PRIMARY KEY,
    idPersona INT,
    idLicencia INT,
    tarjetaPropiedad VARCHAR(100),
    totalPrestamos INT,

    FOREIGN KEY (idPersona) REFERENCES Persona(idPersona),
    FOREIGN KEY (idLicencia) REFERENCES Licencia(idLicencia)
);
ALTER TABLE Propietario
ALTER COLUMN tarjetaPropiedad VARCHAR(MAX);

GO

CREATE TABLE Vehiculo (
    idVehiculo INT PRIMARY KEY IDENTITY(1,1),
    idPropietario INT,
    placa VARCHAR(20),
    marca VARCHAR(50),
    modelo VARCHAR(50),
    anio INT,
    capacidad INT,
    tipoVehiculo VARCHAR(50),
    estado VARCHAR(50) check (estado in('Activo','inactivo','En_mantenimiento')),

    FOREIGN KEY (idPropietario) REFERENCES Propietario(idPropietario)
);
ALTER TABLE Vehiculo
ADD CONSTRAINT placa UNIQUE(placa);
ALTER TABLE Vehiculo
ADD foto VARCHAR(500); 
GO


CREATE TABLE estado_tecnico_vehiculo (
    idEstado INT PRIMARY KEY IDENTITY(1,1),
    idVehiculo INT,
    kilometraje DECIMAL(10,2),
    estadoMotor VARCHAR(50),
    estadoFrenos VARCHAR(50),
    estadoLlantas VARCHAR(50),
    estadoBateria DECIMAL(5,2),
    necesitaMantenimiento BIT,

    FOREIGN KEY (idVehiculo) REFERENCES Vehiculo(idVehiculo)
);
ALTER TABLE estado_tecnico_vehiculo
ADD fechaRegistro DATETIME;

GO

CREATE TABLE Telemetria (
    idTelemetria INT PRIMARY KEY IDENTITY(1,1),
    idVehiculo INT,
    latitud DECIMAL(10,6),
    longitud DECIMAL(10,6),
    velocidad DECIMAL(10,2),
    timestampRegistro DATETIME,

    FOREIGN KEY (idVehiculo) REFERENCES Vehiculo(idVehiculo)
);

GO

CREATE TABLE Ruta (
    idRuta INT PRIMARY KEY IDENTITY(1,1),
    origen VARCHAR(100),
    destino VARCHAR(100),
    distancia DECIMAL(10,2),
    duracion INT,
    fecha DATE
);
ALTER TABLE Ruta
ADD origenLon DECIMAL(10,6);

ALTER TABLE Ruta
ADD origenLat DECIMAL(10,6);

ALTER TABLE Ruta
ADD destinoLon DECIMAL(10,6);

ALTER TABLE Ruta
ADD destinoLat DECIMAL(10,6);
GO

CREATE TABLE Reserva (
    idReserva INT PRIMARY KEY IDENTITY(1,1),
    idCliente INT,
    idVehiculo INT,
    idConductor INT NULL,
    idRuta INT,

    horaEntrega TIME,
    fechaReserva DATE,
    fechaServicio DATE,
    estado VARCHAR(50) check (estado in('Pendiente','Confirmada','Rechazada','Finalizada','Cancelada','EnCurso')),

    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente),
    FOREIGN KEY (idVehiculo) REFERENCES Vehiculo(idVehiculo),
    FOREIGN KEY (idConductor) REFERENCES Conductor(idConductor),
    FOREIGN KEY (idRuta) REFERENCES Ruta(idRuta)
);
ALTER TABLE Reserva
ADD PrecioEstimado DECIMAL(10,2); 
EXEC sp_rename 'Reserva.PrecioEstimado', 'precioEstimado', 'COLUMN';
GO

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

    FOREIGN KEY (idReserva) REFERENCES Reserva(idReserva),
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente),
    FOREIGN KEY (idVehiculo) REFERENCES Vehiculo(idVehiculo),
    FOREIGN KEY (idPropietario) REFERENCES Propietario(idPropietario),
    FOREIGN KEY (idConductor) REFERENCES Conductor(idConductor)
);
ALTER TABLE ContratoAlquiler
ADD CONSTRAINT CK_ContratoAlquiler_Estado
CHECK (estado IN ('PENDIENTE_FIRMA', 'ACTIVO', 'FINALIZADO', 'CANCELADO'));

ALTER TABLE ContratoAlquiler
ADD archivoPdf VARCHAR(500);

ALTER TABLE ContratoAlquiler
ADD firmaCliente VARCHAR(500);

ALTER TABLE ContratoAlquiler
ADD fechaGeneracion DATE;

GO

CREATE TABLE OrdenDePago (
    idOrdenPago INT PRIMARY KEY IDENTITY(1,1),
    idReserva INT UNIQUE,
    fechaEmision DATE,
    total DECIMAL(10,2),
    detalles VARCHAR(255),

    FOREIGN KEY (idReserva) REFERENCES Reserva(idReserva)
);
ALTER TABLE OrdenDePago ADD estado VARCHAR(20);
ALTER TABLE OrdenDePago ADD CONSTRAINT CK_OrdenPago_Estado CHECK (estado IN ('ACTIVA','PAGADA','ANULADA'));
ALTER TABLE OrdenDePago ADD rutaPdf VARCHAR(500);
GO


CREATE TABLE MetodoPago (
    idMetodo INT PRIMARY KEY IDENTITY(1,1),
    tipo VARCHAR(50),
    descripcion VARCHAR(100),
    activo BIT
);
ALTER TABLE MetodoPago
ADD CONSTRAINT UQ_MetodoPago_Tipo UNIQUE(tipo);

GO

CREATE TABLE Pago (
    idPago INT PRIMARY KEY IDENTITY(1,1),
    idReserva INT,
    idOrdenPago INT,
    idMetodo INT,

    monto DECIMAL(10,2),
    fechaHoraPago DATETIME,
    estado VARCHAR(50) check (estado in('Pendiente','Rechazado','Reembolsado','Cancelado','Pago')),

    FOREIGN KEY (idReserva) REFERENCES Reserva(idReserva),
    FOREIGN KEY (idOrdenPago) REFERENCES OrdenDePago(idOrdenPago),
    FOREIGN KEY (idMetodo) REFERENCES MetodoPago(idMetodo)
);
ALTER TABLE Pago
ADD CONSTRAINT CK_Pago_Estado
CHECK (estado IN ('PENDIENTE','PAGADO','RECHAZADO','REEMBOLSADO', 'CANCELADO'));
GO

CREATE TABLE Notificacion (
    idNotificacion INT PRIMARY KEY IDENTITY(1,1),
    idReserva INT NULL,
    idPago INT NULL,

    tipo VARCHAR(50),
    mensaje VARCHAR(255),
    fechaHoraEnvio DATETIME,

    FOREIGN KEY (idReserva) REFERENCES Reserva(idReserva),
    FOREIGN KEY (idPago) REFERENCES Pago(idPago)
);
ALTER TABLE Notificacion ADD leida BIT;
GO

CREATE TABLE #TempMetodoPago (
    tipo VARCHAR(50),
    descripcion VARCHAR(100),
    activo BIT
);
go
BULK INSERT #TempMetodoPago
FROM 'C:\PROYECTO_CARMAN\metodos_pago_bulk.csv'
WITH (
    FIRSTROW = 2,
    FIELDTERMINATOR = ',',
    ROWTERMINATOR = '0x0a'
);
go
INSERT INTO MetodoPago(tipo, descripcion, activo)
SELECT tipo, descripcion, activo
FROM #TempMetodoPago;
go
SELECT * FROM MetodoPago;
go


