package cloud.matheus.curtin.exception;

import jakarta.persistence.EntityNotFoundException;

public class UrlNotFoundException extends EntityNotFoundException {

    public UrlNotFoundException(String message){
        super(message);
    }

}
