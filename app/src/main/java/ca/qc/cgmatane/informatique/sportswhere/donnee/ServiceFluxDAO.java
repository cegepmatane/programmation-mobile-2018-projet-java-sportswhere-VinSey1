package ca.qc.cgmatane.informatique.sportswhere.donnee;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class ServiceFluxDAO extends AsyncTask<HttpURLConnection, Void, InputStream> {

    @Override
    protected InputStream doInBackground(HttpURLConnection... httpURLConnections) {
        HttpURLConnection connexion = httpURLConnections[0];
        InputStream lecteur = null;

        try {
            lecteur = connexion.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lecteur;
    }
}