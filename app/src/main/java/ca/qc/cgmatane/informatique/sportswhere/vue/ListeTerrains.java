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
import ca.qc.cgmatane.informatique.sportswhere.donnee.TerrainDAO;
import ca.qc.cgmatane.informatique.sportswhere.modele.Terrain;

public class ListeTerrains extends AppCompatActivity {

    static final public int ACTIVITE_LISTE_EVENEMENTS = 2;

    protected TerrainDAO accesseurTerrain;
    protected List<HashMap<String, String>> listeTerrainsPourAdapteur;
    protected ListView vueListeTerrains;
    private Intent intentionNaviguerListeEvenements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_liste_terrains);

        accesseurTerrain = TerrainDAO.getInstance();

        vueListeTerrains = (ListView) findViewById(R.id.vue_liste_terrains);

        afficherTousLesTerrains();

        intentionNaviguerListeEvenements = new Intent(this, ListeEvenements.class);

        Button actionNaviguerListeEvenements = (Button) findViewById(R.id.action_naviguer_liste_evenements);

        actionNaviguerListeEvenements.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        startActivityForResult(intentionNaviguerListeEvenements, ACTIVITE_LISTE_EVENEMENTS);
                    }
                }
        );
    }

    private void afficherTousLesTerrains(){
        listeTerrainsPourAdapteur = accesseurTerrain.recupererListeTerrainsPourAdapteur();

        SimpleAdapter adapteur = new SimpleAdapter(
                this,
                listeTerrainsPourAdapteur,
                android.R.layout.two_line_list_item,
                new String[] {"titre", "ville"},
                new int[] {android.R.id.text1, android.R.id.text2});

        vueListeTerrains.setAdapter(adapteur);
    }
}
