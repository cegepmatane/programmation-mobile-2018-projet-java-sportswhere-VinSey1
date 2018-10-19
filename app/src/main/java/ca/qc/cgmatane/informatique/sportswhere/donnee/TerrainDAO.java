package ca.qc.cgmatane.informatique.sportswhere.donnee;

import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
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
        String ville = "Matane";
        Terrain terrain = new Terrain(position, titre, description, ville, 0);
        listeTerrains.add(terrain);

        position = new LatLng(48.840691 ,   -67.497435);
        titre = "Terrain du CEGEP de Matane";
        description = "2 terrains en vrai gazon\n1 terrain synthétique";
        terrain = new Terrain(position, titre, description, ville,1);
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

    public List<HashMap<String, String>> recupererListeTerrainsPourAdapteur(){
        List<HashMap<String, String>> listeTerrainsPourAdaptateur = new ArrayList<HashMap<String, String>>();

        for(Terrain terrain : listeTerrains){
            listeTerrainsPourAdaptateur.add(terrain.obtenirTerrainPourAdapteur());
        }
        return listeTerrainsPourAdaptateur;
    }

    public int getNombreTerrains(){
        return listeTerrains.size();
    }

    public List<Terrain> listerTerrainBD() {
        /*
        String LISTER_TERRAINS = "SELECT * FROM terrain";
        Cursor curseur = accesseurBaseDeDonnees.getReadableDatabase().rawQuery(LISTER_TERRAINS,null);
        this.listeTerrains.clear();
        Terrain terrain;

        int indexId_Terrain = curseur.getColumnIndex("id_terrain");
        int indexTitre = curseur.getColumnIndex("titre");
        int indexVille = curseur.getColumnIndex("ville");
        int indexDescription = curseur.getColumnIndex("description");
        int indexImage = curseur.getColumnIndex("image");
        int indexLatitude = curseur.getColumnIndex("latitude");
        int indexLongitude = curseur.getColumnIndex("longitude");


        for (curseur.moveToFirst();!curseur.isAfterLast();curseur.moveToNext()) {
            int id_terrain = curseur.getInt(indexId_Terrain);
            String titre = curseur.getString(indexTitre);
            String ville = curseur.getString(indexVille);
            String description = curseur.getString(indexDescription);
            String image = curseur.getString(indexImage);
            int latitude = curseur.getInt(indexLatitude);
            int longitude = curseur.getInt(indexLongitude);
            LatLng position = new LatLng(latitude, longitude);
            terrain = new Terrain(position, titre, description, ville, image, id_terrain);
            this.listeTerrains.add(terrain);
        }

*/
        return listeTerrains;
    }

    public List<Terrain> listerTerrains() {
        return listeTerrains;
    }
}