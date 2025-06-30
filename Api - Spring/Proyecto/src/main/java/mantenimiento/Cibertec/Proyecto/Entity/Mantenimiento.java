package mantenimiento.Cibertec.Proyecto.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "reporte_mantenimiento")
public class Mantenimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate fecha;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipos equipos;

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private Usuarios usuarios;

    private String tipo_mantenimiento;
    private String estado;
    private Boolean sincronizado;
}
