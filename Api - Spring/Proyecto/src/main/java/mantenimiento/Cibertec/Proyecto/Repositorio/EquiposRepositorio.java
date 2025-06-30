package mantenimiento.Cibertec.Proyecto.Repositorio;

import mantenimiento.Cibertec.Proyecto.Entity.Equipos;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EquiposRepositorio extends JpaRepository<Equipos,Integer> {

    List<Equipos> findByNombreContaining(String nombre);
    Optional<Equipos> findByNombre(String nombre);
}
