-- Un vehículo de prueba para el catálogo (/buscar)
-- Idempotente: no inserta si ya existe la placa PRU-TEST-01

USE CarmanDB;
GO

SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    IF EXISTS (SELECT 1 FROM dbo.Vehiculo WHERE placa = N'PRU-TEST-01')
    BEGIN
        PRINT 'Ya existe el vehículo PRU-TEST-01. No se insertó nada.';
        ROLLBACK TRANSACTION;
        RETURN;
    END

    DECLARE @idPersona INT = (SELECT COALESCE(MAX(idPersona), 0) + 1 FROM dbo.Persona);
    DECLARE @idPropietario INT = @idPersona;
    DECLARE @idLic INT;
    DECLARE @idVehiculo INT;

    INSERT INTO dbo.Licencia (numero, fechaExpiracion, tipoLicencia, estado)
    VALUES (N'PRU-TEST-LIC-01', DATEADD(YEAR, 5, CAST(GETDATE() AS DATE)), N'B1', N'VIGENTE');
    SET @idLic = SCOPE_IDENTITY();

    INSERT INTO dbo.Persona (idPersona, nombre, apellido, email, contrasena, telefono, fechaRegistro, edad, estado)
    VALUES (@idPersona, N'Propietario', N'Auto Prueba', N'auto.prueba@carman.com', N'Prueba123!', N'3000008888',
            CAST(GETDATE() AS DATE), 38, N'ACTIVO');

    IF COL_LENGTH('dbo.Persona', 'usuario') IS NOT NULL
        UPDATE dbo.Persona SET usuario = N'prop_auto_prueba' WHERE idPersona = @idPersona;

    IF COL_LENGTH('dbo.Persona', 'ciudad') IS NOT NULL
        UPDATE dbo.Persona SET ciudad = N'Medellín' WHERE idPersona = @idPersona;

    INSERT INTO dbo.Propietario (idPropietario, idPersona, idLicencia, tarjetaPropiedad, totalPrestamos)
    VALUES (@idPropietario, @idPersona, @idLic, N'TP-PRU-TEST-01', 0);

    INSERT INTO dbo.Vehiculo (idPropietario, placa, marca, modelo, anio, capacidad, tipoVehiculo, estado)
    VALUES (@idPropietario, N'PRU-TEST-01', N'Toyota', N'Corolla', 2022, 5, N'Sedán', N'ACTIVO');
    SET @idVehiculo = SCOPE_IDENTITY();

    IF COL_LENGTH('dbo.Vehiculo', 'foto') IS NOT NULL
        UPDATE dbo.Vehiculo SET foto = NULL WHERE idVehiculo = @idVehiculo;
        -- Sin foto: el catálogo usará imagen de referencia Toyota Corolla

    COMMIT TRANSACTION;

    PRINT 'Vehículo de prueba insertado.';
    PRINT 'Placa: PRU-TEST-01 | Toyota Corolla 2022 | idVehiculo = ' + CAST(@idVehiculo AS VARCHAR(20));
    PRINT 'Propietario: auto.prueba@carman.com / Prueba123!';
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
        ROLLBACK TRANSACTION;
    PRINT 'Error:';
    PRINT ERROR_MESSAGE();
END CATCH;
GO

UPDATE dbo.Vehiculo
SET foto = NULL
WHERE placa = N'PRU-TEST-01';
