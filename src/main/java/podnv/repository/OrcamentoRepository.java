package podnv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import podnv.model.Orcamento;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
    Optional<Orcamento> findByIdAndUsuarioId(Long id, Long usuarioId);

    List<Orcamento> findByUsuarioId(Long usuarioId);

    Optional<Orcamento> findByUsuarioIdAndCategoriaId(Long usuarioId, Long categoriaId);

    @Query("SELECT COALESCE(SUM(d.valor), 0) " +
            "FROM Despesa d " +
            "WHERE d.usuario.id = :usuarioId " +
            "AND d.categoria.id = :categoriaId")
    BigDecimal sumDespesasByUsuarioIdAndCategoriaId(@Param("usuarioId") Long usuarioId,
                                                    @Param("categoriaId") Long categoriaId);
}
