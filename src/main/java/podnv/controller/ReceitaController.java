package podnv.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import podnv.dto.ReceitaDTO;
import podnv.model.Receita;
import podnv.service.ReceitaService;
import java.util.List;

@RestController
@RequestMapping("/receita")
public class ReceitaController {
    private final ReceitaService receitaService;

    public ReceitaController(ReceitaService receitaService) {
        this.receitaService = receitaService;
    }

    @PostMapping
    public ResponseEntity<Receita> salvarReceita(@RequestBody ReceitaDTO dto){
        Receita receita = receitaService.salvarReceita(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(receita);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Receita> editarReceita(@PathVariable Long id, @RequestBody ReceitaDTO dto){
        Receita receita = receitaService.editarReceita(id, dto);
        return ResponseEntity.ok(receita);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarReceita(@PathVariable Long id){
        receitaService.deletarReceita(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Receita>> listarTodos(){
        return ResponseEntity.ok(receitaService.listarTodos());
    }
}
