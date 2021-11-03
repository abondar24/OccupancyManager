package org.abondar.industrial.occupancy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OccupancyExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({OccupancyException.class})
    public void handleBadRequest(Exception ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_GATEWAY.value(), ex.getMessage());
    }

}
