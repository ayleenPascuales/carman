-- Si solo existe fila en Persona (sin Cliente), el login entrará como INVITADO.
-- Este script crea Cliente + Licencia para idPersona = 1 (amwdjh@gmail.com)

USE CarmanDB;
GO

DECLARE @idPersona INT = 1;
DECLARE @idLic INT;
DECLARE @idCliente INT = @idPersona;

IF NOT EXISTS (SELECT 1 FROM dbo.Persona WHERE idPersona = @idPersona)
BEGIN
    PRINT 'No existe Persona con idPersona = 1';
    RETURN;
END

IF EXISTS (SELECT 1 FROM dbo.Cliente WHERE idPersona = @idPersona)
BEGIN
    PRINT 'Ya existe registro en Cliente para esta persona.';
    RETURN;
END

INSERT INTO dbo.Licencia (numero, fechaExpiracion, tipoLicencia, estado)
VALUES (N'LIC-PERSONA-1', DATEADD(YEAR, 5, CAST(GETDATE() AS DATE)), N'B1', N'VIGENTE');
SET @idLic = SCOPE_IDENTITY();

INSERT INTO dbo.Cliente (idCliente, idPersona, idLicencia)
VALUES (@idCliente, @idPersona, @idLic);

PRINT 'Cliente creado. Vuelve a iniciar sesión con amwdjh@gmail.com';
GO
