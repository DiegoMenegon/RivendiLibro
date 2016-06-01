package it.gov.iiseinaudiscarpa.rivendilibro;

/**
 * Created by menegondiego on 14/05/2016.
 */
public class Libro {
    int id;
    String nome;

    public Libro(int id,String nome ) {
        this.nome = nome;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String toString() {
        return "Titolo : "+nome;
    }
}
