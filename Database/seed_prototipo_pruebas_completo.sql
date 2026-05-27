/*
  =============================================================================
  CARMAN — Datos prototipo para pruebas (SQL Server)
  Compatible con columna Persona.contrasena (sin ñ) — esquema SQLQuery1 / Hibernate
  =============================================================================

  LOGIN:
    Email:    demo.cliente@carman.com
    Password: Demo123!

  Si una ejecución anterior falló a medias, el script limpia restos PROT-* antes de insertar.
  =============================================================================
*/

USE CarmanDB;
GO

SET NOCOUNT ON;
SET XACT_ABORT ON;

IF EXISTS (SELECT 1 FROM dbo.Cliente WHERE idCliente = 91001)
BEGIN
    PRINT 'Prototipo ya cargado (idCliente = 91001). Usa el bloque LIMPIEZA al final para recargar.';
    RETURN;
END

BEGIN TRY
    BEGIN TRANSACTION;

    /* Limpieza de ejecución parcial anterior */
    DELETE FROM dbo.Notificacion WHERE idReserva IN (95002, 95003) OR idPago = 97001;
    DELETE FROM dbo.Pago WHERE idPago = 97001;
    DELETE FROM dbo.OrdenDePago WHERE idOrdenPago IN (96001, 96002, 96003);
    DELETE FROM dbo.ContratoAlquiler WHERE idContratoAlquiler = 98001;
    DELETE FROM dbo.Reserva WHERE idReserva IN (95001, 95002, 95003, 95004);
    DELETE FROM dbo.Telemetria WHERE idVehiculo IN (SELECT idVehiculo FROM dbo.Vehiculo WHERE placa LIKE N'PROT-VEH-%');
    IF OBJECT_ID(N'dbo.estado_tecnico_vehiculo', N'U') IS NOT NULL
        DELETE FROM dbo.estado_tecnico_vehiculo WHERE idVehiculo IN (SELECT idVehiculo FROM dbo.Vehiculo WHERE placa LIKE N'PROT-VEH-%');
    DELETE FROM dbo.Vehiculo WHERE placa IN (N'PROT-VEH-01', N'PROT-VEH-02');
    DELETE FROM dbo.Ruta WHERE origen LIKE N'PROT-RUTA-%';
    DELETE FROM dbo.Cliente WHERE idCliente = 91001;
    DELETE FROM dbo.Conductor WHERE idConductor = 93001;
    DELETE FROM dbo.Propietario WHERE idPropietario = 92001;
    DELETE FROM dbo.Persona WHERE idPersona IN (91001, 92002, 92003);
    DELETE FROM dbo.Licencia WHERE numero LIKE N'PROT-LIC-%';

    DECLARE @licCliente INT, @licProp INT, @licCond INT;
    DECLARE @veh1 INT, @veh2 INT;
    DECLARE @ruta1 INT, @ruta2 INT, @ruta3 INT;
    DECLARE @metTarjeta INT;
    DECLARE @estadoPersona NVARCHAR(20);
    DECLARE @estadoLicencia NVARCHAR(20);

    /* Detectar valores de CHECK según tu esquema */
    IF EXISTS (
        SELECT 1 FROM sys.check_constraints cc
        INNER JOIN sys.columns c ON c.object_id = cc.parent_object_id AND c.column_id = cc.parent_column_id
        WHERE cc.parent_object_id = OBJECT_ID(N'dbo.Persona') AND c.name = N'estado'
          AND cc.definition LIKE N'%ACTIVO%'
    )
        SET @estadoPersona = N'ACTIVO';
    ELSE
        SET @estadoPersona = N'Activo';

    IF EXISTS (
        SELECT 1 FROM sys.check_constraints cc
        INNER JOIN sys.columns c ON c.object_id = cc.parent_object_id AND c.column_id = cc.parent_column_id
        WHERE cc.parent_object_id = OBJECT_ID(N'dbo.Licencia') AND c.name = N'estado'
          AND cc.definition LIKE N'%VIGENTE%'
    )
        SET @estadoLicencia = N'VIGENTE';
    ELSE
        SET @estadoLicencia = N'Vigente';

    IF COL_LENGTH(N'dbo.Persona', N'contrasena') IS NULL
    BEGIN
        RAISERROR(N'La tabla Persona no tiene columna "contrasena". Revisa el esquema de tu BD.', 16, 1);
    END

    /* 1) Licencias */
    INSERT INTO dbo.Licencia (numero, fechaExpiracion, estado, tipoLicencia)
    VALUES (N'PROT-LIC-CLI-01', DATEADD(YEAR, 5, CAST(GETDATE() AS DATE)), @estadoLicencia, N'B1');
    SET @licCliente = SCOPE_IDENTITY();

    INSERT INTO dbo.Licencia (numero, fechaExpiracion, estado, tipoLicencia)
    VALUES (N'PROT-LIC-PROP-01', DATEADD(YEAR, 5, CAST(GETDATE() AS DATE)), @estadoLicencia, N'B1');
    SET @licProp = SCOPE_IDENTITY();

    INSERT INTO dbo.Licencia (numero, fechaExpiracion, estado, tipoLicencia)
    VALUES (N'PROT-LIC-COND-01', DATEADD(YEAR, 5, CAST(GETDATE() AS DATE)), @estadoLicencia, N'B2');
    SET @licCond = SCOPE_IDENTITY();

    /* 2) Personas — columna contrasena (sin ñ) */
    INSERT INTO dbo.Persona (idPersona, nombre, apellido, email, contrasena, telefono, fechaRegistro, estado, edad)
    VALUES
    (91001, N'María', N'García', N'demo.cliente@carman.com', N'Demo123!', N'+57 300 1111111', CAST(GETDATE() AS DATE), @estadoPersona, 28),
    (92002, N'Carlos', N'Ramírez', N'demo.propietario@carman.com', N'Demo123!', N'+57 300 2222222', CAST(GETDATE() AS DATE), @estadoPersona, 42),
    (92003, N'Jorge', N'Martínez', N'demo.conductor@carman.com', N'Demo123!', N'+57 300 3333333', CAST(GETDATE() AS DATE), @estadoPersona, 35);

    IF COL_LENGTH(N'dbo.Persona', N'usuario') IS NOT NULL
    BEGIN
        UPDATE dbo.Persona SET usuario = N'demo_cliente'   WHERE idPersona = 91001;
        UPDATE dbo.Persona SET usuario = N'demo_prop'      WHERE idPersona = 92002;
        UPDATE dbo.Persona SET usuario = N'demo_conductor' WHERE idPersona = 92003;
    END

    /* 3) Cliente, Propietario, Conductor */
    INSERT INTO dbo.Cliente (idCliente, idPersona, idLicencia) VALUES (91001, 91001, @licCliente);
    INSERT INTO dbo.Propietario (idPropietario, idPersona, idLicencia, tarjetaPropiedad, totalPrestamos)
    VALUES (92001, 92002, @licProp, N'TARJ-PROT-92001', 0);
    INSERT INTO dbo.Conductor (idConductor, idPersona, idLicencia, disponibilidad, calificacion)
    VALUES (93001, 92003, @licCond, 1, 4.85);

    /* 4) Vehículos */
    INSERT INTO dbo.Vehiculo (idPropietario, placa, marca, modelo, anio, capacidad, tipoVehiculo, estado)
    VALUES (92001, N'PROT-VEH-01', N'Toyota', N'Yaris', 2023, 5, N'Sedan', N'Activo');
    SET @veh1 = SCOPE_IDENTITY();

    INSERT INTO dbo.Vehiculo (idPropietario, placa, marca, modelo, anio, capacidad, tipoVehiculo, estado)
    VALUES (92001, N'PROT-VEH-02', N'Honda', N'Civic', 2024, 5, N'Sedan', N'Activo');
    SET @veh2 = SCOPE_IDENTITY();

    /* 5) Rutas */
    INSERT INTO dbo.Ruta (origen, destino, distancia, duracion, fecha)
    VALUES (N'PROT-RUTA-A Origen', N'PROT-RUTA-A Destino', 8.5, 25, CAST(GETDATE() AS DATE));
    SET @ruta1 = SCOPE_IDENTITY();

    INSERT INTO dbo.Ruta (origen, destino, distancia, duracion, fecha)
    VALUES (N'PROT-RUTA-B Origen', N'PROT-RUTA-B Destino', 22.0, 40, CAST(GETDATE() AS DATE));
    SET @ruta2 = SCOPE_IDENTITY();

    INSERT INTO dbo.Ruta (origen, destino, distancia, duracion, fecha)
    VALUES (N'PROT-RUTA-C Origen', N'PROT-RUTA-C Destino', 15.0, 35, CAST(GETDATE() AS DATE));
    SET @ruta3 = SCOPE_IDENTITY();

    /* 6) Métodos de pago */
    IF NOT EXISTS (SELECT 1 FROM dbo.MetodoPago WHERE tipo = N'Tarjeta')
        INSERT INTO dbo.MetodoPago (tipo, descripcion, activo) VALUES (N'Tarjeta', N'Visa / Mastercard', 1);
    IF NOT EXISTS (SELECT 1 FROM dbo.MetodoPago WHERE tipo = N'Nequi')
        INSERT INTO dbo.MetodoPago (tipo, descripcion, activo) VALUES (N'Nequi', N'Billetera Nequi', 1);
    IF NOT EXISTS (SELECT 1 FROM dbo.MetodoPago WHERE tipo = N'PSE')
        INSERT INTO dbo.MetodoPago (tipo, descripcion, activo) VALUES (N'PSE', N'Pago PSE', 1);

    SET @metTarjeta = (SELECT TOP 1 idMetodo FROM dbo.MetodoPago WHERE tipo = N'Tarjeta' ORDER BY idMetodo);

    /* 7) Reservas */
    SET IDENTITY_INSERT dbo.Reserva ON;
    INSERT INTO dbo.Reserva (idReserva, idCliente, idVehiculo, idConductor, idRuta, horaEntrega, fechaReserva, fechaServicio, estado, precioEstimado)
    VALUES
    (95001, 91001, @veh1, NULL,  @ruta1, CAST('09:00' AS TIME), CAST(GETDATE() AS DATE), DATEADD(DAY, 3, CAST(GETDATE() AS DATE)), N'Pendiente',  75000.00),
    (95002, 91001, @veh1, 93001, @ruta2, CAST('10:30' AS TIME), CAST(GETDATE() AS DATE), DATEADD(DAY, 5, CAST(GETDATE() AS DATE)), N'Confirmada', 110000.00),
    (95003, 91001, @veh2, 93001, @ruta3, CAST('14:00' AS TIME), DATEADD(DAY, -10, CAST(GETDATE() AS DATE)), DATEADD(DAY, -8, CAST(GETDATE() AS DATE)), N'Finalizada', 95000.00),
    (95004, 91001, @veh2, NULL,  @ruta1, CAST('08:00' AS TIME), CAST(GETDATE() AS DATE), DATEADD(DAY, 1, CAST(GETDATE() AS DATE)), N'EnCurso', 120000.00);
    SET IDENTITY_INSERT dbo.Reserva OFF;

    /* 8) Órdenes de pago */
    SET IDENTITY_INSERT dbo.OrdenDePago ON;
    INSERT INTO dbo.OrdenDePago (idOrdenPago, idReserva, fechaEmision, total, detalles, estado, rutaPdf)
    VALUES
    (96001, 95002, CAST(GETDATE() AS DATE), 110000.00, N'Orden prototipo — reserva confirmada', N'ACTIVA', NULL),
    (96002, 95003, DATEADD(DAY, -9, CAST(GETDATE() AS DATE)), 95000.00, N'Orden prototipo — ya pagada', N'PAGADA', N'generado'),
    (96003, 95004, CAST(GETDATE() AS DATE), 120000.00, N'Orden prototipo — anulada', N'ANULADA', NULL);
    SET IDENTITY_INSERT dbo.OrdenDePago OFF;

    /* 9) Pago */
    SET IDENTITY_INSERT dbo.Pago ON;
    INSERT INTO dbo.Pago (idPago, idReserva, idOrdenPago, idMetodo, monto, fechaHoraPago, estado)
    VALUES (97001, 95003, 96002, @metTarjeta, 95000.00, DATEADD(DAY, -8, GETDATE()), N'PAGADO');
    SET IDENTITY_INSERT dbo.Pago OFF;

    /* 10) Contrato */
    IF COL_LENGTH(N'dbo.ContratoAlquiler', N'estado') IS NOT NULL
    BEGIN
        SET IDENTITY_INSERT dbo.ContratoAlquiler ON;
        INSERT INTO dbo.ContratoAlquiler (
            idContratoAlquiler, idReserva, idCliente, idVehiculo, idPropietario, idConductor,
            fechaInicio, fechaFin, condiciones, estado, archivoPdf, firmaCliente, fechaGeneracion
        )
        VALUES (
            98001, 95002, 91001, @veh1, 92001, 93001,
            DATEADD(DAY, 5, CAST(GETDATE() AS DATE)), DATEADD(DAY, 7, CAST(GETDATE() AS DATE)),
            N'Condiciones prototipo — prueba de contrato', N'PENDIENTE_FIRMA', NULL, NULL, CAST(GETDATE() AS DATE)
        );
        SET IDENTITY_INSERT dbo.ContratoAlquiler OFF;
    END

    /* 11) Notificaciones */
    INSERT INTO dbo.Notificacion (idReserva, idPago, tipo, mensaje, fechaHoraEnvio, leida)
    VALUES
    (95002, NULL, N'RESERVA', N'Reserva #95002 confirmada (prototipo)', GETDATE(), 0),
    (95003, 97001, N'PAGO', N'Pago #97001 registrado correctamente', DATEADD(HOUR, -2, GETDATE()), 1);

    /* 12) Telemetría */
    INSERT INTO dbo.Telemetria (idVehiculo, latitud, longitud, velocidad, timestampRegistro)
    VALUES (@veh1, 6.244200, -75.581200, 45.00, GETDATE());

    IF OBJECT_ID(N'dbo.estado_tecnico_vehiculo', N'U') IS NOT NULL
        INSERT INTO dbo.estado_tecnico_vehiculo (idVehiculo, kilometraje, estadoMotor, estadoFrenos, estadoLlantas, estadoBateria, necesitaMantenimiento, fechaRegistro)
        VALUES (@veh1, 45230.50, N'Bien', N'Bien', N'Bien', 12.40, 0, GETDATE());

    COMMIT TRANSACTION;

    PRINT '';
    PRINT '=== Prototipo cargado correctamente ===';
    PRINT 'Login: demo.cliente@carman.com / Demo123!';
    PRINT 'idCliente = 91001 | Reservas: 95001-95004 | Orden ACTIVA: 96001';
    PRINT 'Vehículos: ' + CAST(@veh1 AS VARCHAR(10)) + N', ' + CAST(@veh2 AS VARCHAR(10));

