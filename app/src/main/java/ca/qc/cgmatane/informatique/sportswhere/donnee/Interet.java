package ca.qc.cgmatane.informatique.sportswhere.donnee;

public class Interet {

    protected boolean coche;
    protected int idEvenement;
    protected  int idInteret;

    public Interet(int idEvenement, boolean coche) {

        this.idEvenement = idEvenement;
        this.coche = coche;
    }

    public boolean isCoche() {
        return coche;
    }

    public void setCoche(boolean coche) {
        this.coche = coche;
    }

    public int getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(int id_evenement) {
        this.idEvenement = id_evenement;
    }

    public int getIdInteret() {
        return idInteret;
    }

    public void setIdInteret(int id_interet) {
        this.idInteret = id_interet;
    }
}