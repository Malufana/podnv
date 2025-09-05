package podnv.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrcamentoDTO {
    private Long id;
    private Long usuarioId;
    private BigDecimal valor;
    private BigDecimal consumido;
    private Long categoriaId;
    private Double porcetagemConsumida;
}
