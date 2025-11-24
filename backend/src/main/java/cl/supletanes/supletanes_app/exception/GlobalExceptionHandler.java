package cl.supletanes.supletanes_app.exception;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /*
     * Maneja el Error 400: Bad Request (Errores de Validación)
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        // Extrae todos los mensajes de error de la validación
        String detailedMessage = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse = new ErrorResponse(
                httpStatus.value(),
                "Validation Error", // Se usa como el error principal
                "Fallo de validación: " + detailedMessage,
                request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    /**
     * Maneja el Error 500: Internal Server Error (Excepciones Generales/No
     * Controladas)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(
            Exception ex, WebRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        // No exponer detalles sensibles de la excepción interna
        String message = "Ha ocurrido un error inesperado en el servidor.";

        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getDescription(false).replace("uri=", ""));

        // Loggear la excepción completa aquí para la depuración interna

        return new ResponseEntity<>(errorResponse, status);
    }
}