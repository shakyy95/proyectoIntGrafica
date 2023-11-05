USE trabajo_integrador;
CREATE USER 'usuario'@'localhost' IDENTIFIED BY '12345678';
GRANT ALL PRIVILEGES ON *.* TO 'usuario'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;


-- Creación de la tabla para TamanioPizza
CREATE TABLE TamanioPizza (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    cantPorciones INT NOT NULL
);

-- Creación de la tabla para TipoPizza
CREATE TABLE TipoPizza (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255) NOT NULL
);

-- Creación de la tabla para VariedadPizza
CREATE TABLE VariedadPizza (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL
);

-- Creación de la tabla para Pizza
CREATE TABLE Pizza (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    tipoPizzaId INT,
    variedadPizzaId INT,
    tamanioPizzaId INT,
    FOREIGN KEY (tipoPizzaId) REFERENCES TipoPizza(id),
    FOREIGN KEY (variedadPizzaId) REFERENCES VariedadPizza(id),
    FOREIGN KEY (tamanioPizzaId) REFERENCES TamanioPizza(id)
);

-- Creación de la tabla para EstadoPedido
CREATE TABLE EstadoPedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre ENUM('REGISTRADO', 'PREPARACION', 'LISTO_PARA_ENTREGAR', 'ENTREGADO', 'PENDIENTE_PAGO', 'PAGADO', 'CANCELADO') NOT NULL,
    descripcion VARCHAR(255) NOT NULL
);

-- Creación de la tabla para EstadoFactura
CREATE TABLE EstadoFactura (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre ENUM('GENERADA', 'PENDIENTE_FACTURACION') NOT NULL,
    descripcion VARCHAR(255) NOT NULL
);

-- Creación de la tabla para DetallePedido
CREATE TABLE DetallePedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cantidad INT NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    estadoPedidoId INT,
    pizzaId INT,
    FOREIGN KEY (estadoPedidoId) REFERENCES EstadoPedido(id),
    FOREIGN KEY (pizzaId) REFERENCES Pizza(id)
);

-- Creación de la tabla para Factura
CREATE TABLE Factura (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fechaHoraEmision DATETIME NOT NULL,
    numero INT NOT NULL,
    estadoFacturaId INT,
    FOREIGN KEY (estadoFacturaId) REFERENCES EstadoFactura(id)
);

-- Creación de la tabla para Pedido
CREATE TABLE Pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fechaHoraCreacion DATETIME NOT NULL,
    fechaHoraEntrega DATETIME,
    nombreCliente VARCHAR(255) NOT NULL,
    numero INT NOT NULL,
    facturaId INT,
    estadoPedidoId INT,
    FOREIGN KEY (facturaId) REFERENCES Factura(id),
    FOREIGN KEY (estadoPedidoId) REFERENCES EstadoPedido(id)
);

-- Creación de la tabla rol
CREATE TABLE Rol (
    id INT PRIMARY KEY,
    nombre VARCHAR(255)
);

-- Creación de la tabla Usuario
CREATE TABLE Usuario (
    id INT PRIMARY KEY,
    nombre VARCHAR(255),
    rol_id INT,
    clave VARCHAR(255),
	habilitado BOOLEAN,
    FOREIGN KEY (rol_id) REFERENCES Rol(id)
);

-- Creación de la tabla Permiso
CREATE TABLE Permiso (
    id INT PRIMARY KEY,
    nombre VARCHAR(255)
);

-- Creación de la tabla Permiso_Rol

CREATE TABLE Permiso_Rol (
    permiso_id INT,
    rol_id INT,
    PRIMARY KEY (permiso_id, rol_id),
    FOREIGN KEY (permiso_id) REFERENCES Permiso(id),
    FOREIGN KEY (rol_id) REFERENCES Rol(id)
);

