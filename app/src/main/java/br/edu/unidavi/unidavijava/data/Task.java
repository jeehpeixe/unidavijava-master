package br.edu.unidavi.unidavijava.data;

/**
 * Created by jessicapeixe.
 */

class Task {

    private int id;

    private String name;

    private String description;

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
