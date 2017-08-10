package mybatis.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by daniel on 09.08.17.
 */
@ResponseStatus(value= HttpStatus.NO_CONTENT, reason="Could not connect do DarkSky API")
public class DarkSkyApiException extends RuntimeException {
    @Override
    public String toString() {
        return "Could not connect do DarkSky API";
    }
}
