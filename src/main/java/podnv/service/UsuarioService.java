package podnv.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import podnv.config.TokenService;
import podnv.dto.UsuarioDTO;
import podnv.model.Usuario;
import podnv.repository.UsuarioRepository;
import java.util.List;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public Usuario salvarUsuario(UsuarioDTO dto){
        if(usuarioRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("Email já cadastrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public List<Usuario> listarTodos(){
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario editarUsuario(Long id, UsuarioDTO dto){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setNome(dto.getNome());
        if(dto.getSenha() != null && !dto.getSenha().isEmpty()){
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }
        return usuarioRepository.save(usuario);
    }

    public String autenticarLogin(String email, String senha){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email não encontrado"));


        if(!passwordEncoder.matches(senha, usuario.getSenha())){
            throw new RuntimeException("Senha incorreta");
        }

        return tokenService.gerarToken(usuario);
    }

    public boolean emailJaCadastrado(String email){
        return usuarioRepository.existsByEmail(email);
    }
}
