package ca.qc.cgmatane.informatique.sportswhere.vue;

import android.content.Intent;
import android.provider.MediaStore;
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
    private ImageView vueImage;
    private ScaleGestureDetector detecteur;
    private float echelle = 1f;
    private final static int APPAREIL_PHOTO = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar barreAction = getSupportActionBar();
        barreAction.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_details_terrain);

        Button prendrePhoto = findViewById(R.id.action_modifier_image);

        prendrePhoto.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        Intent intentionPrendrePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(intentionPrendrePhoto.resolveActivity(getPackageManager()) != null){
                            startActivityForResult(intentionPrendrePhoto, APPAREIL_PHOTO);
                        }
                    }
                }
        );

        this.accesseurEvenement = EvenementDAO.getInstance();
        this.accesseurTerrain = TerrainDAO.getInstance();

        vueListeEvenements = findViewById(R.id.vue_liste_evenements);

        Bundle parametres = this.getIntent().getExtras();
        String parametreIdTerrain = (String) parametres.get("idTerrain");
        int idTerrain = Integer.parseInt(parametreIdTerrain);
        nombreTerrains = accesseurTerrain.getNombreTerrains();
        terrain = accesseurTerrain.trouverTerrain(idTerrain);

        afficherTousLesEvenements();

        intentionNaviguerAjouterEvenement = new Intent(this, AjouterEvenement.class);

        Button actionNaviguerAjouterEvenement = findViewById(R.id.action_naviguer_ajouter_evenement);

        actionNaviguerAjouterEvenement.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        intentionNaviguerAjouterEvenement.putExtra("terrain", terrain.getIdTerrain());
                        startActivityForResult(intentionNaviguerAjouterEvenement, ACTIVITE_AJOUTER_EVENEMENT);
                    }
                }
        );

        intentionNaviguerAccueil = new Intent(this, Accueil.class);

        Button actionNaviguerAccueil = findViewById(R.id.action_naviguer_accueil);

        actionNaviguerAccueil.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        startActivity(intentionNaviguerAccueil);
                    }
                }
        );
      
        TextView titre = findViewById(R.id.titre_terrain);

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

                        intentionNaviguerDetailsEvenement.putExtra("idEvenement", evenement.get("idEvenement"));

                        startActivityForResult(intentionNaviguerDetailsEvenement, ACTIVITE_DETAILS_EVENEMENT);
                    }
                }
        );

        View vue = findViewById(R.id.fenetre_details_terrain);
        vue.setOnTouchListener(new EcouteurSurBalayement(DetailsTerrain.this) {
            public void balayageHaut() {}
            public void balayageGauche() {

                Intent intentionNaviguerDetailsTerrain = new Intent (DetailsTerrain.this, DetailsTerrain.class);

                int idTerrain = terrain.getIdTerrain()+1;

                if(idTerrain>nombreTerrains-1){
                    idTerrain = 0;
                }

                intentionNaviguerDetailsTerrain.putExtra("idTerrain", ""+idTerrain);

                startActivityForResult(intentionNaviguerDetailsTerrain, ACTIVITE_DETAILS_TERRAIN);
            }
            public void balayageDroit() {

                Intent intentionNaviguerDetailsTerrain = new Intent (DetailsTerrain.this, DetailsTerrain.class);

                int idTerrain = terrain.getIdTerrain()-1;

                if(idTerrain<0){
                    idTerrain = nombreTerrains-1;
                }

                intentionNaviguerDetailsTerrain.putExtra("idTerrain", ""+idTerrain);

                startActivityForResult(intentionNaviguerDetailsTerrain, ACTIVITE_DETAILS_TERRAIN);
            }
            public void balayageBas() {}

        });

        vueImage = findViewById(R.id.imageView);
        detecteur = new ScaleGestureDetector(this,new EcouteEchelle());


    }

    public boolean onTouchEvent(MotionEvent evenement) {

        detecteur.onTouchEvent(evenement);
        return super.onTouchEvent(evenement);
    }

    private class EcouteEchelle extends ScaleGestureDetector.SimpleOnScaleGestureListener {


        float debutEchelle = 1f;
        float finEchelle = 1f;

        @Override
        public boolean onScale(ScaleGestureDetector detecteur) {
            echelle *= detecteur.getScaleFactor();
            vueImage.setScaleX(echelle);
            vueImage.setScaleY(echelle);
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detecteur) {

            vueImage.setScaleX(debutEchelle);
            vueImage.setScaleY(debutEchelle);

            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detecteur) {

            vueImage.setScaleX(finEchelle);
            vueImage.setScaleY(finEchelle);

            super.onScaleEnd(detecteur);
        }
    }

    private void afficherTousLesEvenements(){
        listeEvenementsPourAdapteur = accesseurEvenement.recupererListeEvenementsPourAdapteur(terrain.getIdTerrain());

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
