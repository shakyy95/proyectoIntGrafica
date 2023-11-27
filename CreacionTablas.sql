USE trabajo_integrador;
 CREATE USER 'usuario'@'localhost' IDENTIFIED BY '12345678';
 GRANT ALL PRIVILEGES ON *.* TO 'usuario'@'localhost' WITH GRANT OPTION;
 FLUSH PRIVILEGES;


-- Creación de la tabla para TamanioPizza
CREATE TABLE `tamaniopizza` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `cantPorciones` int(11) NOT NULL,
  `habilitado` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- Creación de la tabla para TipoPizza
CREATE TABLE `tipopizza` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `descripcion` varchar(255) NOT NULL,
  `habilitado` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- Creación de la tabla para VariedadPizza
CREATE TABLE `variedadpizza` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `ingredientes` varchar(1000) DEFAULT NULL,
  `habilitado` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Creación de la tabla para Pizza
CREATE TABLE `pizza` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `tipoPizzaId` int(11) DEFAULT NULL,
  `variedadPizzaId` int(11) DEFAULT NULL,
  `tamanioPizzaId` int(11) DEFAULT NULL,
  `habilitado` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`),
  KEY `tipoPizzaId` (`tipoPizzaId`),
  KEY `variedadPizzaId` (`variedadPizzaId`),
  KEY `tamanioPizzaId` (`tamanioPizzaId`),
  CONSTRAINT `pizza_ibfk_1` FOREIGN KEY (`tipoPizzaId`) REFERENCES `tipopizza` (`id`),
  CONSTRAINT `pizza_ibfk_2` FOREIGN KEY (`variedadPizzaId`) REFERENCES `variedadpizza` (`id`),
  CONSTRAINT `pizza_ibfk_3` FOREIGN KEY (`tamanioPizzaId`) REFERENCES `tamaniopizza` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- Creación de la tabla para EstadoPedido
CREATE TABLE `estadopedido` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` enum('REGISTRADO','PREPARACION','LISTO_PARA_ENTREGAR','ENTREGADO','PENDIENTE_PAGO','PAGADO','CANCELADO') NOT NULL,
  `descripcion` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- Creación de la tabla para Factura
CREATE TABLE `factura` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fechaHoraEmision` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- Creación de la tabla para Pedido
CREATE TABLE `pedido` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fechaHoraCreacion` datetime NOT NULL,
  `fechaHoraEntrega` datetime DEFAULT NULL,
  `nombreCliente` varchar(255) NOT NULL,
  `facturaId` int(11) DEFAULT NULL,
  `estadoPedidoId` int(11) DEFAULT NULL,
  `pagado` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `facturaId` (`facturaId`),
  KEY `estadoPedidoId` (`estadoPedidoId`),
  CONSTRAINT `pedido_ibfk_1` FOREIGN KEY (`facturaId`) REFERENCES `factura` (`id`),
  CONSTRAINT `pedido_ibfk_2` FOREIGN KEY (`estadoPedidoId`) REFERENCES `estadopedido` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- Creación de la tabla para DetallePedido
CREATE TABLE `detallepedido` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cantidad` int(11) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `pizzaId` int(11) DEFAULT NULL,
  `pedidoId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `pizzaId` (`pizzaId`),
  KEY `pedido_fk_idx` (`pedidoId`),
  CONSTRAINT `detallepedido_ibfk_2` FOREIGN KEY (`pizzaId`) REFERENCES `pizza` (`id`),
  CONSTRAINT `pedido_fk` FOREIGN KEY (`pedidoId`) REFERENCES `pedido` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- Creación de la tabla para permiso
CREATE TABLE `permiso` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- Creación de la tabla rol
CREATE TABLE `rol` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- Creación de la tabla Usuario
CREATE TABLE `usuario` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `rol_id` int(11) DEFAULT NULL,
  `clave` varchar(255) DEFAULT NULL,
  `habilitado` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rol_id` (`rol_id`),
  CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`rol_id`) REFERENCES `rol` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



-- Creación de la tabla Permiso_Rol
CREATE TABLE `permiso_rol` (
  `permiso_id` int(11) NOT NULL,
  `rol_id` int(11) NOT NULL,
  PRIMARY KEY (`permiso_id`,`rol_id`),
  KEY `rol_id` (`rol_id`),
  CONSTRAINT `permiso_rol_ibfk_1` FOREIGN KEY (`permiso_id`) REFERENCES `permiso` (`id`),
  CONSTRAINT `permiso_rol_ibfk_2` FOREIGN KEY (`rol_id`) REFERENCES `rol` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



