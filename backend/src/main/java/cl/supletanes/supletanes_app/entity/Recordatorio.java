package cl.supletanes.supletanes_app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Recordatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idUsuario;
    private String mensaje;
    private LocalDateTime fechaHora;

    // Constructor vacío
    public Recordatorio() {
    }

    // Constructor completo
    public Recordatorio(Long id, String idUsuario, String mensaje, LocalDateTime fechaHora) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.mensaje = mensaje;
        this.fechaHora = fechaHora;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recordatorio that = (Recordatorio) o;
        return Objects.equals(id, that.id) && Objects.equals(idUsuario, that.idUsuario) && Objects.equals(mensaje, that.mensaje) && Objects.equals(fechaHora, that.fechaHora);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idUsuario, mensaje, fechaHora);
    }

    @Override
    public String toString() {
        return "Recordatorio{" +
                "id=" + id +
                ", idUsuario='" + idUsuario + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", fechaHora=" + fechaHora +
                '}';
    }
}