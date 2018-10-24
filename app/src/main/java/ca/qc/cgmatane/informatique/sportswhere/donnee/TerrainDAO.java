package ca.qc.cgmatane.informatique.sportswhere.donnee;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ca.qc.cgmatane.informatique.sportswhere.modele.Terrain;

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
            xml = accesseurService.execute("http://158.69.192.249/sportswhere/terrain/liste_terrain.php", "</terrains>").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            initialiserTerrains();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void initialiserTerrains() throws IOException, SAXException, ParserConfigurationException {
        listeTerrains = new ArrayList<Terrain>();
        DocumentBuilder parseur = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = parseur.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        NodeList listeNoeudsTerrains = document.getElementsByTagName("terrain");

        for (int iterateur = 0; iterateur < listeNoeudsTerrains.getLength(); iterateur++) {
            Element noeudTerrain = (Element) listeNoeudsTerrains.item(iterateur);

            int id = Integer.parseInt(noeudTerrain.getElementsByTagName("id_terrain").item(0).getTextContent());
            String nom = noeudTerrain.getElementsByTagName("nom").item(0).getTextContent();
            String ville = noeudTerrain.getElementsByTagName("ville").item(0).getTextContent();
            String description = noeudTerrain.getElementsByTagName("description").item(0).getTextContent();
            String images = noeudTerrain.getElementsByTagName("images").item(0).getTextContent();
            Float latitude = Float.parseFloat(noeudTerrain.getElementsByTagName("latitude").item(0).getTextContent());
            Float longitude = Float.parseFloat(noeudTerrain.getElementsByTagName("longitude").item(0).getTextContent());

            LatLng position = new LatLng(latitude, longitude);

            Terrain terrain = new Terrain(position, nom, description, ville, id);

            listeTerrains.add(terrain);
        }
    }

    public Terrain trouverTerrain(String nom){
        for(Terrain terrain: this.listeTerrains){
            if(terrain.getTitre().equals(nom)) return terrain;
        }
        return null;
    }

    public Terrain trouverTerrain(int idTerrain){
        for(Terrain terrain: this.listeTerrains){
            if(terrain.getId_terrain() == idTerrain) return terrain;
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

    public List<Terrain> getListeTerrains() {
        return listeTerrains;
    }
}