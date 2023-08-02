package exceptions;

import jakarta.servlet.http.HttpServletResponse;

public class ExceptionHandler {
    public static void handleException(int errorCode, String message, HttpServletResponse resp) {
        resp.setStatus(errorCode);
    }
}
