package ca.qc.cgmatane.informatique.sportswhere.vue;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
    private ImageView imageTerrain;
    private View vue;
    private String imageURL;

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

        imageTerrain = findViewById(R.id.image_terrain);

        imageURL = "http://158.69.192.249/sportswhere/image/stockage/Terrain-"+terrain.getIdTerrain()+".jpg";
        Picasso.get().load(imageURL).into(imageTerrain);

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

                        Intent intentionNaviguerDetailsEvenement = new Intent (DetailsTerrain.this, DetailsEvenement.class);

                        intentionNaviguerDetailsEvenement.putExtra("idEvenement", evenement.get("idEvenement"));

                        startActivityForResult(intentionNaviguerDetailsEvenement, ACTIVITE_DETAILS_EVENEMENT);
                    }
                }
        );

        vue = findViewById(R.id.fenetre_details_terrain);
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

        vueImage = findViewById(R.id.image_terrain);
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
        super.onActivityResult(activite,resultat,donnees);
        switch(activite)
        {
            case ACTIVITE_AJOUTER_EVENEMENT:
                afficherTousLesEvenements();
                break;

            case ACTIVITE_DETAILS_EVENEMENT:
                afficherTousLesEvenements();
                break;

            case APPAREIL_PHOTO:
                if(resultat == RESULT_OK){

                    Bitmap bit = (Bitmap) donnees.getExtras().get("data");

                    String nomfichier = "Terrain-"+terrain.getIdTerrain();

                    new TransfererImage(bit, nomfichier).execute();

                    }
                break;
        }
    }

    private HttpParams getParametresRequeteHttp(){
        HttpParams parametresRequeteHttp = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(parametresRequeteHttp, 1000 * 30);
        HttpConnectionParams.setSoTimeout(parametresRequeteHttp, 1000 * 30);
        return parametresRequeteHttp;
    }


    private class TransfererImage extends AsyncTask<Void,Void,Void> {

        Bitmap image;
        String nom;
        String URLServeur = "http://158.69.192.249/sportswhere/image/ajouter.php";
        public TransfererImage(Bitmap image, String nom){
            this.image = image;
            this.nom = nom;
        }

        protected Void doInBackground(Void... params){

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String imageEncodee  = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> fichierAEnvoyer = new ArrayList<>();
            fichierAEnvoyer.add(new BasicNameValuePair("image", imageEncodee));
            fichierAEnvoyer.add (new BasicNameValuePair("nom", nom));

            HttpParams parametresRequeteHttp = getParametresRequeteHttp();

            HttpClient client = new DefaultHttpClient(parametresRequeteHttp);
            HttpPost post = new HttpPost(URLServeur);

            try {
                post.setEntity(new UrlEncodedFormEntity(fichierAEnvoyer));
                client.execute(post);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Image changée", Toast.LENGTH_SHORT).show();
            mettreImageTerrain();
            //mettreImageTerrain();
            //getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        }

    }

    protected void mettreImageTerrain(){
        Picasso.get().invalidate(imageURL);
        Picasso.get().load(imageURL).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE);
        /*
        try {
            URL lien = new URL("http://158.69.192.249/sportswhere/image/stockage/Terrain-"+terrain.getIdTerrain());
            HttpURLConnection connexion = (HttpURLConnection) lien.openConnection();
            connexion.setDoInput(true);
            connexion.connect();
            InputStream input = connexion.getInputStream();
            Bitmap photoBitMap = BitmapFactory.decodeStream(input);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            photoBitMap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            imageTerrain.setImageBitmap(photoBitMap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /*
    private class mettreImageTerrain extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL lien = new URL("http://158.69.192.249/sportswhere/image/stockage/Terrain-"+terrain.getIdTerrain());
                HttpURLConnection connexion = (HttpURLConnection) lien.openConnection();
                connexion.setDoInput(true);
                connexion.connect();
                InputStream input = connexion.getInputStream();
                Bitmap photoBitMap = BitmapFactory.decodeStream(input);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                photoBitMap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                imageTerrain.setImageBitmap(photoBitMap);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  null;
        }

        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
        }
    }
    */

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