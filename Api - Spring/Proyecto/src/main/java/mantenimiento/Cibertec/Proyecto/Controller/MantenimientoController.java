package mantenimiento.Cibertec.Proyecto.Controller;

import mantenimiento.Cibertec.Proyecto.DTO.ActualizarMantenimientoDTO;
import mantenimiento.Cibertec.Proyecto.DTO.MantenimientoDTO;
import mantenimiento.Cibertec.Proyecto.Entity.Mantenimiento;
import mantenimiento.Cibertec.Proyecto.Service.MantenimientoServicioImpl;
import mantenimiento.Cibertec.Proyecto.Service.UsuarioServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
public class MantenimientoController {

    private final MantenimientoServicioImpl mantenimientoService;

    public MantenimientoController(MantenimientoServicioImpl mantenimientoService) {
        this.mantenimientoService = mantenimientoService;
    }

    @GetMapping("/todas")
    public ResponseEntity<List<MantenimientoDTO>> listarMantenimientos(){
        List<MantenimientoDTO> listar = mantenimientoService.listarMantenimiento();
        return ResponseEntity.ok(listar);
    }

    @PatchMapping("/editar")
    public ResponseEntity<String> editarServicio(@RequestBody ActualizarMantenimientoDTO dto){
        mantenimientoService.editarMantenimiento(dto.getId(),dto.getDescripcion(), dto.getEstado());
        return ResponseEntity.ok("Mantenimiento actualizado");
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarMantenimiento(@RequestBody MantenimientoDTO dto) {
        mantenimientoService.guardarMantenimiento(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Mantenimiento guardado correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPorId (@PathVariable Integer id){
        mantenimientoService.eliminarMantenimiento(id);
        return ResponseEntity.ok("Mantenimiento eliminado con exito");
    }


}
