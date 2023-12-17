package com.example.appnavigationdrawer.models;

import java.time.LocalDateTime;

public class Percurso {
    private double latitudeFimCorrida, longitudeFimCorrida;
    private double latitudeInicioCorrida, longitudeInicioCorrida;
    private LocalDateTime dataInicioCorrida;
    private double contadorDistancia;
    private double contadorTempo;
    private String titulo;

    public Percurso(double latitudeFimCorrida, double longitudeFimCorrida, double latitudeInicioCorrida, double longitudeInicioCorrida, LocalDateTime dataInicioCorrida, double contadorDistancia, double contadorTempo, String titulo) {
        this.latitudeFimCorrida = latitudeFimCorrida;
        this.longitudeFimCorrida = longitudeFimCorrida;
        this.latitudeInicioCorrida = latitudeInicioCorrida;
        this.longitudeInicioCorrida = longitudeInicioCorrida;
        this.dataInicioCorrida = dataInicioCorrida;
        this.contadorDistancia = contadorDistancia;
        this.contadorTempo = contadorTempo;
        this.titulo = titulo;
    }

    public Percurso(LocalDateTime dataInicioCorrida, double contadorDistancia, double contadorTempo, String titulo) {
        this.dataInicioCorrida = dataInicioCorrida;
        this.contadorDistancia = contadorDistancia;
        this.contadorTempo = contadorTempo;
        this.titulo = titulo;
    }

    public double getLatitudeFimCorrida() {
        return latitudeFimCorrida;
    }

    public void setLatitudeFimCorrida(double latitudeFimCorrida) {
        this.latitudeFimCorrida = latitudeFimCorrida;
    }

    public double getLongitudeFimCorrida() {
        return longitudeFimCorrida;
    }

    public void setLongitudeFimCorrida(double longitudeFimCorrida) {
        this.longitudeFimCorrida = longitudeFimCorrida;
    }

    public double getLatitudeInicioCorrida() {
        return latitudeInicioCorrida;
    }

    public void setLatitudeInicioCorrida(double latitudeInicioCorrida) {
        this.latitudeInicioCorrida = latitudeInicioCorrida;
    }

    public double getLongitudeInicioCorrida() {
        return longitudeInicioCorrida;
    }

    public void setLongitudeInicioCorrida(double longitudeInicioCorrida) {
        this.longitudeInicioCorrida = longitudeInicioCorrida;
    }

    public LocalDateTime getDataInicioCorrida() {
        return dataInicioCorrida;
    }

    public void setDataInicioCorrida(LocalDateTime dataInicioCorrida) {
        this.dataInicioCorrida = dataInicioCorrida;
    }

    public double getContadorDistancia() {
        return contadorDistancia;
    }

    public void setContadorDistancia(double contadorDistancia) {
        this.contadorDistancia = contadorDistancia;
    }

    public double getContadorTempo() {
        return contadorTempo;
    }

    public void setContadorTempo(double contadorTempo) {
        this.contadorTempo = contadorTempo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
