package podnv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import podnv.dto.ReceitaDTO;
import podnv.model.Receita;
import podnv.service.ReceitaService;
import java.util.List;

@RestController
@RequestMapping("/receita")
@RequiredArgsConstructor
public class ReceitaController {
    private final ReceitaService receitaService;

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Receita> salvarReceita(@PathVariable Long usuarioId, @RequestBody ReceitaDTO dto){
        Receita receita = receitaService.salvarReceita(usuarioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(receita);
    }

    @PatchMapping("/usuario/{usuarioId}/{id}")
    public ResponseEntity<Receita> editarReceita(@PathVariable Long usuarioId, @PathVariable Long id, @RequestBody ReceitaDTO dto){
        Receita receita = receitaService.editarReceita(usuarioId, id, dto);
        return ResponseEntity.ok(receita);
    }

    @DeleteMapping("/usuario/{usuarioId}/{id}")
    public ResponseEntity<Void> deletarReceita(@PathVariable Long usuarioId, @PathVariable Long id){
        receitaService.deletarReceita(usuarioId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Receita>> listarTodos(@PathVariable Long usuarioId){
        return ResponseEntity.ok(receitaService.listarTodos(usuarioId));
    }
}
