package mantenimiento.Cibertec.Proyecto.Controller;

import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.http.HttpServletRequest;
import mantenimiento.Cibertec.Proyecto.DTO.LoginRequest;
import mantenimiento.Cibertec.Proyecto.DTO.LoginResponse;
import mantenimiento.Cibertec.Proyecto.Service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @GetMapping("/firebase")
    public ResponseEntity<String> ejemplo(HttpServletRequest request) {
        System.out.println("📥 Entró al controlador /auth/firebase");
        FirebaseToken user = (FirebaseToken) request.getAttribute("firebaseUser");
        return ResponseEntity.ok("Hola, " + user.getEmail() + " (UID: " + user.getUid() + ")");
    }

}
