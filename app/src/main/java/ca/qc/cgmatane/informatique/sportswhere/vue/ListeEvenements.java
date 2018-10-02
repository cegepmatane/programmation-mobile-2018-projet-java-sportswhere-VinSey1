package ca.qc.cgmatane.informatique.sportswhere.vue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;

import ca.qc.cgmatane.informatique.sportswhere.R;
import ca.qc.cgmatane.informatique.sportswhere.donnee.EvenementDAO;

public class ListeEvenements extends AppCompatActivity {

    static final public int ACTIVITE_LISTE_TERRAINS = 1;

    protected EvenementDAO accesseurEvenement;
    protected List<HashMap<String, String>> listeEvenementsPourAdapteur;
    protected ListView vueListeEvenements;
    private Intent intentionNaviguerListeTerrains;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_liste_evenements);

        accesseurEvenement = EvenementDAO.getInstance();

        vueListeEvenements = (ListView) findViewById(R.id.vue_liste_evenements);

        afficherTousLesEvenements();

        intentionNaviguerListeTerrains = new Intent(this, ListeTerrains.class);

        Button actionNaviguerListeTerrains = (Button) findViewById(R.id.action_naviguer_liste_terrains);

        actionNaviguerListeTerrains.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        startActivityForResult(intentionNaviguerListeTerrains, ACTIVITE_LISTE_TERRAINS);
                    }
                }
        );
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
