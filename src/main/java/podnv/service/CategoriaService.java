package podnv.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import podnv.dto.CategoriaDTO;
import podnv.model.Categoria;
import podnv.model.Usuario;
import podnv.repository.CategoriaRepository;
import podnv.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Categoria salvarCategoria(CategoriaDTO dto){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if(categoriaRepository.existsByNomeAndUsuario_Id(dto.getNome(), usuario.getId())){
            throw new IllegalArgumentException("Já existe uma categoria com esse nome");
        }

        Categoria categoria = Categoria.builder()
                .nome(dto.getNome())
                .cor(dto.getCor())
                .usuario(usuario)
                .build();
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listarTodos(){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return categoriaRepository.findByUsuario_Id(usuario.getId());
    }

    @Transactional
    public Categoria editarCategoria(Long id, CategoriaDTO dto){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Categoria categoria = categoriaRepository.findByIdAndUsuario_Id(id, usuario.getId());

        if(!categoria.getNome().equals(dto.getNome()) &&
                categoriaRepository.existsByNomeAndUsuario_Id(dto.getNome(), usuario.getId())){
            throw new IllegalArgumentException("Já existe uma categoria com esse nome");
        }

        if(dto.getNome() != null){
            categoria.setNome(dto.getNome());
        }
        if(dto.getCor() != null){
            categoria.setCor(dto.getCor());
        }
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void deletarCategoria(Long id){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Categoria categoria = categoriaRepository.findByIdAndUsuario_Id(id, usuario.getId());
        categoriaRepository.delete(categoria);
    }
}
