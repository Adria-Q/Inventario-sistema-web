package com.example_inventario.Dto;

public class ErrorResponse {
    private String mensaje;
    private int codigo;
    private long timestamp;

    // Constructores
    public ErrorResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    public ErrorResponse(String mensaje, int codigo) {
        this.mensaje = mensaje;
        this.codigo = codigo;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters y Setters
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}