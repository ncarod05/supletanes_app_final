package cl.supletanes.supletanes_app.repository;

import cl.supletanes.supletanes_app.entity.Recordatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordatorioRepository extends JpaRepository<Recordatorio, Long> {

    // Método para buscar todos los recordatorios de un usuario específico
    List<Recordatorio> findByIdUsuario(String idUsuario);
}
