package ca.qc.cgmatane.informatique.sportswhere.modele;

import com.google.android.gms.maps.model.LatLng;

public class Terrain {

    protected int id_terrain;
    protected LatLng position;
    protected String titre;
    protected String description;

    public Terrain(LatLng position, String titre, String description){
        this.position = position;
        this.titre = titre;
        this.description = description;
    }

    public Terrain(LatLng position, String titre, String description, int id_terrain){
        this.position = position;
        this.titre = titre;
        this.description = description;
        this.id_terrain = id_terrain;
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
}
