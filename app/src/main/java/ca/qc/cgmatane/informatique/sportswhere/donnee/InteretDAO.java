package ca.qc.cgmatane.informatique.sportswhere.donnee;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class InteretDAO {


    private static InteretDAO instance = null;
    private BaseDeDonnees accesseurBaseDeDonnees;

    public static InteretDAO getInstance(Context context) {
        if (instance == null) {

            instance = new InteretDAO(context);

        }
        return instance;
    }

    public InteretDAO(Context context) {
        this.accesseurBaseDeDonnees = BaseDeDonnees.getInstance(context);
    }

    public void ajouterInteret(Interet interet) {
        String AJOUTER_INTERET = "insert into interet(id_evenement, coche) VALUES('" + interet.getIdEvenement() + "','" + interet.isCoche() + "')";
        accesseurBaseDeDonnees.getWritableDatabase().execSQL(AJOUTER_INTERET);
    }

    public void modifierInteret(Interet interet) {
        int coche = 0;
        if(interet.isCoche()){
            coche = 1;
        }
        String MODIFIER_INTERET = "UPDATE interet SET (id_evenement, coche) =('" + interet.getIdEvenement() + "','" + coche + "')";
        Log.d("Test SQL ", MODIFIER_INTERET);
        accesseurBaseDeDonnees.getWritableDatabase().execSQL(MODIFIER_INTERET);
        chercherInteret(interet.getIdEvenement());
    }

    public Interet chercherInteret(int idEvenement) {
        String CHERCHER_INTERET = "SELECT * FROM interet WHERE id_evenement =" + idEvenement;

        Cursor curseur = accesseurBaseDeDonnees.getReadableDatabase().rawQuery(CHERCHER_INTERET, null);
        Interet interet;
        int indexIdEvenement = curseur.getColumnIndex("id_evenement");
        int indexCoche = curseur.getColumnIndex("coche");

        boolean coche = false;
        if (indexCoche == 1){
            coche = true;
        }

        interet = new Interet(indexIdEvenement, coche);

        Log.d("Test chercherInteret ", ""+interet.isCoche());
        return interet;
    }
}