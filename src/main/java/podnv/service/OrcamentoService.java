package podnv.service;

import org.springframework.security.core.context.SecurityContextHolder;
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
public class OrcamentoService {
    private final OrcamentoRepository orcamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    public OrcamentoService(OrcamentoRepository orcamentoRepository, UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository) {
        this.orcamentoRepository = orcamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public Orcamento salvarOrcamento(OrcamentoDTO dto){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Categoria categoria = categoriaRepository.findByIdAndUsuario_Id(dto.getCategoriaId(), usuario.getId());

        if(orcamentoRepository.findByUsuarioIdAndCategoriaId(usuario.getId(), dto.getCategoriaId()).isPresent()){
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
    public Orcamento editarOrcamento(Long id, OrcamentoDTO dto){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Orcamento orcamento = orcamentoRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new RuntimeException("Orçamento não encontrado"));

        Categoria categoria = categoriaRepository.findByIdAndUsuario_Id(dto.getCategoriaId(), usuario.getId());

        if(dto.getCategoriaId() != null){
            orcamento.setCategoria(categoria);
        }
        if(dto.getValor() != null){
            orcamento.setValor(dto.getValor());
        }

        return orcamentoRepository.save(orcamento);
    }

    @Transactional
    public void deletarOrcamento(Long id){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Orcamento orcamento = orcamentoRepository.findByIdAndUsuarioId(id, usuario.getId())
                        .orElseThrow(() -> new RuntimeException("Orçamento não encontrado"));
        orcamentoRepository.delete(orcamento);
    }

    public List<Orcamento> listarTodos(){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return orcamentoRepository.findByUsuarioId(usuario.getId());
    }

    public Orcamento calcularOrcamentoPorCategoria(Long categoriaId){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Orcamento orcamento = orcamentoRepository.findByUsuarioIdAndCategoriaId(usuario.getId(), categoriaId)
                .orElseThrow(() -> new RuntimeException("Orçamento não encontrado"));

        BigDecimal totalDespesas = orcamentoRepository.sumDespesasByUsuarioIdAndCategoriaId(usuario.getId(), categoriaId);

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

    public BigDecimal saldoRestante(Long categoriaId){
        Orcamento orcamento = calcularOrcamentoPorCategoria(categoriaId);
        return orcamento.getValor().subtract(orcamento.getConsumido());
    }
}
