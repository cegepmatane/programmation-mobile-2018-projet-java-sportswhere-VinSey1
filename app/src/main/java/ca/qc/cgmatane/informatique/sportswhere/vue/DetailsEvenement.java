package ca.qc.cgmatane.informatique.sportswhere.vue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;

import ca.qc.cgmatane.informatique.sportswhere.R;
import ca.qc.cgmatane.informatique.sportswhere.donnee.EvenementDAO;
import ca.qc.cgmatane.informatique.sportswhere.donnee.TerrainDAO;
import ca.qc.cgmatane.informatique.sportswhere.modele.Evenement;
import ca.qc.cgmatane.informatique.sportswhere.modele.Terrain;

public class DetailsEvenement extends AppCompatActivity {

    EvenementDAO accesseurEvenement;
    TerrainDAO accesseurTerrain;
    Evenement evenement;
    Terrain terrain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        TextView date = (TextView) findViewById(R.id.date_evenement);
        TextView titreTerrain = (TextView) findViewById(R.id.titre_terrain);
        TextView villeTerrain = (TextView) findViewById(R.id.ville_terrain);

        nom.setText(evenement.getNom());
        description.setText(evenement.getDescription());
        Date dateEvenement = evenement.getDate();
        date.setText(dateEvenement.getYear()+"-"+dateEvenement.getMonth()+"-"+dateEvenement.getDate());
        titreTerrain.setText(terrain.getTitre());
        villeTerrain.setText(terrain.getVille());
    }
}
