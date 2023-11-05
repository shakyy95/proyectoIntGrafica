Use trabajo_integrador;

-- Insertar roles en la tabla Rol
INSERT INTO Rol (id, nombre) VALUES
(1, 'Vendedor'),
(2, 'Responsable de Cocina'),
(3, 'Responsable de la Pizzería');

-- Insertar permisos en la tabla Permiso
INSERT INTO Permiso (id, nombre) VALUES
(1, 'Generar factura'),
(2, 'Registrar pedido'),
(3, 'Cancelar pedido'),
(4, 'Confirmar pedido'),
(5, 'Informar cierre pedido'),
(6, 'Registrar menú'),
(7, 'Actualizar precios'),
(8, 'Ver estadísticas');

-- Asociar permisos a roles en la tabla Permiso_Rol
INSERT INTO Permiso_Rol (permiso_id, rol_id) VALUES
(1, 1), -- Rol "Vendedor" tiene permiso "Generar factura"
(2, 1), -- Rol "Vendedor" tiene permiso "Registrar pedido"
(3, 1), -- Rol "Vendedor" tiene permiso "Cancelar pedido"
(4, 2), -- Rol "Responsable de Cocina" tiene permiso "Confirmar pedido"
(5, 2), -- Rol "Responsable de Cocina" tiene permiso "Informar cierre pedido"
(6, 3), -- Rol "Responsable de la Pizzería" tiene permiso "Registrar menú"
(7, 3); -- Rol "Responsable de la Pizzería" tiene permiso "Actualizar precios"

-- Crear un usuario con el rol "Vendedor" y establecer una contraseña inicial
INSERT INTO Usuario (id, nombre, rol_id, clave) VALUES
(1, 'Usuario Vendedor', 1, '123456');

-- Crear un usuario con el rol "Responsable de Cocina" y establecer una contraseña inicial
INSERT INTO Usuario (id, nombre, rol_id, clave) VALUES
(2, 'Usuario Cocina', 2, '123456');

-- Crear un usuario con el rol "Responsable de la Pizzería" y establecer una contraseña inicial
INSERT INTO Usuario (id, nombre, rol_id, clave) VALUES
(3, 'Usuario Pizzería', 3, '123456');


