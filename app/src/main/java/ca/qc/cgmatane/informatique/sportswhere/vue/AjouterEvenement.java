package ca.qc.cgmatane.informatique.sportswhere.vue;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.qc.cgmatane.informatique.sportswhere.R;
import ca.qc.cgmatane.informatique.sportswhere.donnee.EvenementDAO;
import ca.qc.cgmatane.informatique.sportswhere.donnee.TerrainDAO;
import ca.qc.cgmatane.informatique.sportswhere.modele.Evenement;
import ca.qc.cgmatane.informatique.sportswhere.modele.Terrain;

public class AjouterEvenement extends AppCompatActivity {

    protected EvenementDAO accesseurEvenement;
    protected TerrainDAO accesseurTerrain;
    protected Terrain terrain;
    protected EditText champNom;
    protected EditText champDescription;
    protected EditText champDate;
    private Intent intentionNaviguerAccueil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_ajouter_evenement);

        this.accesseurEvenement = EvenementDAO.getInstance();
        this.accesseurTerrain = TerrainDAO.getInstance();

        Bundle parametres = this.getIntent().getExtras();
        int parametre_terrain = (Integer) parametres.get("terrain");
        terrain = accesseurTerrain.trouverTerrain(parametre_terrain);

        TextView titre = (TextView) findViewById(R.id.titre_terrain);

        titre.setText(terrain.getTitre());

        champNom = (EditText) findViewById(R.id.vue_ajouter_evenement_nom);
        champDescription = (EditText) findViewById(R.id.vue_ajouter_evenement_description);
        champDate = (EditText) findViewById(R.id.vue_ajouter_evenement_date);

        Button actionEnregisterEvenement = (Button) findViewById(R.id.action_ajouter_evenement);

        actionEnregisterEvenement.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View arg0) {
                    try {
                        ajouterEvenement();
                    } catch (ParseException e) {
                        Toast.makeText(AjouterEvenement.this, "Merci de rentrer une date correcte", Toast.LENGTH_SHORT).show();
                    }
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

    private void ajouterEvenement() throws ParseException {
        if (champNom.getText().toString().isEmpty() || champNom.getText().toString().isEmpty() || champDate.getText().toString().isEmpty()) {
            Toast.makeText(AjouterEvenement.this, "Merci de remplir tous les champs", Toast.LENGTH_SHORT).show();
        } else {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(champDate.getText().toString());
            if (date.getTime() < System.currentTimeMillis()) {
                Toast.makeText(AjouterEvenement.this, "Merci de rentrer une date correcte", Toast.LENGTH_SHORT).show();
            } else {
                Evenement evenement = new Evenement(date.getTime(),
                        champNom.getText().toString(),
                        champDescription.getText().toString(),
                        terrain.getId_terrain());
                accesseurEvenement.ajouterEvenement(evenement);
                naviguerRetourDetailsTerrain();
            }
        }
    }

    private void naviguerRetourDetailsTerrain() {
        this.finish();
    }
}
