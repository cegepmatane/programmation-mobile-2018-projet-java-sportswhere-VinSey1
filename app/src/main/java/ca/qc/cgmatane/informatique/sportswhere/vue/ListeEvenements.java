package ca.qc.cgmatane.informatique.sportswhere.vue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;

import ca.qc.cgmatane.informatique.sportswhere.R;
import ca.qc.cgmatane.informatique.sportswhere.donnee.EvenementDAO;
import ca.qc.cgmatane.informatique.sportswhere.modele.Evenement;

public class ListeEvenements extends AppCompatActivity {

    protected EvenementDAO accesseurEvenement;
    protected List<HashMap<String, String>> listeEvenementsPourAdapteur;
    protected ListView vueListeEvenements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_liste_evenements);

        accesseurEvenement = EvenementDAO.getInstance();

        vueListeEvenements = (ListView) findViewById(R.id.vue_liste_evenements);

        afficherTousLesEvenements();
    }

    private void afficherTousLesEvenements(){
        listeEvenementsPourAdapteur = accesseurEvenement.recupererListeEvenementsPourAdapteur();

        SimpleAdapter adapteur = new SimpleAdapter(
                this,
                listeEvenementsPourAdapteur,
                android.R.layout.two_line_list_item,
                new String[] {"nom", "date"},
                new int[] {android.R.id.text1, android.R.id.text2});

        vueListeEvenements.setAdapter(adapteur);
    }
}
