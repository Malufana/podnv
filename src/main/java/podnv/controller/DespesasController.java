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

    @PostMapping
    public ResponseEntity<Despesa> salvarDespesa(@RequestBody DespesaDTO dto){
        Despesa despesa = despesaService.salvarDespesa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(despesa);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Despesa> editarDespesa(@PathVariable Long id, @RequestBody DespesaDTO dto){
        Despesa despesa = despesaService.editarDespesa(id, dto);
        return ResponseEntity.ok(despesa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDespesa(@PathVariable Long id){
        despesaService.deletarDespesa(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Despesa>> listarTodos(){
        return ResponseEntity.ok(despesaService.listarTodos());
    }

    @GetMapping("/categoria/{categoriaId}/mes/{anoMes}")
    public ResponseEntity<BigDecimal> calcularGastoPorCategoriaNoMes(@PathVariable Long categoriaId, @PathVariable YearMonth anoMes){
        BigDecimal gasto = despesaService.calcularGastoPorCategoriaNoMes(categoriaId, anoMes);
        return ResponseEntity.ok(gasto);
    }

    @GetMapping("/mes/{anoMes}")
    public ResponseEntity<BigDecimal> calcularTotalGastoNoMes(@PathVariable YearMonth anoMes){
        BigDecimal total = despesaService.calcularTotalGastoNoMes(anoMes);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/mes/{anoMes}/listar")
    public ResponseEntity<List<Despesa>> listarDespesasNoMes(@PathVariable YearMonth anoMes){
        return ResponseEntity.ok(despesaService.listarDepesasNoMes(anoMes));
    }

    @GetMapping("/categoria/{categoriaId}/mes/{anoMes}/listar")
    public ResponseEntity<List<Despesa>> listarDespesasPorCategoria(@PathVariable Long categoriaId, @PathVariable YearMonth anoMes){
        return ResponseEntity.ok(despesaService.listarDepesasPorCategoria(categoriaId, anoMes));
    }

    @GetMapping("/processar-parcelas/{anoMes}")
    public ResponseEntity<Void> processarParcelasRecorrentes(@PathVariable YearMonth anoMes){
        despesaService.processarParcelasRecorrentes(anoMes);
        return ResponseEntity.ok().build();
    }
}
