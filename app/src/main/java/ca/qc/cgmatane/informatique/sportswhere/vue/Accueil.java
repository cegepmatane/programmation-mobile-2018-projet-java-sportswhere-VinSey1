package ca.qc.cgmatane.informatique.sportswhere.vue;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ca.qc.cgmatane.informatique.sportswhere.R;
import ca.qc.cgmatane.informatique.sportswhere.donnee.EvenementDAO;
import ca.qc.cgmatane.informatique.sportswhere.donnee.TerrainDAO;
import ca.qc.cgmatane.informatique.sportswhere.modele.Terrain;

public class Accueil extends AppCompatActivity implements OnMapReadyCallback {

    static final public int ACTIVITE_LISTE_TERRAINS = 1;
    static final public int ACTIVITE_LISTE_EVENEMENTS = 2;

    private GoogleMap carteTerrains;
    private TerrainDAO accesseurTerrain;
    private EvenementDAO accesseurEvenement;
    private List<Terrain> listeTerrains;
    private Intent intentionNaviguerListeTerrains;
    private Intent intentionNaviguerListeEvenements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_accueil);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.carte_terrains);
        mapFragment.getMapAsync(this);

        accesseurEvenement = EvenementDAO.getInstance();
        accesseurTerrain = TerrainDAO.getInstance();

        listeTerrains = accesseurTerrain.listerTerrains();

        intentionNaviguerListeTerrains = new Intent(this, ListeTerrains.class);

        Button actionNaviguerListeTerrains = (Button) findViewById(R.id.action_naviguer_liste_terrains);

        actionNaviguerListeTerrains.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View arg0) {
                    startActivityForResult(intentionNaviguerListeTerrains, ACTIVITE_LISTE_TERRAINS);
                }
            }
        );

        intentionNaviguerListeEvenements = new Intent(this, ListeEvenements.class);

        Button actionNaviguerListeEvenements = (Button) findViewById(R.id.action_naviguer_liste_evenements);

        actionNaviguerListeEvenements.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View arg0) {
                    startActivityForResult(intentionNaviguerListeEvenements, ACTIVITE_LISTE_EVENEMENTS);
                }
            }
        );

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        carteTerrains = googleMap;
        float zoom = 13.0f;
        /*carteTerrains.animateCamera(CameraUpdateFactory.zoomTo(13), 10, null);*/
        LatLng emplacementDeBase = new LatLng( 48.8526, -67.518);
        carteTerrains.moveCamera(CameraUpdateFactory.newLatLngZoom(emplacementDeBase, zoom));

        for(Terrain terrain : this.listeTerrains){
            carteTerrains.addMarker(new MarkerOptions().position(terrain.getPosition()).title(terrain.getTitre()).snippet(terrain.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }

        carteTerrains.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context mContext = getApplicationContext();
                LinearLayout info = new LinearLayout(mContext);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(mContext);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(mContext);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
    }
}