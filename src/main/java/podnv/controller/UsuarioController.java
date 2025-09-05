package podnv.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import podnv.dto.LoginRequest;
import podnv.dto.UsuarioDTO;
import podnv.model.Usuario;
import podnv.service.UsuarioService;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/criar")
    public ResponseEntity<Usuario> salvarUsuario(@RequestBody @Valid UsuarioDTO dto){
        Usuario usuario = usuarioService.salvarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id){
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos(){
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Usuario> editarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO dto){
        Usuario usuario = usuarioService.editarUsuario(id, dto);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<String> autenticar(@RequestBody LoginRequest loginRequest){
        String token = usuarioService.autenticarLogin(loginRequest.getEmail(), loginRequest.getSenha());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/verificar-email")
    public ResponseEntity<Boolean> verificarEmail(@RequestParam String email){
        boolean existe = usuarioService.emailJaCadastrado(email);
        return ResponseEntity.ok(existe);
    }
}
