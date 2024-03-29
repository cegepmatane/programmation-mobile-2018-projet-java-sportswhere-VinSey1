package ca.qc.cgmatane.informatique.sportswhere.modele;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import ca.qc.cgmatane.informatique.sportswhere.R;

public class AlarmeReception extends BroadcastReceiver {

    protected Context contexte;
    protected Intent intention;

    @Override
    public void onReceive(Context contexte, Intent intention) {

        this.contexte = contexte;
        this.intention = intention;

        NotificationManager gestionnaireNotification = (NotificationManager) contexte.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "Événements",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notifications d'événements");
            gestionnaireNotification.createNotificationChannel(channel);
        }

        Bundle parametres = intention.getExtras();
        String nom = parametres.get("nom").toString();
        String description = parametres.get("description").toString();

        NotificationCompat.Builder constructeurNotification = new NotificationCompat.Builder(contexte, "default")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(nom)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        gestionnaireNotification.notify(1, constructeurNotification.build());
    }
}