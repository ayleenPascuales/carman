-- Vehículos de referencia (presentación: BMW Serie 3, Tesla Model 3, Toyota RAV4 + catálogo)
-- Ejecutar en CarmanDB si el catálogo está vacío. Idempotente por placas PROT-CAT-*

USE CarmanDB;
GO

SET NOCOUNT ON;

IF NOT EXISTS (SELECT 1 FROM dbo.Persona WHERE email = N'propietario.catalogo@carman.com')
BEGIN
    DECLARE @idPersona INT = (SELECT COALESCE(MAX(idPersona), 0) + 1 FROM dbo.Persona);
    DECLARE @idLic INT;
    DECLARE @idProp INT = @idPersona;

    INSERT INTO dbo.Licencia (numero, fechaExpiracion, tipoLicencia, estado)
    VALUES (N'PROT-CAT-LIC', DATEADD(YEAR, 5, CAST(GETDATE() AS DATE)), N'B1', N'VIGENTE');
    SET @idLic = SCOPE_IDENTITY();

    INSERT INTO dbo.Persona (idPersona, nombre, apellido, email, contrasena, telefono, fechaRegistro, edad, estado, usuario, ciudad)
    VALUES (@idPersona, N'Propietario', N'Catálogo', N'propietario.catalogo@carman.com', N'Catalogo123!', N'3000009999',
            CAST(GETDATE() AS DATE), 35, N'ACTIVO', N'prop_catalogo', N'Medellín');

    INSERT INTO dbo.Propietario (idPropietario, idPersona, idLicencia, tarjetaPropiedad, totalPrestamos)
    VALUES (@idProp, @idPersona, @idLic, N'/uploads/registro/tarjeta/demo.pdf', 0);

    INSERT INTO dbo.Vehiculo (idPropietario, placa, marca, modelo, anio, capacidad, tipoVehiculo, estado)
    VALUES
        (@idProp, N'PROT-CAT-01', N'BMW', N'Serie 3', 2024, 5, N'Sedán', N'ACTIVO'),
        (@idProp, N'PROT-CAT-02', N'Tesla', N'Model 3', 2025, 5, N'Eléctrico', N'ACTIVO'),
        (@idProp, N'PROT-CAT-03', N'Toyota', N'RAV4', 2023, 7, N'SUV', N'ACTIVO'),
        (@idProp, N'PROT-CAT-04', N'Toyota', N'Yaris', 2022, 5, N'Sedán', N'ACTIVO'),
        (@idProp, N'PROT-CAT-05', N'Honda', N'Civic', 2023, 5, N'Sedán', N'ACTIVO'),
        (@idProp, N'PROT-CAT-06', N'Mazda', N'CX-3', 2021, 5, N'SUV', N'ACTIVO');

    PRINT 'Catálogo de presentación insertado (6 vehículos).';
END
ELSE
    PRINT 'El propietario de catálogo demo ya existe; no se insertó nada.';
GO
