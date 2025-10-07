package podnv.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "despesa")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Despesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @DecimalMin("0.0")
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    private String nome;
    private String descricao;
    private boolean recorrente;
    private LocalDate data;

    @Column(name = "parcelas_totais")
    private Integer parcelasTotais;

    @Column(name = "parcelas_restante")
    private Integer parcelasRestantes;
}
