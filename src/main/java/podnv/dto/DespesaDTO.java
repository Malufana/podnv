package podnv.dto;

import lombok.*;
import podnv.model.Categoria;
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
    private Categoria categoria;
    private String descricao;
    private boolean recorrente;
    private LocalDate data;
    private Integer parcelasTotais;
    private Integer parcelasRestantes;
}
