package darkskyapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by daniel on 11.08.17.
 */
@ResponseStatus(value= HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, reason="Invalid API key")
public class InvalidKeyException extends Exception {
    @Override
    public String toString() {
        return "Invalid API key";
    }

}
