package com.example.afmobile.Models;

public class ModeloSerie extends SerieId {

    private String nome;
    private String diaDaSemana;
    private String plataforma;
    private String temporada;
    private String ultimoEpisodioAssistido;

    private int status;

    public String getNome() {
        return nome;
    }

    public String getDiaDaSemana() {
        return diaDaSemana;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public String getTemporada() {
        return temporada;
    }

    public String getUltimoEpisodioAssistido() {
        return ultimoEpisodioAssistido;
    }

    public int getStatus() {
        return status;
    }
}
