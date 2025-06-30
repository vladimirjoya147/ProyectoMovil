package mantenimiento.Cibertec.Proyecto.Service;

import mantenimiento.Cibertec.Proyecto.DTO.ActualizarMantenimientoDTO;
import mantenimiento.Cibertec.Proyecto.DTO.MantenimientoDTO;
import mantenimiento.Cibertec.Proyecto.Entity.Mantenimiento;

import java.util.List;

public interface MantenimientoServicio {

    public List<MantenimientoDTO> listarMantenimiento();

    public void editarMantenimiento(int id, String descripcion, String nuevoEstado);

    public Mantenimiento guardarMantenimiento (MantenimientoDTO mantenimiento);

    public void eliminarMantenimiento (int id);
}
