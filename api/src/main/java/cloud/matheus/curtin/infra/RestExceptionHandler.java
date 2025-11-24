package cloud.matheus.curtin.infra;

import cloud.matheus.curtin.exception.UrlNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UrlNotFoundException.class)
    private ResponseEntity<RestExceptionResponse> urlNotFoundException(UrlNotFoundException exception, WebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        Integer statusCode = HttpStatus.NOT_FOUND.value();
        String path = request.getDescription(false).replace("uri=", "");
        String error = status.getReasonPhrase();
        String message = exception.getMessage();


        RestExceptionResponse response = new RestExceptionResponse(statusCode, error, message, path);


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request){

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        Integer statusCode = httpStatus.value();
        String path = request.getDescription(false).replace("uri=", "");
        String error = httpStatus.getReasonPhrase();
        String message = "Route not exists";


        RestExceptionResponse response = new RestExceptionResponse(statusCode, error, message, path);

        return ResponseEntity.status(httpStatus).body(response);
    }


}
