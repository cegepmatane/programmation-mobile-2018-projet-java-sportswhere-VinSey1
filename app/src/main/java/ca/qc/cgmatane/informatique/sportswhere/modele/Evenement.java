package ca.qc.cgmatane.informatique.sportswhere.modele;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import ca.qc.cgmatane.informatique.sportswhere.vue.Alarme;

public class Evenement extends AppCompatActivity {

    protected Long date;
    protected String nom;
    protected String description;
    protected int terrain;
    protected int idEvenement;
    protected static final int ACTIVITE_AJOUTER_ALARME = 6;


    public Evenement(Long date, String nom, String description, int terrain){
        this.date = date;
        this.nom = nom;
        this.description = description;
        this.terrain = terrain;
    }

    public Evenement(Long date, String nom, String description, int terrain, int idEvenement){
        this.date = date;
        this.nom = nom;
        this.description = description;
        this.terrain = terrain;
        this.idEvenement = idEvenement;
    }

    public Long getDate(){
        return date;
    }

    public String getDateTexte(){
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
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

    public int getIdEvenement(){
        return idEvenement;
    }

    public void setDate(Long date){
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

    public HashMap<String,String> obtenirEvenementPourAdapteur() {
        HashMap<String, String> evenementPourAdapteur = new HashMap<String, String>();
        evenementPourAdapteur.put("nom", this.nom);
        evenementPourAdapteur.put("date", ""+this.getDateTexte());
        evenementPourAdapteur.put("terrain", ""+this.terrain);
        evenementPourAdapteur.put("idEvenement", ""+this.idEvenement);
        return evenementPourAdapteur;
    }

    Activity activite;
    public void ajouterAlarme(Context context, long dateAlarme){
        Intent intententionLancerAlarme = new Intent(context, Alarme.class);
        intententionLancerAlarme.putExtra("nom", nom);
        intententionLancerAlarme.putExtra("description", description);
        intententionLancerAlarme.putExtra("tempsAlarme", dateAlarme);
        activite = (Activity) context;

        activite.startActivityForResult(intententionLancerAlarme, ACTIVITE_AJOUTER_ALARME);
    }

    protected void onActivityResult(int activite, int resultat, Intent donnees){
        switch (activite){
            case ACTIVITE_AJOUTER_ALARME:
                this.finish();
                break;
        }
    }
}
