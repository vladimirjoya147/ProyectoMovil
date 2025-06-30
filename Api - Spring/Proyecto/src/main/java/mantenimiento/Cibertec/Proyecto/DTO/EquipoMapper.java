package mantenimiento.Cibertec.Proyecto.DTO;

import mantenimiento.Cibertec.Proyecto.Entity.Equipos;

import java.util.List;
import java.util.stream.Collectors;

public class EquipoMapper {
    public static EquiposDTO toDTO(Equipos equipos){
        EquiposDTO dto = new EquiposDTO();
        dto.setId(equipos.getId());
        dto.setNombre(equipos.getNombre());
        dto.setTipo(equipos.getTipo());
        dto.setSede(equipos.getSede());
        dto.setSku(equipos.getSku());
        return dto;
    }

    public static Equipos toEntity(EquiposDTO dto){
        Equipos equipo = new Equipos();
        equipo.setId(dto.getId());
        equipo.setNombre(dto.getNombre());
        equipo.setTipo(dto.getTipo());
        equipo.setSede(dto.getSede());
        equipo.setSku(dto.getSku());
        return equipo;
    }

    public static EquiposSaveDTO saveDTO (Equipos equipos){
        EquiposSaveDTO dto = new EquiposSaveDTO();
        dto.setNombre(equipos.getNombre());
        dto.setSede(equipos.getSede());
        dto.setTipo(equipos.getTipo());
        dto.setSku(equipos.getSku());
        dto.setDescripcion(equipos.getDescripcion());
        return dto;
    }

    public static Equipos saveEntity (EquiposSaveDTO dto){
        Equipos equipos = new Equipos();
        equipos.setNombre(dto.getNombre());
        equipos.setSede(dto.getSede());
        equipos.setTipo(dto.getTipo());
        equipos.setSku(dto.getSku());
        equipos.setDescripcion(dto.getDescripcion());
        return equipos;
    }

    public static EquiposUpdateDTO patchDTO (Equipos equipos){
        EquiposUpdateDTO dto = new EquiposUpdateDTO();
        dto.setId(equipos.getId());
        dto.setNombre(equipos.getNombre());
        dto.setSede(equipos.getSede());
        dto.setTipo(equipos.getTipo());
        dto.setSku(equipos.getSku());
        dto.setDescripcion(equipos.getDescripcion());
        return dto;
    }
    public static void patchEntity(Equipos entity, EquiposUpdateDTO dto) {
        if (dto.getNombre() != null) entity.setNombre(dto.getNombre());
        if (dto.getTipo() != null) entity.setTipo(dto.getTipo());
        if (dto.getDescripcion() != null) entity.setDescripcion(dto.getDescripcion());
        if (dto.getSede() != null) entity.setSede(dto.getSede());
        if (dto.getSku() != null) entity.setSku(dto.getSku());
    }

    /*public static Equipos patchEntity (EquiposUpdateDTO dto){
        Equipos equipos = new Equipos();
        equipos.setId(dto.getId());
        equipos.setNombre(dto.getNombre());
        equipos.setSede(dto.getSede());
        equipos.setTipo(dto.getTipo());
        equipos.setSku(dto.getSku());
        equipos.setDescripcion(dto.getDescripcion());
        return equipos;
    }*/

    public static List<EquiposDTO> toDTOList(List<Equipos> equipos) {
        return equipos.stream()
                .map(EquipoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static List<Equipos> toEntityList(List<EquiposDTO> equipoDTOs) {
        return equipoDTOs.stream()
                .map(EquipoMapper::toEntity)
                .collect(Collectors.toList());
    }
}
