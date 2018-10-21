package ca.qc.cgmatane.informatique.sportswhere.action;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class EcouteurSurBalayement implements OnTouchListener {

    private final GestureDetector detecteurDeGeste;

    public EcouteurSurBalayement(Context contexte){
        detecteurDeGeste = new GestureDetector(contexte, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent evenement) {
        return detecteurDeGeste.onTouchEvent(evenement);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SEUIL_BALAYAGE = 100;
        private static final int SEUIL_VITESSE_BALAYAGE = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent position1, MotionEvent position2, float vitesseEnX, float vitesseEnY) {
            boolean resultat = false;
            try {
                float differenceEnY = position2.getY() - position1.getY();
                float differenceEnX = position2.getX() - position1.getX();
                if (Math.abs(differenceEnX) > Math.abs(differenceEnY)) {
                    if (Math.abs(differenceEnX) > SEUIL_BALAYAGE && Math.abs(vitesseEnX) > SEUIL_VITESSE_BALAYAGE) {
                        if (differenceEnX > 0) {
                            balayageGauche();
                        } else {
                            balayageDroit();
                        }
                        resultat = true;
                    }
                }
                else if (Math.abs(differenceEnY) > SEUIL_BALAYAGE && Math.abs(vitesseEnY) > SEUIL_VITESSE_BALAYAGE) {
                    if (differenceEnY > 0) {
                        balayageBas();
                    } else {
                        balayageHaut();
                    }
                    resultat = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return resultat;
        }
    }

    public void balayageGauche() {
    }

    public void balayageDroit() {
    }

    public void balayageHaut() {
    }

    public void balayageBas() {
    }

}