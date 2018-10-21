package ca.qc.cgmatane.informatique.sportswhere.vue;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import ca.qc.cgmatane.informatique.sportswhere.R;
import ca.qc.cgmatane.informatique.sportswhere.modele.AlarmeReception;

import static android.support.v4.content.ContextCompat.getSystemService;

public class Alarme extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_alarme);

        Bundle parametres = this.getIntent().getExtras();

        long tempsAlarme = parametres.getLong("tempsAlarme");

        Intent intententionLancerAlarme = new Intent(this, AlarmeReception.class);

        intententionLancerAlarme.putExtra("nom", parametres.getString("nom"));
        intententionLancerAlarme.putExtra("description", parametres.getString("description"));

        PendingIntent attenteIntentionLancerAlarme = PendingIntent.getBroadcast(this, 0, intententionLancerAlarme, 0);

        AlarmManager gestionnaireAlarme = (AlarmManager) getSystemService(ALARM_SERVICE);

        Log.d("TestTemps",""+System.currentTimeMillis());

        gestionnaireAlarme.set(AlarmManager.RTC_WAKEUP, tempsAlarme, attenteIntentionLancerAlarme);

        Toast.makeText(this, "Alarme prévue pour le " + new SimpleDateFormat("dd/MM/yyyy").format(tempsAlarme),Toast.LENGTH_LONG).show();
        this.finish();
    }
}
