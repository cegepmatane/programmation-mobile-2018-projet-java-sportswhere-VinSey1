package ca.qc.cgmatane.informatique.sportswhere.vue;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;

import ca.qc.cgmatane.informatique.sportswhere.R;
import ca.qc.cgmatane.informatique.sportswhere.donnee.EvenementDAO;

public class ListeEvenements extends AppCompatActivity {

    static final public int ACTIVITE_LISTE_TERRAINS = 1;
    static final public int ACTIVITE_DETAILS_EVENEMENT = 5;


    protected EvenementDAO accesseurEvenement;
    protected List<HashMap<String, String>> listeEvenementsPourAdapteur;
    protected ListView vueListeEvenements;
    private Intent intentionNaviguerListeTerrains;
    private Intent intentionNaviguerAccueil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar barreAction = getSupportActionBar();
        barreAction.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_liste_evenements);

        accesseurEvenement = EvenementDAO.getInstance();

        vueListeEvenements = findViewById(R.id.vue_liste_evenements);

        afficherTousLesEvenements();

        intentionNaviguerAccueil = new Intent(this, Accueil.class);

        Button actionNaviguerAccueil= findViewById(R.id.action_naviguer_accueil);

        actionNaviguerAccueil.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        startActivity(intentionNaviguerAccueil);
                    }
                }
        );

        intentionNaviguerListeTerrains = new Intent(this, ListeTerrains.class);

        Button actionNaviguerListeTerrains = findViewById(R.id.action_naviguer_liste_terrains);

        actionNaviguerListeTerrains.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View arg0) {
                        startActivityForResult(intentionNaviguerListeTerrains, ACTIVITE_LISTE_TERRAINS);
                    }
                }
        );

        gestionnaireTailleListe(vueListeEvenements);

        vueListeEvenements.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View vue,
                                            int positionDansAdapteur,
                                            long positionItem) {

                        ListView vueListeEvenements = (ListView) vue.getParent();

                        HashMap<String, String> evenement = (HashMap<String, String>) vueListeEvenements.getItemAtPosition((int)positionItem);

                        Intent intentionNaviguerDetailsEvenement = new Intent (ListeEvenements.this, DetailsEvenement.class);

                        intentionNaviguerDetailsEvenement.putExtra("idEvenement", evenement.get("idEvenement"));

                        startActivityForResult(intentionNaviguerDetailsEvenement, ACTIVITE_DETAILS_EVENEMENT);
                    }
                }
        );
    }

    private void afficherTousLesEvenements(){
        listeEvenementsPourAdapteur = accesseurEvenement.recupererListeEvenementsPourAdapteur();

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
            case ACTIVITE_DETAILS_EVENEMENT:
                afficherTousLesEvenements();
                break;
        }
    }

    /* Méthode pour régler le problème de hauteur de la liste
        Source : https://stackoverflow.com/questions/18367522/android-list-view-inside-a-scroll-view */
    private static void gestionnaireTailleListe(ListView listView) {
        ListAdapter adapteurListe = listView.getAdapter();
        if (adapteurListe == null){
            return;
        }

        int largeur = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int hauteurTotale = 0;
        View vue = null;
        for (int i = 0; i < adapteurListe.getCount(); i++) {
            vue = adapteurListe.getView(i, vue, listView);
            if (i == 0){
                vue.setLayoutParams(new ViewGroup.LayoutParams(largeur, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            vue.measure(largeur, View.MeasureSpec.UNSPECIFIED);
            hauteurTotale += vue.getMeasuredHeight();
        }
        ViewGroup.LayoutParams parametres = listView.getLayoutParams();
        parametres.height = hauteurTotale + (listView.getDividerHeight() * (adapteurListe.getCount() - 1));
        listView.setLayoutParams(parametres);
    }
}
