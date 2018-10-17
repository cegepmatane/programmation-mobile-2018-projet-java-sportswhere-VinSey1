package ca.qc.cgmatane.informatique.sportswhere.modele;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Terrain {

    protected int id_terrain;
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

    public Terrain(LatLng position, String titre, String description, String ville, int id_terrain){
        this.position = position;
        this.titre = titre;
        this.description = description;
        this.id_terrain = id_terrain;
        this.ville = ville;
    }

    public Terrain(LatLng position, String titre, String description, String ville, String image, int id_terrain){
        this.position = position;
        this.titre = titre;
        this.description = description;
        this.id_terrain = id_terrain;
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

    public int getId_terrain(){
        return id_terrain;
    }

    public String getVille(){
        return ville;
    }

    public void setVille(String ville){
        this.ville = ville;
    }

    public void setId_terrain(int id_terrain){
        this.id_terrain = id_terrain;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setPosition(LatLng position){
        this.position = position;
    }

    public void setTitre(String titre){
        this.titre = titre;
    }

    public HashMap<String, String> obtenirTerrainPourAdapteur(){
        HashMap<String, String> terrainPourAdapteur = new HashMap<String, String>();
        terrainPourAdapteur.put("titre", this.titre);
        terrainPourAdapteur.put("ville", this.ville);
        terrainPourAdapteur.put("id_terrain", ""+this.id_terrain);
        return terrainPourAdapteur;
    }
}
