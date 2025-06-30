package mantenimiento.Cibertec.Proyecto.Service;

import mantenimiento.Cibertec.Proyecto.DTO.*;
import mantenimiento.Cibertec.Proyecto.Entity.Equipos;
import mantenimiento.Cibertec.Proyecto.Repositorio.EquiposRepositorio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipoServiceImplement implements EquipoService{
    private final EquiposRepositorio equiposRepositorio;

    public EquipoServiceImplement(EquiposRepositorio equiposRepositorio) {
        this.equiposRepositorio = equiposRepositorio;
    }

    @Override
    public Page<EquiposDTO> listarEquipos(Pageable pageable) {
        return  equiposRepositorio.findAll(pageable).map(EquipoMapper::toDTO);
    }

    @Override
    public List<EquiposDTO> listaEquipos() {
        return EquipoMapper.toDTOList(equiposRepositorio.findAll());
    }

    @Override
    public List<EquiposDTO> listarEquiposNombre(String nombre) {
        List<Equipos> equipos = equiposRepositorio.findByNombreContaining(nombre);
        return EquipoMapper.toDTOList(equipos);
    }

    @Override
    public void eliminarEquipo(Integer id) {
       equiposRepositorio.deleteById(id);
    }

    @Override
    public EquiposSaveDTO guardarEquipo(EquiposSaveDTO saveDTO) {
        Equipos equipos = EquipoMapper.saveEntity(saveDTO);
        Equipos equiposreserva = equiposRepositorio.save(equipos);
        return EquipoMapper.saveDTO(equiposreserva);
    }

    @Override
    public EquiposUpdateDTO actualizarEquipos(EquiposUpdateDTO equiposDTO) {

        Optional<Equipos> optionalEquipo = equiposRepositorio.findById(equiposDTO.getId());
        if (!optionalEquipo.isPresent()) {
            throw new RuntimeException("Equipo no encontrado con ID: " + equiposDTO.getId());
        }
        Equipos equipoExistente = optionalEquipo.get();
        EquipoMapper.patchEntity(equipoExistente, equiposDTO);
        Equipos equipoGuardado = equiposRepositorio.save(equipoExistente);
        return EquipoMapper.patchDTO(equipoGuardado);
    }

    @Override
    public EquiposDTO buscarPorId(Integer id) {
        Equipos equipos = equiposRepositorio.findById(id).orElse(null);
        if(equipos != null){
            return EquipoMapper.toDTO(equipos);
        }
        return  null;
    }

    @Override
    public List<EquipoOpcionDTO> listarOpciones() {
        return equiposRepositorio.findAll().stream().map(equipo -> {
            EquipoOpcionDTO dto = new EquipoOpcionDTO();
            dto.setId(equipo.getId());
            dto.setNombre(equipo.getNombre());
            dto.setSku(equipo.getSku());
            return dto;
        }).collect(Collectors.toList());
    }


}
