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

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Orcamento> salvarOrcamento(@PathVariable Long usuarioId, @RequestBody OrcamentoDTO dto){
        Orcamento orcamento = orcamentoService.salvarOrcamento(usuarioId, dto);
        return ResponseEntity.ok(orcamento);
    }

    @PatchMapping("/usuario/{usuarioId}/{id}")
    public ResponseEntity<Orcamento> editarOrcamento(@PathVariable Long usuarioId, @PathVariable Long id, @RequestBody OrcamentoDTO dto){
        Orcamento orcamento = orcamentoService.editarOrcamento(usuarioId, id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orcamento);
    }

    @DeleteMapping("/usuario/{usuarioId}/{id}")
    public ResponseEntity<Void> deletarOrcamento(@PathVariable Long usuarioId, @PathVariable Long id){
        orcamentoService.deletarOrcamento(usuarioId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Orcamento>> listarTodos(@PathVariable Long usuarioId){
        List<Orcamento> orcamentos = orcamentoService.listarTodos(usuarioId);
        return ResponseEntity.ok(orcamentos);
    }

    @GetMapping("/usuario/{usuarioId}/categoria/{categoriaId}")
    public ResponseEntity<Orcamento> calcularOrcamentoPorCategoria(@PathVariable Long usuarioId, @PathVariable Long categoriaId){
        Orcamento orcamento = orcamentoService.calcularOrcamentoPorCategoria(usuarioId, categoriaId);
        return ResponseEntity.ok(orcamento);
    }

    @GetMapping("/usuario/{usuarioId}/categoria/{categoriaId}/saldo")
    public ResponseEntity<BigDecimal> saldoRestante(@PathVariable Long usuarioId, @PathVariable Long categoriaId){
        BigDecimal saldo = orcamentoService.saldoRestante(usuarioId, categoriaId);
        return ResponseEntity.ok(saldo);
    }
}
