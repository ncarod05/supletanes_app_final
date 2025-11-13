package cl.supletanes.supletanes_app.recordatorio.service;

import java.util.List;
import java.util.Optional;

import cl.supletanes.supletanes_app.recordatorio.entity.Recordatorio;

public interface RecordatorioService {
    /**
     * Guarda un nuevo recordatorio.
     * @param recordatorio El recordatorio a guardar.
     * @return El recordatorio guardado.
     */
    Recordatorio crearRecordatorio(Recordatorio recordatorio);

    /**
     * Obtiene todos los recordatorios para un ID de usuario específico.
     * @param idUsuario El ID del usuario.
     * @return Una lista de recordatorios.
     */
    List<Recordatorio> obtenerRecordatoriosPorUsuario(String idUsuario);

    /**
     * Busca un recordatorio por su ID.
     * @param id El ID del recordatorio.
     * @return Un Optional que contiene el recordatorio si se encuentra.
     */
    Optional<Recordatorio> obtenerRecordatorioPorId(Long id);

    /**
     * Elimina un recordatorio por su ID.
     * @param id El ID del recordatorio a eliminar.
     */
    void eliminarRecordatorio(Long id);
}
