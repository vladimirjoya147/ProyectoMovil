package mantenimiento.Cibertec.Proyecto.DTO;

import lombok.Data;

import java.time.LocalDate;
@Data
public class MantenimientoDTO {
    private int id;
    private String nombreEquipo;
    private String nombreTecnico;
    private String tipoMantenimiento;
    private String estado;
}
