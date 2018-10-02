package ca.qc.cgmatane.informatique.sportswhere.vue;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
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

    private GoogleMap carteTerrains;
    private TerrainDAO accesseurTerrain;
    private EvenementDAO accesseurEvenement;
    private List<Terrain> listeTerrains;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_accueil);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        accesseurEvenement = EvenementDAO.getInstance();
        accesseurTerrain = TerrainDAO.getInstance();

        listeTerrains = accesseurTerrain.listerTerrains();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        carteTerrains = googleMap;

        LatLng emplacementDeBase = new LatLng( 48.8526, -67.518);
        carteTerrains.moveCamera(CameraUpdateFactory.newLatLng(emplacementDeBase));
        carteTerrains.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);

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