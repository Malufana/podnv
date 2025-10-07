package podnv.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DespesaDTO {
    private Long id;
    private Long usuarioId;
    private String nome;
    private BigDecimal valor;
    private Long categoriaId;
    private String descricao;
    private boolean recorrente;
    private LocalDate data;
    private Integer parcelasTotais;
    private Integer parcelasRestante;
}
