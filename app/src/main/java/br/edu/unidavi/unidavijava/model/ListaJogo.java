package br.edu.unidavi.unidavijava.model;

import java.util.ArrayList;
import java.util.List;

public class ListaJogo {

    private List<Jogo> jogos = new ArrayList<Jogo>();

    public List<Jogo> getJogos() {
        return jogos;
    }

    public void setJogos(List<Jogo> jogos) {
        this.jogos = jogos;
    }
}
