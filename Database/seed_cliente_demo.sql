/*
  Cliente demo mínimo — CarmanDB
  Login: demo.cliente@carman.com / Demo123!
  (Usa columna Persona.contrasena sin ñ)
*/

USE CarmanDB;
GO

SET NOCOUNT ON;

DECLARE @idPersona INT = 99001;
DECLARE @idCliente INT = 99001;
DECLARE @idLicencia INT;
DECLARE @emailDemo VARCHAR(100) = 'demo.cliente@carman.com';
DECLARE @estadoPersona NVARCHAR(20) = N'Activo';
DECLARE @estadoLicencia NVARCHAR(20) = N'Vigente';

IF EXISTS (SELECT 1 FROM Persona WHERE email = @emailDemo)
BEGIN
    PRINT 'Ya existe una persona con ese email.';
    SELECT c.idCliente, c.idPersona FROM Cliente c
    INNER JOIN Persona p ON p.idPersona = c.idPersona
    WHERE p.email = @emailDemo;
    RETURN;
END

IF EXISTS (
    SELECT 1 FROM sys.check_constraints cc
    INNER JOIN sys.columns c ON c.object_id = cc.parent_object_id AND c.column_id = cc.parent_column_id
    WHERE cc.parent_object_id = OBJECT_ID(N'dbo.Persona') AND c.name = N'estado'
      AND cc.definition LIKE N'%ACTIVO%'
)
    SET @estadoPersona = N'ACTIVO';

IF EXISTS (
    SELECT 1 FROM sys.check_constraints cc
    INNER JOIN sys.columns c ON c.object_id = cc.parent_object_id AND c.column_id = cc.parent_column_id
    WHERE cc.parent_object_id = OBJECT_ID(N'dbo.Licencia') AND c.name = N'estado'
      AND cc.definition LIKE N'%VIGENTE%'
)
    SET @estadoLicencia = N'VIGENTE';

INSERT INTO Licencia (numero, fechaExpiracion, estado, tipoLicencia)
VALUES ('DEMO-LIC-99001', DATEADD(YEAR, 5, CAST(GETDATE() AS DATE)), @estadoLicencia, 'B1');
SET @idLicencia = SCOPE_IDENTITY();

INSERT INTO Persona (idPersona, nombre, apellido, email, contrasena, telefono, fechaRegistro, estado, edad)
VALUES (@idPersona, N'Demo', N'Cliente', @emailDemo, N'Demo123!', N'+57 300 0000001', CAST(GETDATE() AS DATE), @estadoPersona, 30);

IF COL_LENGTH(N'dbo.Persona', N'usuario') IS NOT NULL
    UPDATE Persona SET usuario = N'demo_cliente' WHERE idPersona = @idPersona;

INSERT INTO Cliente (idCliente, idPersona, idLicencia)
VALUES (@idCliente, @idPersona, @idLicencia);

PRINT 'Cliente demo creado — Login: demo.cliente@carman.com / Demo123!';
GO

IF NOT EXISTS (SELECT 1 FROM MetodoPago)
BEGIN
    INSERT INTO MetodoPago (tipo, descripcion, activo) VALUES
    (N'Tarjeta', N'Visa / Mastercard', 1),
    (N'Nequi', N'Billetera Nequi', 1),
    (N'PSE', N'Pago PSE', 1);
END
GO
