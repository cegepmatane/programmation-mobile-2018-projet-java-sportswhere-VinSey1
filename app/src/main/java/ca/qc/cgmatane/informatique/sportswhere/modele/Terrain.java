package ca.qc.cgmatane.informatique.sportswhere.modele;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class Terrain {

    protected int idTerrain;
    protected LatLng position;
    protected String titre;
    protected String description;
    protected String ville;
    protected String image;

    public Terrain(LatLng position, String titre, String description, String ville){
        this.position = position;
        this.titre = titre;
        this.description = description;
        this.ville = ville;
    }

    public Terrain(LatLng position, String titre, String description, String ville, int idTerrain){
        this.position = position;
        this.titre = titre;
        this.description = description;
        this.idTerrain = idTerrain;
        this.ville = ville;
    }

    public Terrain(LatLng position, String titre, String description, String ville, String image, int idTerrain){
        this.position = position;
        this.titre = titre;
        this.description = description;
        this.idTerrain = idTerrain;
        this.ville = ville;
        this.image = image;
    }

    public String getTitre(){
        return titre;
    }

    public String getDescription(){
        return description;
    }

    public LatLng getPosition(){
        return position;
    }

    public int getIdTerrain(){
        return idTerrain;
    }

    public String getVille(){
        return ville;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setPosition(LatLng position){
        this.position = position;
    }

    public HashMap<String, String> obtenirTerrainPourAdapteur(){
        HashMap<String, String> terrainPourAdapteur = new HashMap<String, String>();
        terrainPourAdapteur.put("titre", this.titre);
        terrainPourAdapteur.put("ville", this.ville);
        terrainPourAdapteur.put("idTerrain", ""+this.idTerrain);
        return terrainPourAdapteur;
    }
}
