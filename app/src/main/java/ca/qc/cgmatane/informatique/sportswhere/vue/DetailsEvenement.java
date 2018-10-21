package ca.qc.cgmatane.informatique.sportswhere.vue;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.qc.cgmatane.informatique.sportswhere.R;
import ca.qc.cgmatane.informatique.sportswhere.donnee.EvenementDAO;
import ca.qc.cgmatane.informatique.sportswhere.donnee.TerrainDAO;
import ca.qc.cgmatane.informatique.sportswhere.modele.Evenement;
import ca.qc.cgmatane.informatique.sportswhere.modele.Terrain;

public class DetailsEvenement extends AppCompatActivity {

    static final public int ACTIVITE_DETAILS_TERRAIN = 3;

    EvenementDAO accesseurEvenement;
    TerrainDAO accesseurTerrain;
    Evenement evenement;
    Terrain terrain;
    Intent intentionNaviguerDetailsTerrain;
    private Intent intentionNaviguerAccueil;
    Activity activite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_details_evenement);

        this.accesseurEvenement = EvenementDAO.getInstance();
        this.accesseurTerrain = TerrainDAO.getInstance();

        Bundle parametres = this.getIntent().getExtras();
        String parametre_id_evenement = (String) parametres.get("id_evenement");
        int id_evenement = Integer.parseInt(parametre_id_evenement);

        evenement = accesseurEvenement.trouverEvenement(id_evenement);
        terrain = accesseurTerrain.trouverTerrain(evenement.getTerrain());

        TextView nom = (TextView) findViewById(R.id.nom_evenement);
        TextView description = (TextView) findViewById(R.id.description_evenement);
        final TextView date = (TextView) findViewById(R.id.date_evenement);
        TextView titreTerrain = (TextView) findViewById(R.id.titre_terrain);
        TextView villeTerrain = (TextView) findViewById(R.id.ville_terrain);

        nom.setText(evenement.getNom());
        description.setText(evenement.getDescription());
        final Date dateEvenement = evenement.getDate();
        date.setText(dateEvenement.getYear()+"-"+dateEvenement.getMonth()+"-"+dateEvenement.getDate());
        titreTerrain.setText(terrain.getTitre());
        villeTerrain.setText(terrain.getVille());

        Button actionNaviguerDetailsTerrain = (Button) findViewById(R.id.action_naviguer_details_terrain);

        intentionNaviguerDetailsTerrain = new Intent(this, DetailsTerrain.class);

        actionNaviguerDetailsTerrain.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View arg0) {
                    intentionNaviguerDetailsTerrain.putExtra("id_terrain", ""+terrain.getId_terrain());
                    startActivityForResult(intentionNaviguerDetailsTerrain, ACTIVITE_DETAILS_TERRAIN);
                }
            }
        );

        CheckBox estInteresse = ( CheckBox ) findViewById( R.id.action_est_interesse );
        estInteresse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean estCoche){
                if(estCoche){
                    long dateAlarme = 0;
                    try{
                        dateAlarme = new SimpleDateFormat("dd/MM/yyyy").parse(
                                dateEvenement.getDay()+"/"+
                                dateEvenement.getMonth()+"/"+
                                dateEvenement.getYear()).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    evenement.ajouterAlarme(DetailsEvenement.this, dateAlarme);
                } else{

                }
            }
        });

        Button actionSupprimerEvenement = (Button) findViewById(R.id.action_supprimer_evenement);

        actionSupprimerEvenement.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View arg0) {
                    naviguerPrecedenteVue();
                    accesseurEvenement.supprimerEvenement(evenement.getId_evenement());
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

    }

    private void naviguerPrecedenteVue(){
        this.finish();
    };

}
