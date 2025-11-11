package cl.supletanes.supletanes_app.controller;

import cl.supletanes.supletanes_app.entity.Recordatorio;
import cl.supletanes.supletanes_app.service.RecordatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recordatorios")
public class RecordatorioController {

    @Autowired
    private RecordatorioService recordatorioService;

    // Endpoint para CREAR un nuevo recordatorio
    @PostMapping
    public ResponseEntity<Recordatorio> crearRecordatorio(@RequestBody Recordatorio recordatorio) {
        Recordatorio nuevoRecordatorio = recordatorioService.crearRecordatorio(recordatorio);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRecordatorio);
    }

    // Endpoint para OBTENER todos los recordatorios de un usuario
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Recordatorio>> obtenerRecordatoriosPorUsuario(@PathVariable String idUsuario) {
        List<Recordatorio> recordatorios = recordatorioService.obtenerRecordatoriosPorUsuario(idUsuario);
        return ResponseEntity.ok(recordatorios);
    }

    // Endpoint para ELIMINAR un recordatorio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRecordatorio(@PathVariable Long id) {
        if (recordatorioService.obtenerRecordatorioPorId(id).isPresent()) {
            recordatorioService.eliminarRecordatorio(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
