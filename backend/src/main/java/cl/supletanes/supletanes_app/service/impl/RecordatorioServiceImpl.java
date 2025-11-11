package cl.supletanes.supletanes_app.service.impl;

import cl.supletanes.supletanes_app.entity.Recordatorio;
import cl.supletanes.supletanes_app.repository.RecordatorioRepository;
import cl.supletanes.supletanes_app.service.RecordatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecordatorioServiceImpl implements RecordatorioService {

    @Autowired
    private RecordatorioRepository recordatorioRepository;

    @Override
    public Recordatorio crearRecordatorio(Recordatorio recordatorio) {
        return recordatorioRepository.save(recordatorio);
    }

    @Override
    public List<Recordatorio> obtenerRecordatoriosPorUsuario(String idUsuario) {
        return recordatorioRepository.findByIdUsuario(idUsuario);
    }

    @Override
    public Optional<Recordatorio> obtenerRecordatorioPorId(Long id) {
        return recordatorioRepository.findById(id);
    }

    @Override
    public void eliminarRecordatorio(Long id) {
        recordatorioRepository.deleteById(id);
    }
}
