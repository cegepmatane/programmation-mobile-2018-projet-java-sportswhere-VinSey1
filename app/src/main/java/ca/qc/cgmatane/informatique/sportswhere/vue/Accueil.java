package ca.qc.cgmatane.informatique.sportswhere.vue;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import ca.qc.cgmatane.informatique.sportswhere.R;

public class Accueil extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_accueil);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng matane = new LatLng( 48.8526, -67.518);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(matane));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);


        LatLng terrainDeLaPoly = new LatLng(48.839393,-67.504567);
        mMap.addMarker(new MarkerOptions().position(terrainDeLaPoly).title("Terrain de la Polyvalente de Matane").snippet("1 terrain en vrai gazon").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        PolygonOptions terrainPolyOption = new PolygonOptions()
                .add(new LatLng(48.839725, -67.505143),
                        new LatLng(48.839283, -67.505416),
                        new LatLng(48.838959, -67.504174),
                        new LatLng(48.839431, -67.503941));

        Polygon footPoly = mMap.addPolygon(terrainPolyOption.fillColor(Color.GREEN));
        LatLng terrainDuCegep = new LatLng(48.840856, -67.495824);
        mMap.addMarker(new MarkerOptions().position(terrainDuCegep).title("Terrain du CEGEP de Matane").snippet("2 terrains en vrai gazon"+"\n"+"1 terrain synthétique").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        PolygonOptions terrainCegepOption1 = new PolygonOptions()
                .add(new LatLng(48.842417,  -67.494738),
                        new LatLng(48.841966, -67.495110),
                        new LatLng(48.841531, -67.493940),
                        new LatLng(48.841997, -67.493552));

        Polygon footCegep1 = mMap.addPolygon(terrainCegepOption1.fillColor(Color.GREEN));
        footCegep1.setClickable(true);

        PolygonOptions terrainCegepOption2 = new PolygonOptions()
                .add(new LatLng(48.840203,   -67.496060),
                        new LatLng(48.840442, -67.495909),
                        new LatLng(48.840192,   -67.494790),
                        new LatLng(48.839886, -67.494838));

        Polygon footCegep2 = mMap.addPolygon(terrainCegepOption2.fillColor(Color.GREEN));
        footCegep2.setClickable(true);
        PolygonOptions terrainCegepOption3 = new PolygonOptions()
                .add(new LatLng(48.841361,  -67.494814),
                        new LatLng(48.841142, -67.494961),
                        new LatLng(48.840964,  -67.494275),
                        new LatLng(48.841181, -67.494153));

        Polygon footCegep3 = mMap.addPolygon(terrainCegepOption3.fillColor(Color.BLUE));
        footCegep3.setClickable(true);


        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

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
