CREATE TABLE reporte_mantenimiento (
  id INT PRIMARY KEY AUTO_INCREMENT,
  fecha DATE,
  descripcion TEXT,
  equipo_id INT,
  tecnico_id INT,
  tipo_mantenimiento VARCHAR(50),
  estado VARCHAR(50),
  sincronizado BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (equipo_id) REFERENCES equipo(id),
  FOREIGN KEY (tecnico_id) REFERENCES usuario(id)
);
