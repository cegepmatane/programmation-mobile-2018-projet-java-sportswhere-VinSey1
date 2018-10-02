package ca.qc.cgmatane.informatique.sportswhere.vue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;

import ca.qc.cgmatane.informatique.sportswhere.R;
import ca.qc.cgmatane.informatique.sportswhere.donnee.TerrainDAO;
import ca.qc.cgmatane.informatique.sportswhere.modele.Terrain;

public class ListeTerrains extends AppCompatActivity {

    protected TerrainDAO accesseurTerrain;
    protected List<Terrain> listeTerrains;
    protected List<HashMap<String, String>> listeTerrainsPourAdapteur;
    protected ListView vueListeTerrains;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_liste_terrains);

        accesseurTerrain = TerrainDAO.getInstance();

        vueListeTerrains = (ListView) findViewById(R.id.vue_liste_terrains);

        afficherTousLesTerrains();
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
