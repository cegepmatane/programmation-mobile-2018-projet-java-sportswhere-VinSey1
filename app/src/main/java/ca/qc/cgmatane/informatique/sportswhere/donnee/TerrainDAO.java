package ca.qc.cgmatane.informatique.sportswhere.donnee;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import ca.qc.cgmatane.informatique.sportswhere.modele.Terrain;

public class TerrainDAO {

    private static TerrainDAO instance = null;

    protected List<Terrain> listeTerrains;

    public static TerrainDAO getInstance(){
        if(null == instance){
            instance = new TerrainDAO();
        }
        return instance;
    }

    public TerrainDAO(){
        initialiserDonneesTestTerrains();
    }

    private void initialiserDonneesTestTerrains(){

        listeTerrains = new ArrayList<Terrain>();

        LatLng position = new LatLng( 48.840897, -67.50821);
        String titre = "Terrain de la Polyvalente de Matane";
        String description = "1 terrain en vrai gazon";
        Terrain terrain = new Terrain(position, titre, description, 1);
        listeTerrains.add(terrain);

        position = new LatLng(48.840691 ,   -67.497435);
        titre = "Terrain du CEGEP de Matane";
        description = "2 terrains en vrai gazon\n1 terrain synthétique";
        terrain = new Terrain(position, titre, description, 2);
        listeTerrains.add(terrain);

    }

    public Terrain trouverTerrain(String nom){
        for(Terrain terrain: this.listeTerrains){
            if(terrain.getTitre() == nom) return terrain;
        }
        return null;
    }

    public Terrain trouverTerrain(int id_terrain){
        for(Terrain terrain: this.listeTerrains){
            if(terrain.getId_terrain() == id_terrain) return terrain;
        }
        return null;
    }

    public List<Terrain> listerTerrains(){
        return listeTerrains;
    }
}