package ca.qc.cgmatane.informatique.sportswhere.donnee;

import android.database.Cursor;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ca.qc.cgmatane.informatique.sportswhere.modele.Terrain;

import static java.lang.Thread.sleep;

public class TerrainDAO {

    private static TerrainDAO instance = null;
    private ServiceDAO accesseurService;
    private String xml;

    protected List<Terrain> listeTerrains;

    public static TerrainDAO getInstance(){
        if(null == instance){
            instance = new TerrainDAO();
        }
        return instance;
    }

    public TerrainDAO(){
        try {
            accesseurService = new ServiceDAO();
            xml = accesseurService.execute("http://158.69.192.249/sportswhere/liste_terrain.php", "</terrains>").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            initialiserDonneesTestTerrains();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void initialiserDonneesTestTerrains() throws IOException, SAXException, ParserConfigurationException {
        listeTerrains = new ArrayList<Terrain>();
        DocumentBuilder parseur = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = parseur.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        NodeList listeNoeudAnnonces = document.getElementsByTagName("terrain");

        for (int iterateur = 0; iterateur < listeNoeudAnnonces.getLength(); iterateur++) {
            Element noeudAnnonce = (Element) listeNoeudAnnonces.item(iterateur);

            int id = Integer.parseInt(noeudAnnonce.getElementsByTagName("id_terrain").item(0).getTextContent());
            String nom = noeudAnnonce.getElementsByTagName("nom").item(0).getTextContent();
            String ville = noeudAnnonce.getElementsByTagName("ville").item(0).getTextContent();
            String description = noeudAnnonce.getElementsByTagName("description").item(0).getTextContent();
            String images = noeudAnnonce.getElementsByTagName("images").item(0).getTextContent();
            Float latitude = Float.parseFloat(noeudAnnonce.getElementsByTagName("latitude").item(0).getTextContent());
            Float longitude = Float.parseFloat(noeudAnnonce.getElementsByTagName("longitude").item(0).getTextContent());

            LatLng position = new LatLng(latitude, longitude);

            Terrain terrain = new Terrain(position, nom, description, ville, id);

            listeTerrains.add(terrain);
        }

        /*

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
        */
    }

    public Terrain trouverTerrain(String nom){
        for(Terrain terrain: this.listeTerrains){
            if(terrain.getTitre().equals(nom)) return terrain;
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