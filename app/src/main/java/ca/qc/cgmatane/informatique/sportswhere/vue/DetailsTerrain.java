package ca.qc.cgmatane.informatique.sportswhere.vue;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import ca.qc.cgmatane.informatique.sportswhere.OnSwipeTouchListener;
import ca.qc.cgmatane.informatique.sportswhere.R;
import ca.qc.cgmatane.informatique.sportswhere.donnee.EvenementDAO;
import ca.qc.cgmatane.informatique.sportswhere.donnee.TerrainDAO;
import ca.qc.cgmatane.informatique.sportswhere.modele.Terrain;

public class DetailsTerrain extends AppCompatActivity {

    static final public int ACTIVITE_AJOUTER_EVENEMENT = 4;
    static final public int ACTIVITE_DETAILS_EVENEMENT = 5;
    static final public int ACTIVITE_DETAILS_TERRAIN = 3;

    protected TerrainDAO accesseurTerrain;
    protected EvenementDAO accesseurEvenement;
    protected Terrain terrain;
    protected ListView vueListeEvenements;
    protected List<HashMap<String, String>> listeEvenementsPourAdapteur;
    private Intent intentionNaviguerAjouterEvenement;
    private Intent intentionNaviguerAccueil;
    private int nombreTerrains;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_details_terrain);

        this.accesseurEvenement = EvenementDAO.getInstance();
        this.accesseurTerrain = TerrainDAO.getInstance();

        vueListeEvenements = (ListView) findViewById(R.id.vue_liste_evenements);

        Bundle parametres = this.getIntent().getExtras();
        String parametre_id_terrain = (String) parametres.get("id_terrain");
        int id_terrain = Integer.parseInt(parametre_id_terrain);
        nombreTerrains = (Integer) parametres.get("nombre_terrains");
        terrain = accesseurTerrain.trouverTerrain(id_terrain);

        afficherTousLesEvenements();

        intentionNaviguerAjouterEvenement = new Intent(this, AjouterEvenement.class);

        Button actionNaviguerAjouterEvenement = (Button) findViewById(R.id.action_naviguer_ajouter_evenement);

        actionNaviguerAjouterEvenement.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        intentionNaviguerAjouterEvenement.putExtra("terrain", terrain.getId_terrain());
                        startActivityForResult(intentionNaviguerAjouterEvenement, ACTIVITE_AJOUTER_EVENEMENT);
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
      
        TextView titre = (TextView) findViewById(R.id.titre_terrain);

        titre.setText(terrain.getTitre());

        vueListeEvenements.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View vue,
                                            int positionDansAdapteur,
                                            long positionItem) {

                        ListView vueListeEvenements = (ListView) vue.getParent();

                        HashMap<String, String> evenement = (HashMap<String, String>) vueListeEvenements.getItemAtPosition((int)positionItem);

                        Intent intentionNaviguerDetailsEvenement = new Intent (DetailsTerrain.this, DetailsEvenement.class);

                        intentionNaviguerDetailsEvenement.putExtra("id_evenement", evenement.get("id_evenement"));

                        startActivityForResult(intentionNaviguerDetailsEvenement, ACTIVITE_DETAILS_EVENEMENT);
                    }
                }
        );

        View myView = findViewById(R.id.fenetre_details_terrain);
        myView.setOnTouchListener(new OnSwipeTouchListener(DetailsTerrain.this) {
            public void onSwipeTop() {}
            public void onSwipeRight() {

                Intent intentionNaviguerDetailsTerrain = new Intent (DetailsTerrain.this, DetailsTerrain.class);

                int idTerrain = terrain.getId_terrain()+1;

                if(idTerrain>nombreTerrains-1){
                    idTerrain = 0;
                }

                intentionNaviguerDetailsTerrain.putExtra("id_terrain", ""+idTerrain);

                intentionNaviguerDetailsTerrain.putExtra("nombre_terrains", nombreTerrains);

                startActivityForResult(intentionNaviguerDetailsTerrain, ACTIVITE_DETAILS_TERRAIN);
            }
            public void onSwipeLeft() {

                Intent intentionNaviguerDetailsTerrain = new Intent (DetailsTerrain.this, DetailsTerrain.class);

                int idTerrain = terrain.getId_terrain()-1;

                if(idTerrain<0){
                    idTerrain = nombreTerrains-1;
                }

                intentionNaviguerDetailsTerrain.putExtra("id_terrain", ""+idTerrain);

                intentionNaviguerDetailsTerrain.putExtra("nombre_terrains", nombreTerrains);

                startActivityForResult(intentionNaviguerDetailsTerrain, ACTIVITE_DETAILS_TERRAIN);
            }
            public void onSwipeBottom() {}

        });
    }

    private void afficherTousLesEvenements(){
        listeEvenementsPourAdapteur = accesseurEvenement.recupererListeEvenementsPourAdapteur(terrain.getId_terrain());

        SimpleAdapter adapteur = new SimpleAdapter(
                this,
                listeEvenementsPourAdapteur,
                android.R.layout.two_line_list_item,
                new String[] {"nom", "date"},
                new int[] {android.R.id.text1, android.R.id.text2});

        vueListeEvenements.setAdapter(adapteur);
    }

    protected void onActivityResult(int activite, int resultat, Intent donnees)
    {
        switch(activite)
        {
            case ACTIVITE_AJOUTER_EVENEMENT:
                afficherTousLesEvenements();
                break;

            case ACTIVITE_DETAILS_EVENEMENT:
                afficherTousLesEvenements();
                break;
        }
    }
}
