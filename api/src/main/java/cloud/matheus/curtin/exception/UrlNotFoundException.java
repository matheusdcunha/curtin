package cloud.matheus.curtin.exception;

public class UrlNotFoundException extends RuntimeException{

    public UrlNotFoundException(String message){
        super(message);
    }

}
