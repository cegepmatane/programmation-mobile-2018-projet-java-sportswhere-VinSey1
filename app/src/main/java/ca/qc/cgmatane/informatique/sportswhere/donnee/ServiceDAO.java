package ca.qc.cgmatane.informatique.sportswhere.donnee;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class ServiceDAO extends AsyncTask<String, Void, String> {

    String xml;

    @Override
    protected String doInBackground(String... parametres) {
        String stringUrlXML = parametres[0];
        String delimiteur = parametres[1];

        xml = recupererXML(stringUrlXML, delimiteur);

        if(null == xml)
            return null;
        return xml;
    }

    private String recupererXML(String stringUrlXML, String delimiteur){
        try{

            URL urlXML = new URL(stringUrlXML);

            InputStream flux = urlXML.openConnection().getInputStream();
            Scanner lecteur = new Scanner(flux).useDelimiter(delimiteur);
            xml = lecteur.next() + delimiteur;
        } catch (IOException e) {
            Log.d("ServiceDAO/IOException", ""+e.getMessage());
        }
        return xml;
    }
}
