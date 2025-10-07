package podnv.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import podnv.dto.ReceitaDTO;
import podnv.model.Receita;
import podnv.model.Usuario;
import podnv.repository.ReceitaRepository;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class ReceitaService {
    private final ReceitaRepository receitaRepository;

    public ReceitaService(ReceitaRepository receitaRepository) {
        this.receitaRepository = receitaRepository;
    }

    @Transactional
    public Receita salvarReceita(ReceitaDTO dto){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if(receitaRepository.existsByNomeAndUsuario_Id(dto.getNome(), usuario.getId())){
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
    public Receita editarReceita(Long id, ReceitaDTO dto){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Receita receita = receitaRepository.findByIdAndUsuarioId(id, usuario.getId())
                        .orElseThrow(() -> new RuntimeException("Receita não encontrada"));

        if(!receita.getNome().equals(dto.getNome()) &&
            receitaRepository.existsByNomeAndUsuario_Id(dto.getNome(), usuario.getId())){
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
    public void deletarReceita(Long id){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Receita receita = receitaRepository.findByIdAndUsuarioId(id, usuario.getId())
                        .orElseThrow(() -> new RuntimeException("Receita não encontrada"));
        receitaRepository.delete(receita);
    }

    public List<Receita> listarTodos(){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return receitaRepository.findByUsuarioId(usuario.getId());
    }
}
