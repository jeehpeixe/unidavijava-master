package br.edu.unidavi.unidavijava.data;

public enum Ordenacao {
    NOME(1), DATA(2), GENERO(3);

    private final int valor;

    Ordenacao(int valorOpcao){
        valor = valorOpcao;
    }

    public int getValor(){
        return valor;
    }
}
