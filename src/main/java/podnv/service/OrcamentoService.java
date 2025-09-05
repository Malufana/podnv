package podnv.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import podnv.dto.OrcamentoDTO;
import podnv.model.Categoria;
import podnv.model.Orcamento;
import podnv.model.Usuario;
import podnv.repository.CategoriaRepository;
import podnv.repository.OrcamentoRepository;
import podnv.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrcamentoService {
    private final OrcamentoRepository orcamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    @Transactional
    public Orcamento salvarOrcamento(Long usuarioId, OrcamentoDTO dto){
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));


        if(orcamentoRepository.findByUsuarioIdAndCategoriaId(usuarioId, dto.getCategoriaId()).isPresent()){
            throw new RuntimeException("Orçamento para esta categoria já existe");
        }

        Orcamento orcamento = Orcamento.builder()
                .valor(dto.getValor())
                .categoria(categoria)
                .usuario(usuario)
                .consumido(dto.getConsumido())
                .build();
        return orcamentoRepository.save(orcamento);
    }

    @Transactional
    public Orcamento editarOrcamento(Long usuarioId, Long id, OrcamentoDTO dto){
        Orcamento orcamento = orcamentoRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RuntimeException("Orçamento não encontrado"));

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if(dto.getCategoriaId() != null){
            orcamento.setCategoria(categoria);
        }
        if(dto.getValor() != null){
            orcamento.setValor(dto.getValor());
        }

        return orcamentoRepository.save(orcamento);
    }

    @Transactional
    public void deletarOrcamento(Long usuarioId, Long id){
        Orcamento orcamento = orcamentoRepository.findByIdAndUsuarioId(id, usuarioId)
                        .orElseThrow(() -> new RuntimeException("Orçamento não encontrado"));
        orcamentoRepository.delete(orcamento);
    }

    public List<Orcamento> listarTodos(Long usuarioId){
        return orcamentoRepository.findByUsuarioId(usuarioId);
    }

    public Orcamento calcularOrcamentoPorCategoria(Long usuarioId, Long categoriaId){
        Orcamento orcamento = orcamentoRepository.findByUsuarioIdAndCategoriaId(usuarioId, categoriaId)
                .orElseThrow(() -> new RuntimeException("Orçamento não encontrado"));

        BigDecimal totalDespesas = orcamentoRepository.sumDespesasByUsuarioIdAndCategoriaId(usuarioId, categoriaId);

        orcamento.setConsumido(totalDespesas);

        if(orcamento.getValor().compareTo(BigDecimal.ZERO) > 0){
            double porcentagem = totalDespesas
                    .divide(orcamento.getValor(), 2, RoundingMode.HALF_UP)
                    .doubleValue() * 100;
            orcamento.setPorcentagemConsumida(porcentagem);
        }else {
            orcamento.setPorcentagemConsumida(0.0);
        }

        return orcamento;
    }

    public BigDecimal saldoRestante(Long usuarioId, Long categoriaId){
        Orcamento orcamento = calcularOrcamentoPorCategoria(usuarioId, categoriaId);
        return orcamento.getValor().subtract(orcamento.getConsumido());
    }
}
