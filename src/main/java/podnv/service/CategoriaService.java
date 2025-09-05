package podnv.service;

import lombok.RequiredArgsConstructor;
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
    public Categoria salvarCategoria(Long usuarioId, CategoriaDTO dto){
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if(categoriaRepository.existsByNomeAndUsuario_Id(dto.getNome(), usuarioId)){
            throw new IllegalArgumentException("Já existe uma categoria com esse nome");
        }

        Categoria categoria = Categoria.builder()
                .nome(dto.getNome())
                .cor(dto.getCor())
                .usuario(usuario)
                .build();
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listarTodos(Long usuarioId){
        return categoriaRepository.findByUsuario_Id(usuarioId);
    }

    @Transactional
    public Categoria editarCategoria(Long usuarioId, Long id, CategoriaDTO dto){
        Categoria categoria = categoriaRepository.findByIdAndUsuario_Id(id, usuarioId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if(!categoria.getNome().equals(dto.getNome()) &&
                categoriaRepository.existsByNomeAndUsuario_Id(dto.getNome(), usuarioId)){
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
    public void deletarCategoria(Long usuarioId, Long id){
        Categoria categoria = categoriaRepository.findByIdAndUsuario_Id(id, usuarioId)
                        .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        categoriaRepository.delete(categoria);
    }
}
