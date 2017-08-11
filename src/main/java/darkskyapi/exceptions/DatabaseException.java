package darkskyapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by daniel on 09.08.17.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Database error")
public class DatabaseException extends RuntimeException {

    @Override
    public String toString() {
        return "Database connection failed...";
    }
}
