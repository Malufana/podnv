package podnv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import podnv.dto.OrcamentoDTO;
import podnv.model.Orcamento;
import podnv.service.OrcamentoService;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/orcamento")
@RequiredArgsConstructor
public class OrcamentoController {
    private final OrcamentoService orcamentoService;

    @PostMapping
    public ResponseEntity<Orcamento> salvarOrcamento(@RequestBody OrcamentoDTO dto){
        Orcamento orcamento = orcamentoService.salvarOrcamento(dto);
        return ResponseEntity.ok(orcamento);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Orcamento> editarOrcamento(@PathVariable Long id, @RequestBody OrcamentoDTO dto){
        Orcamento orcamento = orcamentoService.editarOrcamento(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orcamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarOrcamento(@PathVariable Long id){
        orcamentoService.deletarOrcamento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Orcamento>> listarTodos(){
        List<Orcamento> orcamentos = orcamentoService.listarTodos();
        return ResponseEntity.ok(orcamentos);
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<Orcamento> calcularOrcamentoPorCategoria(@PathVariable Long categoriaId){
        Orcamento orcamento = orcamentoService.calcularOrcamentoPorCategoria(categoriaId);
        return ResponseEntity.ok(orcamento);
    }

    @GetMapping("/categoria/{categoriaId}/saldo")
    public ResponseEntity<BigDecimal> saldoRestante(@PathVariable Long categoriaId){
        BigDecimal saldo = orcamentoService.saldoRestante(categoriaId);
        return ResponseEntity.ok(saldo);
    }
}
