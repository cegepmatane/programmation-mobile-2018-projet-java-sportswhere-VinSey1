package ca.qc.cgmatane.informatique.sportswhere.vue;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import ca.qc.cgmatane.informatique.sportswhere.R;
import ca.qc.cgmatane.informatique.sportswhere.donnee.EvenementDAO;
import ca.qc.cgmatane.informatique.sportswhere.donnee.Interet;
import ca.qc.cgmatane.informatique.sportswhere.donnee.InteretDAO;
import ca.qc.cgmatane.informatique.sportswhere.donnee.TerrainDAO;
import ca.qc.cgmatane.informatique.sportswhere.modele.Evenement;
import ca.qc.cgmatane.informatique.sportswhere.modele.Terrain;

public class DetailsEvenement extends AppCompatActivity {

    static final public int ACTIVITE_DETAILS_TERRAIN = 3;

    private EvenementDAO accesseurEvenement;
    private TerrainDAO accesseurTerrain;
    private Evenement evenement;
    private Terrain terrain;
    private Intent intentionNaviguerDetailsTerrain;
    private Intent intentionNaviguerAccueil;
    private InteretDAO accesseurInteret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar barreAction = getSupportActionBar();
        barreAction.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_details_evenement);

        this.accesseurInteret = InteretDAO.getInstance(getApplicationContext());
        this.accesseurEvenement = EvenementDAO.getInstance();
        this.accesseurTerrain = TerrainDAO.getInstance();

        Bundle parametres = this.getIntent().getExtras();
        String parametreIdEvenement = (String) parametres.get("idEvenement");
        int idEvenement = Integer.parseInt(parametreIdEvenement);

        evenement = accesseurEvenement.trouverEvenement(idEvenement);
        terrain = accesseurTerrain.trouverTerrain(evenement.getTerrain());

        TextView nom = findViewById(R.id.nom_evenement);
        TextView description = findViewById(R.id.description_evenement);
        final TextView date = findViewById(R.id.date_evenement);
        TextView titreTerrain = findViewById(R.id.titre_terrain);
        TextView villeTerrain = findViewById(R.id.ville_terrain);

        nom.setText(evenement.getNom());
        description.setText(evenement.getDescription());
        date.setText(evenement.getDateTexte());
        titreTerrain.setText(terrain.getTitre());
        villeTerrain.setText(terrain.getVille());

        Button actionNaviguerDetailsTerrain = findViewById(R.id.action_naviguer_details_terrain);

        intentionNaviguerDetailsTerrain = new Intent(this, DetailsTerrain.class);

        actionNaviguerDetailsTerrain.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View arg0) {
                    intentionNaviguerDetailsTerrain.putExtra("idTerrain", ""+terrain.getIdTerrain());
                    startActivityForResult(intentionNaviguerDetailsTerrain, ACTIVITE_DETAILS_TERRAIN);
                }
            }
        );

        final CheckBox estInteresse = findViewById( R.id.action_est_interesse );

        estInteresse.setChecked(verifierInteretCoche());

        estInteresse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean estCoche){
                if(estCoche){
                    long dateAlarme = evenement.getDate();
                    evenement.ajouterAlarme(DetailsEvenement.this, dateAlarme);
                    enregistrerInteret(true);
                } else{
                    enregistrerInteret(false);
                }
            }
        });

        findViewById(R.id.fenetre_details_evenement).setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(DetailsEvenement.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {

                    if (estInteresse.isChecked()==false) {
                        estInteresse.setChecked(true);
                    }
                    else {
                        estInteresse.setChecked(false);
                    }

                    return super.onDoubleTap(e);
                }

            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        Button actionSupprimerEvenement = findViewById(R.id.action_supprimer_evenement);

        actionSupprimerEvenement.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View arg0) {
                    naviguerPrecedenteVue();
                    accesseurEvenement.supprimerEvenement(evenement.getIdEvenement());
                }
            }
        );

        intentionNaviguerAccueil = new Intent(this, Accueil.class);

        Button actionNaviguerAccueil= findViewById(R.id.action_naviguer_accueil);

        actionNaviguerAccueil.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        startActivity(intentionNaviguerAccueil);
                    }
                }
        );

    }

    private void naviguerPrecedenteVue(){
        this.finish();
    };

    private void enregistrerInteret(boolean coche){
        Interet nouveauInteret = new Interet(evenement.getIdEvenement(), coche);
        Log.d("Test nouveauInteret ", ""+nouveauInteret.isCoche());
        if (!verifierInteret()) {
            accesseurInteret.ajouterInteret(nouveauInteret);
        }
        else {
            accesseurInteret.modifierInteret(nouveauInteret);
        }
    }

    private boolean verifierInteretCoche() {
        Interet interet = accesseurInteret.chercherInteret(evenement.getIdEvenement());
        if (interet == null) {
            return false;
        }
        else {
            return interet.isCoche();
        }
    }

    private boolean verifierInteret() {
        if (accesseurInteret.chercherInteret(evenement.getIdEvenement()) == null) {
            return false;
        }
        else {
            return true;
        }
    }
}