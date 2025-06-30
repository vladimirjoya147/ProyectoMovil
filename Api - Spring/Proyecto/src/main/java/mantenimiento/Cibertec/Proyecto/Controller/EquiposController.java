package mantenimiento.Cibertec.Proyecto.Controller;

import mantenimiento.Cibertec.Proyecto.DTO.EquipoOpcionDTO;
import mantenimiento.Cibertec.Proyecto.DTO.EquiposDTO;
import mantenimiento.Cibertec.Proyecto.DTO.EquiposSaveDTO;
import mantenimiento.Cibertec.Proyecto.DTO.EquiposUpdateDTO;
import mantenimiento.Cibertec.Proyecto.Entity.Equipos;
import mantenimiento.Cibertec.Proyecto.Service.EquipoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipo")
public class EquiposController {

    private final EquipoService equipoService;

    public EquiposController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }


    @GetMapping("/listar")
    public ResponseEntity<List<EquiposDTO>> listaequipos(){
        return ResponseEntity.ok(equipoService.listaEquipos());
    }

    @GetMapping
    public ResponseEntity<Page<EquiposDTO>> listarequipos (@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<EquiposDTO> equipoPaginado = equipoService.listarEquipos(pageable);
        return ResponseEntity.ok(equipoPaginado);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<EquiposDTO>> listarPorNombre (@RequestParam("nombre") String nombre){
        List<EquiposDTO> equipoPaginadoNombre = equipoService.listarEquiposNombre(nombre);
        return ResponseEntity.ok(equipoPaginadoNombre);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEquipos(@PathVariable Integer id){
        equipoService.eliminarEquipo(id);
        return ResponseEntity.ok("Equipo elminado correctamente");
    }

    @PostMapping("/registrar")
    public ResponseEntity<EquiposSaveDTO> guardaEquipo(@RequestBody EquiposSaveDTO equipos){
        EquiposSaveDTO equipoguardado = equipoService.guardarEquipo(equipos);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipoguardado);
    }

    @PatchMapping("/actualizar/{id}")
    public ResponseEntity<EquiposUpdateDTO> actualizaEquipo (@PathVariable Integer id, EquiposUpdateDTO equiposUpdateDTO){
        if (!id.equals(equiposUpdateDTO.getId())) {
            System.out.println(equiposUpdateDTO.getId());
            return ResponseEntity.badRequest().build();
        }
        EquiposUpdateDTO actualizado = equipoService.actualizarEquipos(equiposUpdateDTO);
        return ResponseEntity.ok(actualizado);

    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<EquiposDTO> buscarPorId (@PathVariable Integer id ){
        EquiposDTO equiposDTO = equipoService.buscarPorId(id);
        return ResponseEntity.ok(equiposDTO);

    }
    @GetMapping("/opciones")
    public ResponseEntity<List<EquipoOpcionDTO>> obtenerOpciones() {
        List<EquipoOpcionDTO> opciones = equipoService.listarOpciones();
        return ResponseEntity.ok(opciones);
    }

}
