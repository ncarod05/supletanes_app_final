package cl.supletanes.supletanes_app.recordatorio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.supletanes.supletanes_app.recordatorio.entity.Recordatorio;
import cl.supletanes.supletanes_app.recordatorio.service.RecordatorioService;

@RestController
@RequestMapping("/api/recordatorios")
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