END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    PRINT 'ERROR — no se cargó el prototipo:';
    PRINT ERROR_MESSAGE();
END CATCH;
GO

/*
  LIMPIEZA manual (descomenta y ejecuta para volver a cargar):

USE CarmanDB;
GO
DELETE FROM dbo.Notificacion WHERE idReserva IN (95002, 95003) OR idPago = 97001;
DELETE FROM dbo.Pago WHERE idPago = 97001;
DELETE FROM dbo.OrdenDePago WHERE idOrdenPago IN (96001, 96002, 96003);
DELETE FROM dbo.ContratoAlquiler WHERE idContratoAlquiler = 98001;
DELETE FROM dbo.Reserva WHERE idReserva IN (95001, 95002, 95003, 95004);
DELETE FROM dbo.Telemetria WHERE idVehiculo IN (SELECT idVehiculo FROM dbo.Vehiculo WHERE placa LIKE N'PROT-VEH-%');
DELETE FROM dbo.estado_tecnico_vehiculo WHERE idVehiculo IN (SELECT idVehiculo FROM dbo.Vehiculo WHERE placa LIKE N'PROT-VEH-%');
DELETE FROM dbo.Vehiculo WHERE placa IN (N'PROT-VEH-01', N'PROT-VEH-02');
DELETE FROM dbo.Ruta WHERE origen LIKE N'PROT-RUTA-%';
DELETE FROM dbo.Cliente WHERE idCliente = 91001;
DELETE FROM dbo.Conductor WHERE idConductor = 93001;
DELETE FROM dbo.Propietario WHERE idPropietario = 92001;
DELETE FROM dbo.Persona WHERE idPersona IN (91001, 92002, 92003);
DELETE FROM dbo.Licencia WHERE numero LIKE N'PROT-LIC-%';
GO
*/
