package ca.qc.cgmatane.informatique.sportswhere.modele;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import ca.qc.cgmatane.informatique.sportswhere.vue.Alarme;
import ca.qc.cgmatane.informatique.sportswhere.vue.DetailsEvenement;

public class Evenement extends AppCompatActivity {

    protected Long date;
    protected String nom;
    protected String description;
    protected int terrain;
    protected int id_evenement;
    protected static final int ACTIVITE_AJOUTER_ALARME = 6;


    public Evenement(Long date, String nom, String description, int terrain){
        this.date = date;
        this.nom = nom;
        this.description = description;
        this.terrain = terrain;
    }

    public Evenement(Long date, String nom, String description, int terrain, int id_evenement){
        this.date = date;
        this.nom = nom;
        this.description = description;
        this.terrain = terrain;
        this.id_evenement = id_evenement;
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

    public int getId_evenement(){
        return id_evenement;
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

    public void setId_evenement(int id_evenement){
        this.id_evenement = id_evenement;
    }

    public HashMap<String,String> obtenirEvenementPourAdapteur() {
        HashMap<String, String> evenementPourAdapteur = new HashMap<String, String>();
        evenementPourAdapteur.put("nom", this.nom);
        evenementPourAdapteur.put("date", ""+this.getDateTexte());
        evenementPourAdapteur.put("terrain", ""+this.terrain);
        evenementPourAdapteur.put("id_evenement", ""+this.id_evenement);
        return evenementPourAdapteur;
    }

    Activity activite;
    public void ajouterAlarme(Context context, long dateAlarme){
        Log.d("Contexte : ", ""+context);
        Intent intententionLancerAlarme = new Intent(context, Alarme.class);
        intententionLancerAlarme.putExtra("nom", this.getNom());
        intententionLancerAlarme.putExtra("description", this.getDescription());
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
