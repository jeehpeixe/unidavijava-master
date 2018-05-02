package br.edu.unidavi.unidavijava.model;

import java.util.ArrayList;
import java.util.List;

public class ListaMeuJogo {

    private List<MeuJogo> jogos = new ArrayList<MeuJogo>();

    public List<MeuJogo> getJogos() {
        return jogos;
    }

    public void setJogos(List<MeuJogo> jogos) {
        this.jogos = jogos;
    }
}
