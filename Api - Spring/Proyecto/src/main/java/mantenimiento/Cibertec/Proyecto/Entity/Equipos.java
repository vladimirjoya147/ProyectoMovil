package mantenimiento.Cibertec.Proyecto.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "equipo")
public class Equipos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String sku;
    private String nombre;
    private String tipo;
    private String descripcion;
    private String sede;

}
