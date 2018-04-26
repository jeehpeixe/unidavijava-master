package br.edu.unidavi.unidavijava.data;

public enum Ordenacao {
    NOME(1), NOME_DESC(2), DATA(3), DATA_DESC(4), GENERO(5), GENERO_DESC(6), NOTA(7), NOTA_DESC(8);

    private final int valor;

    Ordenacao(int valorOpcao){
        valor = valorOpcao;
    }
    public int getValor(){
        return valor;
    }
}
