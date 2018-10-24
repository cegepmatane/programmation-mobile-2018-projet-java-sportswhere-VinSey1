package ca.qc.cgmatane.informatique.sportswhere.vue;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;

import android.os.Build;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
import ca.qc.cgmatane.informatique.sportswhere.donnee.TerrainDAO;
import ca.qc.cgmatane.informatique.sportswhere.modele.Terrain;

public class Accueil extends AppCompatActivity implements OnMapReadyCallback {

    static final public int ACTIVITE_LISTE_TERRAINS = 1;
    static final public int ACTIVITE_LISTE_EVENEMENTS = 2;
    static final public int ACTIVITE_DETAILS_TERRAIN = 3;

    private GoogleMap carteTerrains;
    private TerrainDAO accesseurTerrain;
    private List<Terrain> listeTerrains;
    private Intent intentionNaviguerListeTerrains;
    private Intent intentionNaviguerListeEvenements;
    private Intent intentionNaviguerDetailsTerrain;

    LocationRequest requeteLocation;
    Location derniereLocation;
    Marker marqueurDerniereLocation;

    private FusedLocationProviderClient gestionnaireLocalisation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar barreAction = getSupportActionBar();
        barreAction.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_accueil);

        gestionnaireLocalisation = LocationServices.getFusedLocationProviderClient(this);

        MapFragment fragementCarte = (MapFragment) getFragmentManager().findFragmentById(R.id.carte_terrains);
        fragementCarte.getMapAsync(this);

        accesseurTerrain = TerrainDAO.getInstance();

        listeTerrains = accesseurTerrain.getListeTerrains();

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
    public void onPause() {
        super.onPause();


        if (gestionnaireLocalisation != null) {
            gestionnaireLocalisation.removeLocationUpdates(rappelLocation);
        }
    }


    @Override
    public void onMapReady(GoogleMap carte) {
        carteTerrains = carte;
        for(Terrain terrain : this.listeTerrains){
            carteTerrains.addMarker(new MarkerOptions().position(terrain.getPosition()).title(terrain.getTitre()).snippet(terrain.getDescription()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }

        carteTerrains.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                intentionNaviguerDetailsTerrain = new Intent(Accueil.this, DetailsTerrain.class);
                intentionNaviguerDetailsTerrain.putExtra("id_terrain", ""+accesseurTerrain.trouverTerrain(marker.getTitle()).getId_terrain());
                startActivityForResult(intentionNaviguerDetailsTerrain, ACTIVITE_DETAILS_TERRAIN);
            }
        });

        carteTerrains.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marqueur) {

                Context contexte = getApplicationContext();
                LinearLayout information = new LinearLayout(contexte);
                information.setOrientation(LinearLayout.VERTICAL);

                TextView titre = new TextView(contexte);
                titre.setTextColor(Color.BLACK);
                titre.setGravity(Gravity.CENTER);
                titre.setTypeface(null, Typeface.BOLD);
                titre.setText(marqueur.getTitle());

                TextView description = new TextView(contexte);
                description.setTextColor(Color.GRAY);
                description.setText(marqueur.getSnippet());

                information.addView(titre);
                information.addView(description);

                return information;
            }
        });

        requeteLocation = new LocationRequest();
        requeteLocation.setInterval(120000); // two minute interval
        requeteLocation.setFastestInterval(120000);
        requeteLocation.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                gestionnaireLocalisation.requestLocationUpdates(requeteLocation, rappelLocation, Looper.myLooper());
                carteTerrains.setMyLocationEnabled(true);
            } else {
                checkLocationPermission();
            }
        }
        else {
            gestionnaireLocalisation.requestLocationUpdates(requeteLocation, rappelLocation, Looper.myLooper());
            carteTerrains.setMyLocationEnabled(true);
        }
    }

    LocationCallback rappelLocation = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult resultatLocation) {
            List<Location> listeLocation = resultatLocation.getLocations();
            if (listeLocation.size() > 0) {

                Location location = listeLocation.get(listeLocation.size() - 1);

                derniereLocation = location;
                if (marqueurDerniereLocation != null) {
                    marqueurDerniereLocation.remove();
                }

                LatLng position = new LatLng(location.getLatitude(), location.getLongitude());

                carteTerrains.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13));
            }
        }
    };

    public static final int PERMISSIONS_REQUETE_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Permissions de location nécessaires")
                        .setMessage("Cette application nécessite d'accéder à votre localisation pour son fonctionnement")
                        .setPositiveButton("Désolé", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface interfaceDialogue, int i) {

                                ActivityCompat.requestPermissions(Accueil.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSIONS_REQUETE_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUETE_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int codeDeRequete,
                                           String permissions[], int[] resultats) {
        switch (codeDeRequete) {
            case PERMISSIONS_REQUETE_LOCATION: {
                if (resultats.length > 0
                        && resultats[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        gestionnaireLocalisation.requestLocationUpdates(requeteLocation, rappelLocation, Looper.myLooper());
                        carteTerrains.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Permission refusée", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}