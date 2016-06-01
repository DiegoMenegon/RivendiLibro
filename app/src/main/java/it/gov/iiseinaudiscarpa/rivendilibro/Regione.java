package it.gov.iiseinaudiscarpa.rivendilibro;

/**
 * Created by menegondiego on 28/05/2016.
 */
public class Regione {
    String nome;
    int id;

    public Regione(String nome, int id) {
        this.nome = nome;
        this.id = id;
    }

    @Override
    public String toString() {
        return nome;
    }
}
