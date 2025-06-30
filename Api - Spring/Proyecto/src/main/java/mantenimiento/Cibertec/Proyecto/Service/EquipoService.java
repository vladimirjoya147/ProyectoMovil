package mantenimiento.Cibertec.Proyecto.Service;

import mantenimiento.Cibertec.Proyecto.DTO.EquipoOpcionDTO;
import mantenimiento.Cibertec.Proyecto.DTO.EquiposDTO;
import mantenimiento.Cibertec.Proyecto.DTO.EquiposSaveDTO;
import mantenimiento.Cibertec.Proyecto.DTO.EquiposUpdateDTO;
import mantenimiento.Cibertec.Proyecto.Entity.Equipos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EquipoService {

    public Page<EquiposDTO> listarEquipos(Pageable pageable);

    List<EquiposDTO> listaEquipos();

    public List<EquiposDTO> listarEquiposNombre(String nombre);
    public void eliminarEquipo(Integer id);

    public EquiposSaveDTO guardarEquipo(EquiposSaveDTO equiposSaveDTO);

    public EquiposUpdateDTO actualizarEquipos(EquiposUpdateDTO equipos);

    public EquiposDTO buscarPorId(Integer id);

    public List<EquipoOpcionDTO> listarOpciones();

}
