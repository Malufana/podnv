package podnv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import podnv.dto.CategoriaDTO;
import podnv.model.Categoria;
import podnv.service.CategoriaService;
import java.util.List;

@RestController
@RequestMapping("/categoria")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<Categoria> salvarCategoria(@RequestBody CategoriaDTO dto){
        Categoria categoria = categoriaService.salvarCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodos(){
        return ResponseEntity.ok(categoriaService.listarTodos());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Categoria> editarCategoria( @PathVariable Long id, @RequestBody CategoriaDTO dto){
        Categoria categoria = categoriaService.editarCategoria(id, dto);
        return ResponseEntity.ok(categoria);
    }

    @DeleteMapping(" /{id}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable Long id){
        categoriaService.deletarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
