package br.edu.unidavi.unidavijava.model;

import java.io.Serializable;

/**
 * Created by jessicapeixe
 */

public class Jogo {

    private Integer id;
    private String nome;
    private String plataforma;
    private Integer lancamento;
    private String genero;
    private String imageUrl;
    private Double nota;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public Integer getLancamento() {
        return lancamento;
    }

    public void setLancamento(Integer lancamento) {
        this.lancamento = lancamento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Double getNota() { return nota; }

    public void setNota(Double nota) { this.nota = nota; }
}
