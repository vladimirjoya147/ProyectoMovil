package mantenimiento.Cibertec.Proyecto.Repositorio;

import mantenimiento.Cibertec.Proyecto.Entity.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MantenimientoRepositorio extends JpaRepository<Mantenimiento, Integer> {
}
