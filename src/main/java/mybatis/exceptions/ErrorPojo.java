package mybatis.exceptions;

/**
 * Created by daniel on 09.08.17.
 */
public class ErrorPojo {

    String message;
    int errorCode;

    public ErrorPojo(String message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
