package mantenimiento.Cibertec.Proyecto.Service;

import mantenimiento.Cibertec.Proyecto.DTO.ActualizarMantenimientoDTO;
import mantenimiento.Cibertec.Proyecto.DTO.MantenimientoDTO;
import mantenimiento.Cibertec.Proyecto.Entity.Equipos;
import mantenimiento.Cibertec.Proyecto.Entity.Mantenimiento;
import mantenimiento.Cibertec.Proyecto.Entity.Usuarios;
import mantenimiento.Cibertec.Proyecto.Repositorio.EquiposRepositorio;
import mantenimiento.Cibertec.Proyecto.Repositorio.MantenimientoRepositorio;
import mantenimiento.Cibertec.Proyecto.Repositorio.UsuariosRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class MantenimientoServicioImpl implements MantenimientoServicio{

    private final MantenimientoRepositorio mantenimientoRepositorio;
    private final EquiposRepositorio equiposRepositorio;
    private final UsuariosRepositorio usuariosRepositorio;

    public MantenimientoServicioImpl(MantenimientoRepositorio mantenimientoRepositorio, EquiposRepositorio equiposRepositorio, UsuariosRepositorio usuariosRepositorio) {
        this.mantenimientoRepositorio = mantenimientoRepositorio;
        this.equiposRepositorio = equiposRepositorio;
        this.usuariosRepositorio = usuariosRepositorio;
    }

    @Override
    public List<MantenimientoDTO> listarMantenimiento() {
        List<Mantenimiento> mantenimientos = mantenimientoRepositorio.findAll();
        return mantenimientos.stream().map(m->{
            MantenimientoDTO dto = new MantenimientoDTO();
            dto.setId(m.getId());
            dto.setNombreEquipo(m.getEquipos().getNombre());
            dto.setNombreTecnico(m.getUsuarios().getNombre());
            dto.setTipoMantenimiento(m.getTipo_mantenimiento());
            dto.setEstado(m.getEstado());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void editarMantenimiento(int id, String descripcion, String nuevoEstado) {

        Mantenimiento m = mantenimientoRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado"));
        m.setEstado(nuevoEstado);
        if (descripcion != null && !descripcion.isBlank()) {
            String nuevaDescripcion = m.getDescripcion() + "\nComentario: " + descripcion;
            m.setDescripcion(nuevaDescripcion);
        }
        mantenimientoRepositorio.save(m);
    }


    @Override
    public Mantenimiento guardarMantenimiento(MantenimientoDTO dto) {
        Equipos equipo = equiposRepositorio.findByNombre(dto.getNombreEquipo())
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        Usuarios tecnico = usuariosRepositorio.findByNombre(dto.getNombreTecnico())
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));

        Mantenimiento m = new Mantenimiento();
        m.setEquipos(equipo);
        m.setUsuarios(tecnico);
        m.setTipo_mantenimiento(dto.getTipoMantenimiento());
        m.setEstado(dto.getEstado());
        m.setSincronizado(true);

        return mantenimientoRepositorio.save(m);
    }

    @Override
    public void eliminarMantenimiento(int id) {
        mantenimientoRepositorio.deleteById(id);
    }

}
