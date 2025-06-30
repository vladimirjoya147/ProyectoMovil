
CREATE TABLE usuario (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100),
  correo VARCHAR(100),
  contrasena VARCHAR(100),
  rol VARCHAR(50) 
);

CREATE TABLE equipo (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100),
  tipo VARCHAR(50), 
  descripcion TEXT,
  sku varchar(50) UNIQUE NOT NULL,
  sede VARCHAR(100)
);

CREATE TABLE reporte_mantenimiento (
  id INT PRIMARY KEY AUTO_INCREMENT,
  fecha DATE,
  descripcion TEXT,
  equipo_id INT,
  tecnico_id INT,
  tipo_mantenimiento VARCHAR(50),
  estado VARCHAR(50),
  sincronizado BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (equipo_id) REFERENCES Equipo(id),
  FOREIGN KEY (tecnico_id) REFERENCES Usuario(id)
);
