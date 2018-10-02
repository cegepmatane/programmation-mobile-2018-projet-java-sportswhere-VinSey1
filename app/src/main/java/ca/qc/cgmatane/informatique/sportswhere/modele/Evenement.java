package ca.qc.cgmatane.informatique.sportswhere.modele;

import java.util.Date;

public class Evenement {

    protected Date date;
    protected String nom;
    protected String description;
    protected int terrain;
    protected int id_evenement;

    public Evenement(Date date, String nom, String description, int terrain){
        this.date = date;
        this.nom = nom;
        this.description = description;
        this.terrain = terrain;
    }

    public Evenement(Date date, String nom, String description, int terrain, int id_evenement){
        this.date = date;
        this.nom = nom;
        this.description = description;
        this.terrain = terrain;
        this.id_evenement = id_evenement;
    }

    public Date getDate(){
        return date;
    }

    public String getNom(){
        return nom;
    }

    public String getDescription(){
        return description;
    }

    public int getTerrain(){
        return terrain;
    }

    public int getId_evenement(){
        return id_evenement;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public void setNom(String nom){
        this.nom = nom;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setTerrain(int terrain){
        this.terrain = terrain;
    }

    public void setId_evenement(int id_evenement){
        this.id_evenement = id_evenement;
    }
}
