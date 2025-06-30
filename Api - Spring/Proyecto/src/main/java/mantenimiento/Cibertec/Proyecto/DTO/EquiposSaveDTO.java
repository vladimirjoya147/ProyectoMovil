package mantenimiento.Cibertec.Proyecto.DTO;

import lombok.Data;

@Data
public class EquiposSaveDTO {
    private String sku;
    private String nombre;
    private String tipo;
    private String descripcion;
    private String sede;
}
