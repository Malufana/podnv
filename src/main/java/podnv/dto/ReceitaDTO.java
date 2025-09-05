package podnv.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceitaDTO {
    private Long id;
    private Long usuarioId;
    private String nome;
    private BigDecimal valor;
    private boolean recorrente;
    private LocalDate dataEntrada;
}
