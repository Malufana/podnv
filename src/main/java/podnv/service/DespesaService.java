package podnv.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import podnv.dto.DespesaDTO;
import podnv.model.Categoria;
import podnv.model.Despesa;
import podnv.model.Usuario;
import podnv.repository.CategoriaRepository;
import podnv.repository.DespesaRepository;
import podnv.repository.UsuarioRepository;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import jakarta.transaction.Transactional;

@Service
public class DespesaService {
    private final DespesaRepository despesaRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public DespesaService(DespesaRepository despesaRepository, CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository) {
        this.despesaRepository = despesaRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Despesa salvarDespesa(DespesaDTO dto){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Categoria categoria = categoriaRepository.findByIdAndUsuario_Id(dto.getCategoriaId(), usuario.getId());

        Despesa despesa = Despesa.builder()
                .nome(dto.getNome())
                .valor(dto.getValor())
                .descricao(dto.getDescricao())
                .categoria(categoria)
                .data(dto.getData())
                .recorrente(dto.isRecorrente())
                .parcelasTotais(dto.getParcelasTotais())
                .parcelasRestantes(dto.getParcelasRestante() != null ? dto.getParcelasRestante() : dto.getParcelasTotais())
                .usuario(usuario)
                .build();
        return despesaRepository.save(despesa);
    }

    @Transactional
    public Despesa editarDespesa(Long id, DespesaDTO dto){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Categoria categoria = categoriaRepository.findByIdAndUsuario_Id(dto.getCategoriaId(), usuario.getId());

        Despesa despesa = despesaRepository.findByIdAndUsuario_Id(id, usuario.getId())
                        .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));

        if(dto.getValor() != null){
            despesa.setValor(dto.getValor());
        }
        if(dto.getDescricao() != null){
            despesa.setDescricao(dto.getDescricao());
        }
        if(categoria.getId() != null){
            despesa.setCategoria(categoria);
        }
        if(dto.getData() != null){
            despesa.setData(dto.getData());
        }
        if(dto.getParcelasTotais() != null){
            despesa.setParcelasTotais(dto.getParcelasTotais());
        }

        despesa.setParcelasRestantes(dto.getParcelasRestante() != null ? dto.getParcelasRestante() : dto.getParcelasTotais());
        return despesaRepository.save(despesa);
    }

    @Transactional
    public void deletarDespesa(Long id){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Despesa despesa = despesaRepository.findByIdAndUsuario_Id(id, usuario.getId())
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));
        despesaRepository.delete(despesa);
    }

    public List<Despesa> listarTodos(){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return despesaRepository.findByUsuarioId(usuario.getId());
    }

    public BigDecimal calcularGastoPorCategoriaNoMes(Long categoriaId, YearMonth mes){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        List<Despesa> despesas = despesaRepository.findByUsuario_IdAndCategoria_IdAndDataBetween(
                usuario.getId(),
                categoriaId,
                mes.atDay(1),
                mes.atEndOfMonth()
        );
        return despesas.stream()
                .map(Despesa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalGastoNoMes(YearMonth mes){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        List<Despesa> despesas = despesaRepository.findByUsuarioIdAndDataBetween(
                usuario.getId(),
                mes.atDay(1),
                mes.atEndOfMonth()
        );
        return despesas.stream()
                .map(Despesa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Despesa> listarDepesasNoMes(YearMonth mes){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return despesaRepository.findByUsuarioIdAndDataBetween(
                usuario.getId(),
                mes.atDay(1),
                mes.atEndOfMonth()
        );
    }

    public List<Despesa> listarDepesasPorCategoria(Long categoriaId, YearMonth mes){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return despesaRepository.findByUsuario_IdAndCategoria_IdAndDataBetween(
                usuario.getId(),
                categoriaId,
                mes.atDay(1),
                mes.atEndOfMonth()
        );
    }

    @Transactional
    public void processarParcelasRecorrentes(YearMonth mesAtual){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        List<Despesa> despesas = despesaRepository.findByUsuarioIdAndRecorrenteTrueAndParcelasRestantesGreaterThan(
                usuario.getId(), 0);

        for(Despesa despesa : despesas){
            if(YearMonth.from(despesa.getData()).isBefore(mesAtual)){
                Despesa novaParcela = Despesa.builder()
                        .valor(despesa.getValor())
                        .descricao(despesa.getDescricao())
                        .categoria(despesa.getCategoria())
                        .data(mesAtual.atDay(1))
                        .recorrente(true)
                        .parcelasTotais(despesa.getParcelasTotais())
                        .parcelasRestantes(despesa.getParcelasRestantes() - 1)
                        .usuario(despesa.getUsuario())
                        .build();

                despesaRepository.save(novaParcela);

                despesa.setParcelasRestantes(despesa.getParcelasRestantes() - 1);
                despesaRepository.save(despesa);
            }
        }
    }
}
