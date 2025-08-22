package guzman.SalesDashboard.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
