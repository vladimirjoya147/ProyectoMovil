package mantenimiento.Cibertec.Proyecto.Repositorio;

import mantenimiento.Cibertec.Proyecto.Entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariosRepositorio extends JpaRepository<Usuarios, Integer> {
    Optional<Usuarios> findByCorreo(String correo);
    Optional<Usuarios> findByNombre(String nombre);
}
