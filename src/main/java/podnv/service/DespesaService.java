package podnv.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import podnv.dto.DespesaDTO;
import podnv.model.Categoria;
import podnv.model.Despesa;
import podnv.model.Usuario;
import podnv.repository.DespesaRepository;
import podnv.repository.UsuarioRepository;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class DespesaService {
    private final DespesaRepository despesaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Despesa salvarDespesa(DespesaDTO dto){
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Despesa despesa = Despesa.builder()
                .valor(dto.getValor())
                .descricao(dto.getDescricao())
                .categoria(dto.getCategoria())
                .data(dto.getData())
                .recorrente(dto.isRecorrente())
                .parcelasTotais(dto.getParcelasTotais())
                .parcelasRestantes(dto.getParcelasRestantes() != null ? dto.getParcelasRestantes() : dto.getParcelasTotais())
                .usuario(usuario)
                .build();
        return despesaRepository.save(despesa);
    }

    @Transactional
    public Despesa editarDespesa(Long usuarioId, Long id, DespesaDTO dto){
        Despesa despesa = despesaRepository.findByIdAndUsuario_Id(id, usuarioId)
                        .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));

        if(dto.getValor() != null){
            despesa.setValor(dto.getValor());
        }
        if(dto.getDescricao() != null){
            despesa.setDescricao(dto.getDescricao());
        }
        if(dto.getCategoria() != null){
            despesa.setCategoria(dto.getCategoria());
        }
        if(dto.getData() != null){
            despesa.setData(dto.getData());
        }
        if(dto.getParcelasTotais() != null){
            despesa.setParcelasTotais(dto.getParcelasTotais());
        }

        despesa.setParcelasRestantes(dto.getParcelasRestantes() != null ? dto.getParcelasRestantes() : dto.getParcelasTotais());
        return despesaRepository.save(despesa);
    }

    @Transactional
    public void deletarDespesa(Long usuarioId, Long id){
        Despesa despesa = despesaRepository.findByIdAndUsuario_Id(id, usuarioId)
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));
        despesaRepository.delete(despesa);
    }

    public List<Despesa> listarTodos(Long usuarioId){
        return despesaRepository.findByUsuarioId(usuarioId);
    }

    public BigDecimal calcularGastoPorCategoriaNoMes(Long usuarioId, Long categoriaId, YearMonth mes){
        List<Despesa> despesas = despesaRepository.findByUsuario_IdAndCategoria_IdAndDataBetween(
                usuarioId,
                categoriaId,
                mes.atDay(1),
                mes.atEndOfMonth()
        );
        return despesas.stream()
                .map(Despesa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalGastoNoMes(Long usuarioId, YearMonth mes){
        List<Despesa> despesas = despesaRepository.findByUsuarioIdAndDataBetween(
                usuarioId,
                mes.atDay(1),
                mes.atEndOfMonth()
        );
        return despesas.stream()
                .map(Despesa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Despesa> listarDepesasNoMes(Long usuarioId, YearMonth mes){
        return despesaRepository.findByUsuarioIdAndDataBetween(
                usuarioId,
                mes.atDay(1),
                mes.atEndOfMonth()
        );
    }

    public List<Despesa> listarDepesasPorCategoria(Long usuarioId, Long categoriaId, YearMonth mes){
        return despesaRepository.findByUsuario_IdAndCategoria_IdAndDataBetween(
                usuarioId,
                categoriaId,
                mes.atDay(1),
                mes.atEndOfMonth()
        );
    }

    @Transactional
    public void processarParcelasRecorrentes(Long usuarioId, YearMonth mesAtual){
        List<Despesa> despesas = despesaRepository.findByUsuarioIdAndRecorrenteTrueAndParcelasRestantesGreaterThan(
                usuarioId, 0);
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
