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

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Categoria> salvarCategoria(@PathVariable Long usuarioId, @RequestBody CategoriaDTO dto){
        Categoria categoria = categoriaService.salvarCategoria(usuarioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Categoria>> listarTodos(@PathVariable Long usuarioId){
        return ResponseEntity.ok(categoriaService.listarTodos(usuarioId));
    }

    @PatchMapping("/usuario/{usuarioId}/{id}")
    public ResponseEntity<Categoria> editarCategoria(@PathVariable Long usuarioId, @PathVariable Long id, @RequestBody CategoriaDTO dto){
        Categoria categoria = categoriaService.editarCategoria(usuarioId, id, dto);
        return ResponseEntity.ok(categoria);
    }

    @DeleteMapping("/usuario/{usuarioId}/{id}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable Long usuarioId, @PathVariable Long id){
        categoriaService.deletarCategoria(usuarioId, id);
        return ResponseEntity.noContent().build();
    }
}
