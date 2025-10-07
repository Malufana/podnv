package podnv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import podnv.model.Categoria;
import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Categoria findByIdAndUsuario_Id(Long id, Long usuarioId);
    List<Categoria> findByUsuario_Id(Long usuarioId);
    boolean existsByNomeAndUsuario_Id(String nome, Long usuarioId);

}
