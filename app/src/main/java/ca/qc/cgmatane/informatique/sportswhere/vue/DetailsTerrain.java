package ca.qc.cgmatane.informatique.sportswhere.vue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import ca.qc.cgmatane.informatique.sportswhere.R;
import ca.qc.cgmatane.informatique.sportswhere.donnee.EvenementDAO;
import ca.qc.cgmatane.informatique.sportswhere.donnee.TerrainDAO;
import ca.qc.cgmatane.informatique.sportswhere.modele.Terrain;

public class DetailsTerrain extends AppCompatActivity {

    protected TerrainDAO accesseurTerrain;
    protected EvenementDAO accesseurEvenement;
    protected Terrain terrain;
    protected ListView vueListeEvenements;
    protected List<HashMap<String, String>> listeEvenementsPourAdapteur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_details_terrain);

        this.accesseurEvenement = EvenementDAO.getInstance();
        this.accesseurTerrain = TerrainDAO.getInstance();

        vueListeEvenements = (ListView) findViewById(R.id.vue_liste_evenements);

        Bundle parametres = this.getIntent().getExtras();
        String parametre_id_terrain = (String) parametres.get("id_terrain");
        int id_terrain = Integer.parseInt(parametre_id_terrain);

        terrain = accesseurTerrain.trouverTerrain(id_terrain);

        afficherTousLesEvenements();

        TextView titre = (TextView) findViewById(R.id.titre_terrain);

        titre.setText(terrain.getTitre());
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
}
