package podnv.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import podnv.dto.ReceitaDTO;
import podnv.model.Receita;
import podnv.model.Usuario;
import podnv.repository.ReceitaRepository;
import podnv.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceitaService {
    private final ReceitaRepository receitaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Receita salvarReceita(Long usuarioId, ReceitaDTO dto){
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if(receitaRepository.existsByNomeAndUsuario_Id(dto.getNome(), usuarioId)){
            throw new IllegalArgumentException("Já existe uma receita com esse nome");
        }

        Receita receita = Receita.builder()
                .nome(dto.getNome())
                .usuario(usuario)
                .valor(dto.getValor())
                .dataEntrada(dto.getDataEntrada())
                .recorrente(dto.isRecorrente())
                .build();

        return receitaRepository.save(receita);
    }

    @Transactional
    public Receita editarReceita(Long usuarioId, Long id, ReceitaDTO dto){
        Receita receita = receitaRepository.findByIdAndUsuarioId(id, usuarioId)
                        .orElseThrow(() -> new RuntimeException("Receita não encontrada"));

        if(!receita.getNome().equals(dto.getNome()) &&
            receitaRepository.existsByNomeAndUsuario_Id(dto.getNome(), usuarioId)){
            throw new IllegalArgumentException("Já existe uma receita com esse nome");
        }
        if(dto.getNome() != null){
            receita.setNome(dto.getNome());
        }
        if(dto.getValor() != null){
            receita.setValor(dto.getValor());
        }
        if(dto.getDataEntrada() != null){
            receita.setDataEntrada(dto.getDataEntrada());
        }
        if(dto.isRecorrente()){
            receita.setRecorrente(true);
        }

        return receitaRepository.save(receita);
    }

    @Transactional
    public void deletarReceita(Long usuarioId, Long id){
        Receita receita = receitaRepository.findByIdAndUsuarioId(id, usuarioId)
                        .orElseThrow(() -> new RuntimeException("Receita não encontrada"));
        receitaRepository.delete(receita);
    }

    public List<Receita> listarTodos(Long usuarioId){
        return receitaRepository.findByUsuarioId(usuarioId);
    }
}
