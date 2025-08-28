package com.ufu.gestaoConsultasMedicas.controller;

import com.ufu.gestaoConsultasMedicas.models.Cid;
import com.ufu.gestaoConsultasMedicas.service.CidService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/cid")
public class CidController {

    private final CidService cidService;

    public CidController(CidService cidService) {
        this.cidService = cidService;
    }

    /** GET /cid → lista todos os códigos armazenados */
    @GetMapping
    public ResponseEntity<List<Cid>> findAll() {
        return ResponseEntity.ok(cidService.findAll());
    }

    /** GET /cid/{code} → busca um código específico */
    @GetMapping("/{code}")
    public ResponseEntity<Cid> findOne(@PathVariable String code) {
        return cidService.findOne(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** POST /cid/refresh → baixa do DATASUS e salva/atualiza no banco */
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshRemote() {
        int count = cidService.refreshFromRemote();
        return ResponseEntity.ok("CIDs atualizados. Total processado: " + count);
    }

    /** POST /cid/refresh-local?path=/caminho/arquivo.html → ingere HTML salvo localmente */
    @PostMapping("/refresh-local")
    public ResponseEntity<String> refreshLocal(@RequestParam("path") String path) {
        int count = cidService.refreshFromLocal(Path.of(path));
        return ResponseEntity.ok("CIDs atualizados a partir do arquivo local. Total processado: " + count);
    }

    /** DELETE /cid/{code} → remove um código */
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteOne(@PathVariable String code) {
        cidService.deleteOne(code);
        return ResponseEntity.noContent().build();
    }

    /** DELETE /cid → limpa a tabela */
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        cidService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
