package ca.qc.cgmatane.informatique.sportswhere.donnee;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ca.qc.cgmatane.informatique.sportswhere.modele.Evenement;
import ca.qc.cgmatane.informatique.sportswhere.modele.Terrain;

public class EvenementDAO {

    private static EvenementDAO instance = null;
    private String xml;
    private ServiceDAO accesseurService;

    protected List<Evenement> listeEvenements;

    public static EvenementDAO getInstance(){
        if(null == instance){
            instance = new EvenementDAO();
        }
        return instance;
    }

    public EvenementDAO(){
        try {
            accesseurService = new ServiceDAO();
            xml = accesseurService.execute("http://158.69.192.249/sportswhere/liste_evenements.php", "</evenements>").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            initialiserDonneesTestEvenements();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    private void initialiserDonneesTestEvenements() throws ParserConfigurationException, IOException, SAXException {

        listeEvenements = new ArrayList<Evenement>();
        DocumentBuilder parseur = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = parseur.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        NodeList listeNoeudAnnonces = document.getElementsByTagName("evenement");

        for (int iterateur = 0; iterateur < listeNoeudAnnonces.getLength(); iterateur++) {
            Element noeudAnnonce = (Element) listeNoeudAnnonces.item(iterateur);

            int id = Integer.parseInt(noeudAnnonce.getElementsByTagName("id_evenement").item(0).getTextContent());
            String nom = noeudAnnonce.getElementsByTagName("nom").item(0).getTextContent();
            String description = noeudAnnonce.getElementsByTagName("description").item(0).getTextContent();
            Long dateEnMillisecondes  = Long.parseLong(noeudAnnonce.getElementsByTagName("date").item(0).getTextContent());
            int terrain = Integer.parseInt(noeudAnnonce.getElementsByTagName("terrain").item(0).getTextContent());

            Date date = new Date(dateEnMillisecondes);

            Evenement evenement = new Evenement(date, nom, description, terrain, id);

            listeEvenements.add(evenement);
        }
        /*
        listeEvenements = new ArrayList<Evenement>();

        Date date = new Date(2018, 10, 2);
        String nom = "Match de soccer";
        String description = "Match entre les Capitaines et Rimouski";
        Evenement evenement = new Evenement(date, nom, description, 0, 1);
        listeEvenements.add(evenement);

        date = new Date(2018, 10, 1);
        nom = "Match de football";
        description = "Match entre les Capitaines et la Polyvalence";
        evenement = new Evenement(date, nom, description, 1, 2);
        listeEvenements.add(evenement);

        date = new Date(2018, 11, 5);
        nom = "Séance de cinéma";
        description = "Rediffusion de 'Histoire de jouets'";
        evenement = new Evenement(date, nom, description, 0, 3);
        listeEvenements.add(evenement);

        date = new Date(2020, 01, 7);
        nom = "Concert";
        description = "Concert de l'incroyable F. Levy";
        evenement = new Evenement(date, nom, description, 1, 4);
        listeEvenements.add(evenement);
        */

    }

    public Evenement trouverEvenement(String nom){
        for(Evenement evenement: this.listeEvenements){
            if(evenement.getNom() == nom) return evenement;
        }
        return null;
    }

    public Evenement trouverEvenement(int id_evenement){
        for(Evenement evenement: this.listeEvenements){
            if(evenement.getId_evenement() == id_evenement) return evenement;
        }
        return null;
    }

    public List<Evenement> listerEvenements() {
        return listeEvenements;
    }

    public List<Evenement> listerEvenementsParTerrain(int id_terrain){
        List<Evenement> evenementsDuTerrain = new ArrayList<Evenement>();
        for(Evenement evenement: this.listeEvenements){
            if(evenement.getTerrain() == id_terrain) evenementsDuTerrain.add(evenement);
        }
        return evenementsDuTerrain;
    }

    public List<HashMap<String,String>> recupererListeEvenementsPourAdapteur() {
        List<HashMap<String, String>> listeEvenementsPourAdaptateur = new ArrayList<HashMap<String, String>>();

        for(Evenement evenement : listeEvenements){
            listeEvenementsPourAdaptateur.add(evenement.obtenirEvenementPourAdapteur());
        }
        return listeEvenementsPourAdaptateur;
    }

    public List<HashMap<String,String>> recupererListeEvenementsPourAdapteur(int id_terrain) {
        List<HashMap<String, String>> listeEvenementsPourAdaptateur = new ArrayList<HashMap<String, String>>();
        for(Evenement evenement : listeEvenements){
            if(evenement.getTerrain() == id_terrain) listeEvenementsPourAdaptateur.add(evenement.obtenirEvenementPourAdapteur());
        }
        return listeEvenementsPourAdaptateur;
    }

    public void ajouterEvenement(Evenement evenement) {
        listeEvenements.add(evenement);
    }

    public void supprimerEvenement(int id_evenement) {
        for (Iterator<Evenement> iterateur = listeEvenements.iterator(); iterateur.hasNext(); ) {
            Evenement evenement = iterateur.next();
            if (evenement.getId_evenement() == id_evenement) {
                iterateur.remove();
            }
        }
    }
}
