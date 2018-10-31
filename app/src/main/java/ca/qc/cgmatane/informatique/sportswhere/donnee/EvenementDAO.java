package ca.qc.cgmatane.informatique.sportswhere.donnee;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ca.qc.cgmatane.informatique.sportswhere.modele.Evenement;

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
            xml = accesseurService.execute("http://158.69.192.249/sportswhere/evenement/liste_evenements.php", "</evenements>").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            initialiserEvenements();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    private void initialiserEvenements() throws ParserConfigurationException, IOException, SAXException {

        listeEvenements = new ArrayList<Evenement>();
        DocumentBuilder parseur = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = parseur.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        NodeList listeNoeudEvenements = document.getElementsByTagName("evenement");

        for (int iterateur = 0; iterateur < listeNoeudEvenements.getLength(); iterateur++) {
            Element noeudEvenement = (Element) listeNoeudEvenements.item(iterateur);

            int id = Integer.parseInt(noeudEvenement.getElementsByTagName("id_evenement").item(0).getTextContent());
            String nom = noeudEvenement.getElementsByTagName("nom").item(0).getTextContent();
            String description = noeudEvenement.getElementsByTagName("description").item(0).getTextContent();
            Long date  = Long.parseLong(noeudEvenement.getElementsByTagName("date").item(0).getTextContent());
            int terrain = Integer.parseInt(noeudEvenement.getElementsByTagName("terrain").item(0).getTextContent());

            Evenement evenement = new Evenement(date, nom, description, terrain, id);

            listeEvenements.add(evenement);
        }
    }

    public Evenement trouverEvenement(int idEvenement){
        for(Evenement evenement: this.listeEvenements){
            if(evenement.getIdEvenement() == idEvenement) return evenement;
        }
        return null;
    }

    public List<HashMap<String,String>> recupererListeEvenementsPourAdapteur() {
        List<HashMap<String, String>> listeEvenementsPourAdaptateur = new ArrayList<HashMap<String, String>>();

        for(Evenement evenement : listeEvenements){
            listeEvenementsPourAdaptateur.add(evenement.obtenirEvenementPourAdapteur());
        }
        return listeEvenementsPourAdaptateur;
    }

    public List<HashMap<String,String>> recupererListeEvenementsPourAdapteur(int idTerrain) {
        List<HashMap<String, String>> listeEvenementsPourAdaptateur = new ArrayList<HashMap<String, String>>();
        for(Evenement evenement : listeEvenements){
            if(evenement.getTerrain() == idTerrain) listeEvenementsPourAdaptateur.add(evenement.obtenirEvenementPourAdapteur());
        }
        return listeEvenementsPourAdaptateur;
    }

    public void ajouterEvenement(Evenement evenement) {
        try {
            URL urlAjouterEvenement = new URL("http://158.69.192.249/sportswhere/evenement/ajouter.php");
            HttpURLConnection connexion = (HttpURLConnection) urlAjouterEvenement.openConnection();
            connexion.setDoOutput(true);
            connexion.setRequestMethod("POST");

            OutputStreamWriter envoyeur = new ServiceEnvoyerDAO().execute(connexion).get();

            envoyeur.write("nom=" + evenement.getNom()
                    +"&description=" + evenement.getDescription()
                    +"&date=" + evenement.getDate()
                    +"&terrain=" + evenement.getTerrain()
            );

            envoyeur.close();

            InputStream fluxLecture = new ServiceFluxDAO().execute(connexion).get();

            connexion.disconnect();

            initialiserEvenements();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void supprimerEvenement(int idEvenement) {
        for (Iterator<Evenement> iterateur = listeEvenements.iterator(); iterateur.hasNext(); ) {
            Evenement evenement = iterateur.next();
            if (evenement.getIdEvenement() == idEvenement) {
                iterateur.remove();
            }
        }
    }
}