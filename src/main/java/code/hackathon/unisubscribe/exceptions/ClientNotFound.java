package code.hackathon.unisubscribe.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ClientNotFound extends RuntimeException{
    public ClientNotFound(String errorMessage) {
        super(errorMessage);
    }
}