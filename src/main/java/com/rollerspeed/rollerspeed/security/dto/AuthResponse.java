package com.rollerspeed.rollerspeed.security.dto;

/**
 * Respuesta que devolvemos tras autenticarnos correctamente.
 * Incluye el token, el rol del usuario y metadatos para el cliente.
 */
public class AuthResponse {

    private String token;
    private String tipoToken = "Bearer";
    private long expiresIn;
    private String issuedAt;
    private String rol;

    public AuthResponse(String token, long expiresIn, String issuedAt, String rol) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.issuedAt = issuedAt;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public String getIssuedAt() {
        return issuedAt;
    }

    public String getRol() {
        return rol;
    }
}
