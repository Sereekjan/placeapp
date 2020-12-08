package kz.ikar.cities.entity;

public class AuthResponse {
    private String status;
    private String code;

    public AuthResponse(String status, String code) {
        this.status = status;
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
