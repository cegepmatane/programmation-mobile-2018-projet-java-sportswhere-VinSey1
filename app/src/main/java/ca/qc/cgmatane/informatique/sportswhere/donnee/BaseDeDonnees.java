package ca.qc.cgmatane.informatique.sportswhere.donnee;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseDeDonnees extends SQLiteOpenHelper {

    private static BaseDeDonnees instance = null;

    public static BaseDeDonnees getInstance(Context contexte)
    {
        if(null == instance) {
            instance = new BaseDeDonnees(contexte);

        }
        return instance;
    }

    public static BaseDeDonnees getInstance()
    {
        return instance;
    }


    public BaseDeDonnees(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public BaseDeDonnees(Context contexte) {
        super(contexte, "interet", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table interet(id_interet INTEGER PRIMARY KEY AUTOINCREMENT , id_evenement int, coche bit)";
        db.execSQL(CREATE_TABLE);
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        //String DELETE = "delete from interet where 1 = 1";
        //db.execSQL(DELETE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String CREER_TABLE = "create table interet(id_interet INTEGER PRIMARY KEY, id_evenement int, coche bit)";
        db.execSQL(CREER_TABLE);

    }
}