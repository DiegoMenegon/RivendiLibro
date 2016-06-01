package it.gov.iiseinaudiscarpa.rivendilibro;

/**
 * Created by menegondiego on 28/05/2016.
 */
public class Inserzione {
    String nome;
    double prezzo;
    String prezzosped;
    String residenza;

    public Inserzione(String nome, double prezzo,String prezzosped, String residenza) {
        this.nome = nome;
        this.prezzo = prezzo;
        this.prezzosped = prezzosped;
        this.residenza = residenza;
    }

    public String getNome() {
        return nome;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public String getResidenza() {
        return residenza;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public void setResidenza(String residenza) {
        this.residenza = residenza;
    }

    @Override
    public String toString() {
        if (prezzosped.equals("0")){
            return "Nome utente: "+ nome+"    Prezzo: "+prezzo+"€    \nSpedizione gratuita    Residenza: "+residenza;
        }
        if (prezzosped.equals("No")){
            return "Nome utente: "+ nome+"    Prezzo: "+prezzo+"€    \nSpedizione: "+prezzosped+"   Residenza: "+residenza;
        }else{
            return "Nome utente: "+ nome+"    Prezzo: "+prezzo+"€    \nSpedizione: "+prezzosped+"€    Residenza: "+residenza;
        }
    }
}
