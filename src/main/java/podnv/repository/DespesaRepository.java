package podnv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import podnv.model.Despesa;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    List<Despesa> findByUsuario_IdAndCategoria_IdAndDataBetween(Long usuarioId, Long categoriaId, LocalDate localDate, LocalDate localDate1);
    List<Despesa> findByUsuarioIdAndDataBetween(Long usuarioId, LocalDate localDate, LocalDate localDate1);
    List<Despesa> findByUsuarioIdAndRecorrenteTrueAndParcelasRestantesGreaterThan(Long usuarioId, Integer parcelasRestantes);
    Optional<Despesa> findByIdAndUsuario_Id(Long id, Long usuarioId);
    List<Despesa> findByUsuarioId(Long usuarioId);
    Optional<Despesa> findByUsuarioIdAndCategoriaId(Long usuarioId, Long categoriaId);
}
