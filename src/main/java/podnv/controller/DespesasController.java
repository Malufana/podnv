package podnv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import podnv.dto.DespesaDTO;
import podnv.model.Despesa;
import podnv.service.DespesaService;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/despesa")
public class DespesasController {
    private final DespesaService despesaService;

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Despesa> salvarDespesa(@PathVariable Long usuarioId, @RequestBody DespesaDTO dto){
        Despesa despesa = despesaService.salvarDespesa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(despesa);
    }

    @PatchMapping("/usuario/{usuarioId}/{id}")
    public ResponseEntity<Despesa> editarDespesa(@PathVariable Long usuarioId, @PathVariable Long id, @RequestBody DespesaDTO dto){
        Despesa despesa = despesaService.editarDespesa(usuarioId, id, dto);
        return ResponseEntity.ok(despesa);
    }

    @DeleteMapping("/usuario/{usuarioId}/{id}")
    public ResponseEntity<Void> deletarDespesa(@PathVariable Long usuarioId, @PathVariable Long id){
        despesaService.deletarDespesa(usuarioId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Despesa>> listarTodos(@PathVariable Long usuarioId){
        return ResponseEntity.ok(despesaService.listarTodos(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/categoria/{categoriaId}/mes/{anoMes}")
    public ResponseEntity<BigDecimal> calcularGastoPorCategoriaNoMes(@PathVariable Long usuarioId, @PathVariable Long categoriaId, @PathVariable YearMonth anoMes){
        BigDecimal gasto = despesaService.calcularGastoPorCategoriaNoMes(usuarioId, categoriaId, anoMes);
        return ResponseEntity.ok(gasto);
    }

    @GetMapping("/usuario/{usuarioId}/mes/{anoMes}")
    public ResponseEntity<BigDecimal> calcularTotalGastoNoMes(@PathVariable Long usuarioId, @PathVariable YearMonth anoMes){
        BigDecimal total = despesaService.calcularTotalGastoNoMes(usuarioId, anoMes);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/usuario/{usuarioId}/mes/{anoMes}/listar")
    public ResponseEntity<List<Despesa>> listarDespesasNoMes(@PathVariable Long usuarioId, @PathVariable YearMonth anoMes){
        return ResponseEntity.ok(despesaService.listarDepesasNoMes(usuarioId, anoMes));
    }

    @GetMapping("/usuario/{usuarioId}/categoria/{categoriaId}/mes/{anoMes}/listar")
    public ResponseEntity<List<Despesa>> listarDespesasPorCategoria(@PathVariable Long usuarioId, @PathVariable Long categoriaId, @PathVariable YearMonth anoMes){
        return ResponseEntity.ok(despesaService.listarDepesasPorCategoria(usuarioId, categoriaId, anoMes));
    }

    @GetMapping("/usuario/{usuarioId}/processar-parcelas/{anoMes}")
    public ResponseEntity<Void> processarParcelasRecorrentes(@PathVariable Long usuarioId, @PathVariable YearMonth anoMes){
        despesaService.processarParcelasRecorrentes(usuarioId, anoMes);
        return ResponseEntity.ok().build();
    }
}
