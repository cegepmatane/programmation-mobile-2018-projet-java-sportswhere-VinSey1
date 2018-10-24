package ca.qc.cgmatane.informatique.sportswhere.vue;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import ca.qc.cgmatane.informatique.sportswhere.action.EcouteurSurBalayement;
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
    private ImageView imageView;
    private ScaleGestureDetector detector;
    private float scale = 1f;

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
        nombreTerrains = accesseurTerrain.getNombreTerrains();
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
        myView.setOnTouchListener(new EcouteurSurBalayement(DetailsTerrain.this) {
            public void balayageHaut() {}
            public void balayageGauche() {

                Intent intentionNaviguerDetailsTerrain = new Intent (DetailsTerrain.this, DetailsTerrain.class);

                int idTerrain = terrain.getId_terrain()+1;

                if(idTerrain>nombreTerrains-1){
                    idTerrain = 0;
                }

                intentionNaviguerDetailsTerrain.putExtra("id_terrain", ""+idTerrain);

                startActivityForResult(intentionNaviguerDetailsTerrain, ACTIVITE_DETAILS_TERRAIN);
            }
            public void balayageDroit() {

                Intent intentionNaviguerDetailsTerrain = new Intent (DetailsTerrain.this, DetailsTerrain.class);

                int idTerrain = terrain.getId_terrain()-1;

                if(idTerrain<0){
                    idTerrain = nombreTerrains-1;
                }

                intentionNaviguerDetailsTerrain.putExtra("id_terrain", ""+idTerrain);

                startActivityForResult(intentionNaviguerDetailsTerrain, ACTIVITE_DETAILS_TERRAIN);
            }
            public void balayageBas() {}

        });

        imageView=(ImageView)findViewById(R.id.imageView);
        detector = new ScaleGestureDetector(this,new ScaleListener());


    }

    public boolean onTouchEvent(MotionEvent event) {

        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {


        float onScaleBegin = 1f;
        float onScaleEnd = 1f;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            imageView.setScaleX(scale);
            imageView.setScaleY(scale);
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {

            imageView.setScaleX(onScaleBegin);
            imageView.setScaleY(onScaleBegin);


            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

            imageView.setScaleX(onScaleEnd);
            imageView.setScaleY(onScaleEnd);




            super.onScaleEnd(detector);
        }
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
