package podnv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import podnv.model.Receita;
import java.util.List;
import java.util.Optional;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    boolean existsByNomeAndUsuario_Id(String nome, Long usuarioId);

    List<Receita> findByUsuarioId(Long usuarioId);

    Optional<Receita> findByIdAndUsuarioId(Long id, Long usuarioId);
}
