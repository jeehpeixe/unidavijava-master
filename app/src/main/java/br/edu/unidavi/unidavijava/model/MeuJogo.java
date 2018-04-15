package br.edu.unidavi.unidavijava.model;

/**
 * Created by jessicapeixe
 */

public class MeuJogo {

    private Jogo jogo;
    private boolean quero;
    private boolean tenho;
    private boolean joguei;
    private boolean zerei;
    private boolean fisico;
    private Float paguei;
    private Integer nota;
    private String urlImagem;

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    public boolean isQuero() {
        return quero;
    }

    public void setQuero(boolean quero) {
        this.quero = quero;
    }

    public boolean isTenho() {
        return tenho;
    }

    public void setTenho(boolean tenho) {
        this.tenho = tenho;
    }

    public boolean isJoguei() {
        return joguei;
    }

    public void setJoguei(boolean joguei) {
        this.joguei = joguei;
    }

    public boolean isZerei() {
        return zerei;
    }

    public void setZerei(boolean zerei) {
        this.zerei = zerei;
    }

    public boolean isFisico() {
        return fisico;
    }

    public void setFisico(boolean fisico) {
        this.fisico = fisico;
    }

    public Float getPaguei() {
        return paguei;
    }

    public void setPaguei(Float paguei) {
        this.paguei = paguei;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}
