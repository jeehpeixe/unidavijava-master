package br.edu.unidavi.unidavijava.model;

/**
 * Created by jessicapeixe
 */

public class User {

    private String email;
    private String senha;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenha(){
        return senha;
    }
}
