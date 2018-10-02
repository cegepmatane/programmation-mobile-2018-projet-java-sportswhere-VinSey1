package ca.qc.cgmatane.informatique.sportswhere.donnee;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.qc.cgmatane.informatique.sportswhere.modele.Evenement;

public class EvenementDAO {

    private static EvenementDAO instance = null;

    protected List<Evenement> listeEvenements;

    public static EvenementDAO getInstance(){
        if(null == instance){
            instance = new EvenementDAO();
        }
        return instance;
    }

    public EvenementDAO(){
        initialiserDonneesTestEvenements();
    }

    private void initialiserDonneesTestEvenements(){

        listeEvenements = new ArrayList<Evenement>();

        Date date = new Date(2018, 10, 02);
        String nom = "Match de soccer";
        String description = "Match entre les Capitaines et Rimouski";
        Evenement evenement = new Evenement(date, nom, description, 1, 1);
        listeEvenements.add(evenement);

        date = new Date(2018, 10, 10);
        nom = "Match de football";
        description = "Match entre les Capitaines et la Polyvalence";
        evenement = new Evenement(date, nom, description, 2, 2);
        listeEvenements.add(evenement);

        date = new Date(2018, 11, 05);
        nom = "Séance de cinéma";
        description = "Rediffusion de 'Histoire de jouets'";
        evenement = new Evenement(date, nom, description, 1, 3);
        listeEvenements.add(evenement);

        date = new Date(2020, 01, 07);
        nom = "Concert";
        description = "Concert de l'incroyable F. Levy";
        evenement = new Evenement(date, nom, description, 2, 4);
        listeEvenements.add(evenement);

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
}
