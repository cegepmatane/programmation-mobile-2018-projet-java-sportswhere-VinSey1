package ca.qc.cgmatane.informatique.sportswhere.vue;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import ca.qc.cgmatane.informatique.sportswhere.R;
import ca.qc.cgmatane.informatique.sportswhere.donnee.TerrainDAO;

public class ListeTerrains extends AppCompatActivity {

    static final public int ACTIVITE_LISTE_EVENEMENTS = 2;
    static final public int ACTIVITE_DETAILS_TERRAIN = 3;

    protected TerrainDAO accesseurTerrain;
    protected List<HashMap<String, String>> listeTerrainsPourAdapteur;
    protected ListView vueListeTerrains;
    private Intent intentionNaviguerListeEvenements;
    private Intent intentionNaviguerAccueil;
    private int nombreTerrains;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

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


        intentionNaviguerAccueil = new Intent(this, Accueil.class);

        Button actionNaviguerAccueil= (Button) findViewById(R.id.action_naviguer_accueil);

        actionNaviguerAccueil.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        startActivity(intentionNaviguerAccueil);
                    }
                }
        );

        vueListeTerrains.setOnItemClickListener(
            new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> parent,
                                        View vue,
                                        int positionDansAdapteur,
                                        long positionItem) {

                    ListView vueListeTerrains = (ListView) vue.getParent();

                    HashMap<String, String> terrain = (HashMap<String, String>) vueListeTerrains.getItemAtPosition((int)positionItem);

                    Intent intentionNaviguerDetailsTerrain = new Intent (ListeTerrains.this, DetailsTerrain.class);

                    intentionNaviguerDetailsTerrain.putExtra("id_terrain", terrain.get("id_terrain"));

                    intentionNaviguerDetailsTerrain.putExtra("nombre_terrains", listeTerrainsPourAdapteur.size());

                    startActivityForResult(intentionNaviguerDetailsTerrain, ACTIVITE_DETAILS_TERRAIN);
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
