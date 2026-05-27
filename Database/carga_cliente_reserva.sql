USE CarmanDB;
GO

SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    DECLARE @idPersonaCliente INT = 99101;
    DECLARE @idCliente INT = 99101;
    DECLARE @idPersonaProp INT = 99102;
    DECLARE @idPropietario INT = 99102;

    DECLARE @idLicCliente INT;
    DECLARE @idLicProp INT;
    DECLARE @idVehiculo INT;
    DECLARE @idRuta INT;
    DECLARE @idReserva INT;

    IF EXISTS (SELECT 1 FROM dbo.Cliente WHERE idCliente = @idCliente)
    BEGIN
        PRINT 'Ya existe el cliente 99101. No se insertan datos.';
        ROLLBACK TRANSACTION;
        RETURN;
    END

    INSERT INTO dbo.Licencia (numero, fechaExpiracion, tipoLicencia, estado)
    VALUES (N'CL-99101-LIC', DATEADD(YEAR, 5, CAST(GETDATE() AS DATE)), N'B1', N'VIGENTE');
    SET @idLicCliente = SCOPE_IDENTITY();

    INSERT INTO dbo.Licencia (numero, fechaExpiracion, tipoLicencia, estado)
    VALUES (N'PROP-99102-LIC', DATEADD(YEAR, 5, CAST(GETDATE() AS DATE)), N'B1', N'VIGENTE');
    SET @idLicProp = SCOPE_IDENTITY();

    INSERT INTO dbo.Persona (idPersona, nombre, apellido, email, contrasena, telefono, fechaRegistro, edad, estado)
    VALUES (@idPersonaCliente, N'Cliente', N'Prueba', N'cliente.prueba@carman.com', N'Prueba123!', N'3000001111', CAST(GETDATE() AS DATE), 29, N'ACTIVO');

    INSERT INTO dbo.Persona (idPersona, nombre, apellido, email, contrasena, telefono, fechaRegistro, edad, estado)
    VALUES (@idPersonaProp, N'Propietario', N'Prueba', N'propietario.prueba@carman.com', N'Prueba123!', N'3000002222', CAST(GETDATE() AS DATE), 40, N'ACTIVO');

    IF COL_LENGTH('dbo.Persona', 'usuario') IS NOT NULL
    BEGIN
        UPDATE dbo.Persona SET usuario = N'cliente_prueba_99101' WHERE idPersona = @idPersonaCliente;
        UPDATE dbo.Persona SET usuario = N'prop_prueba_99102' WHERE idPersona = @idPersonaProp;
    END

    INSERT INTO dbo.Cliente (idCliente, idPersona, idLicencia)
    VALUES (@idCliente, @idPersonaCliente, @idLicCliente);

    INSERT INTO dbo.Propietario (idPropietario, idPersona, idLicencia, tarjetaPropiedad, totalPrestamos)
    VALUES (@idPropietario, @idPersonaProp, @idLicProp, N'TP-99102', 0);

    INSERT INTO dbo.Vehiculo (idPropietario, placa, marca, modelo, anio, capacidad, tipoVehiculo, estado)
    VALUES (@idPropietario, N'PRU991', N'Toyota', N'Corolla', 2022, 5, N'SEDAN', N'ACTIVO');
    SET @idVehiculo = SCOPE_IDENTITY();

    INSERT INTO dbo.Ruta (origen, destino, origenLon, origenLat, destinoLon, destinoLat, distancia, duracion, fecha)
    VALUES (N'PROT-ORIGEN-99101', N'PROT-DESTINO-99101', -75.570000, 6.250000, -75.600000, 6.220000, 12.50, 28, CAST(GETDATE() AS DATE));
    SET @idRuta = SCOPE_IDENTITY();

    INSERT INTO dbo.Reserva (idCliente, idVehiculo, idConductor, idRuta, horaEntrega, fechaReserva, fechaServicio, precioEstimado, estado)
    VALUES (@idCliente, @idVehiculo, NULL, @idRuta, CAST('09:30' AS TIME), CAST(GETDATE() AS DATE), DATEADD(DAY, 2, CAST(GETDATE() AS DATE)), 98000.00, N'PENDIENTE');
    SET @idReserva = SCOPE_IDENTITY();

    COMMIT TRANSACTION;

    PRINT 'Cliente y reserva creados correctamente.';
    PRINT 'idCliente = ' + CAST(@idCliente AS VARCHAR(20));
    PRINT 'idReserva = ' + CAST(@idReserva AS VARCHAR(20));
    PRINT 'Login: cliente.prueba@carman.com / Prueba123!';
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
        ROLLBACK TRANSACTION;

    PRINT 'Error al cargar cliente y reserva:';
    PRINT ERROR_MESSAGE();
END CATCH;
GO
