Use trabajo_integrador;

-- Insertar roles en la tabla Rol
INSERT INTO Rol (id, nombre) VALUES
(1, 'Vendedor'),
(2, 'Responsable de Cocina'),
(3, 'Responsable de la Pizzería');
(4, 'Dueño pizzeria');

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
(8, 4); -- Rol "Dueño pizzeria" tiene permiso "Ver estadísticas"

-- Crear un usuario con el rol "Vendedor" y establecer una contraseña inicial
INSERT INTO Usuario (id, nombre, rol_id, clave, habilitado) VALUES
(1, 'Usuario Vendedor', 1, '123456',1);

-- Crear un usuario con el rol "Responsable de Cocina" y establecer una contraseña inicial
INSERT INTO Usuario (id, nombre, rol_id, clave,habilitado) VALUES
(2, 'Usuario Cocina', 2, '123456',1);

-- Crear un usuario con el rol "Responsable de la Pizzería" y establecer una contraseña inicial
INSERT INTO Usuario (id, nombre, rol_id, clave,habilitado) VALUES
(3, 'Usuario Pizzería', 3, '123456',1);

-- Crear un usuario con el rol "Responsable de la Pizzería" y establecer una contraseña inicial
INSERT INTO Usuario (id, nombre, rol_id, clave,habilitado) VALUES
(4, 'Dueño', 4, '123456',1);


-- Insertar tipos de pizzas
INSERT INTO tipopizza (id,nombre, descripcion)
VALUES 
    (1,'A la Piedra', 'Pizza cocinada a alta temperatura sobre piedra.'),
    (2,'A la Parrilla', 'Pizza cocinada en parrilla.'),
    (3,'De Molde', 'Pizza de masa más densa, cocida en molde.');

-- Insertar tamaño pizzas
INSERT INTO tamaniopizza (id,nombre, cantPorciones)
VALUES 
    (1,'8 Porciones', 8),
    (2,'10 Porciones', 10),
    (3,'12 Porciones', 12);

-- Insertar variedades pizzas
INSERT INTO variedadpizza (id,nombre, ingredientes)
VALUES 
    (1,'Margarita', 'Salsa de tomate, mozzarella, albahaca fresca.'),
    (2,'Napolitana', 'Salsa de tomate, mozzarella, anchoas, aceitunas negras, orégano.'),
    (3,'Cuatro Quesos', 'Mozzarella, gorgonzola, parmesano, provolone.');


-- Insertar una pizza de Margarita, tamaño 8 porciones, a la piedra, con un precio de $10.99
INSERT INTO pizza (nombre, precio, tipoPizzaId, variedadPizzaId, tamanioPizzaId)
VALUES ('Margarita 8 A la Piedra', 1200, 1, 1, 1);

-- Insertar una pizza de Napolitana, tamaño 10 porciones, a la parrilla, con un precio de $12.99
INSERT INTO pizza (nombre, precio, tipoPizzaId, variedadPizzaId, tamanioPizzaId)
VALUES ('Napolitana 10 A la Parrilla', 1400, 2, 2, 2);

-- Insertar una pizza de Cuatro Quesos, tamaño 12 porciones, de molde, con un precio de $15.99
INSERT INTO pizza (nombre, precio, tipoPizzaId, variedadPizzaId, tamanioPizzaId)
VALUES ('Cuatro Quesos 12 De Molde', 1700, 3, 3, 3);

-- Insert estados pedidos
INSERT INTO estadopedido (id,nombre, descripcion)
VALUES 
    (1,'REGISTRADO', 'Pedido registrado'),
    (2,'PREPARACION', 'En preparación'),
    (3,'LISTO_PARA_ENTREGAR', 'Listo para entregar'),
    (4,'ENTREGADO', 'Entregado'),
    (5,'PENDIENTE_PAGO', 'Pendiente de pago'),
    (6,'PAGADO', 'Pagado'),
    (7,'CANCELADO', 'Pedido cancelado');
