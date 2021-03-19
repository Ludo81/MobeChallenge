package level_page.gameplay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.mobechallengeproject.R;

import level_page.model.Balle;

import end_page.EndActivity;
import level_page.model.Chrono;

public class LevelView extends SurfaceView implements SurfaceHolder.Callback {
    private final static int xDep = 60;
    private final static int yDep = 600;

    public static float luminosite;

    public static LevelGamePlayActivity activity;

    private static Balle balle = new Balle(20, xDep, yDep);

    private LevelThread levelThread;

    public static float[] gVector = new float[]{0, 0, 0};

    public static int xMax;
    public static int yMax;

    private Bitmap map_clean;
    private Bitmap map_high;
    private Bitmap map_defonce;
    private static Bitmap current_map;
    private Bitmap bonus1;
    private Bitmap bonus2;
    private Bitmap malus;

    private boolean bonus1actived = true;
    private boolean bonus2actived = true;
    private boolean malusactived = true;

    public static boolean restart;

    private boolean gameover = false;

    private Etat etat;

    public static LevelGamePlayActivity levelGamePlayActivity;

    private Chrono chrono = new Chrono();
    private Chrono chronometreGlobal = new Chrono();

    private String textChrono;

    public boolean demarre = false;

    public LevelView(Context context) {
        super(context);
        getHolder().addCallback(this);
        this.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        map_clean = BitmapFactory.decodeResource(getResources(), R.drawable.map_state0);
        map_high = BitmapFactory.decodeResource(getResources(), R.drawable.map_state1);
        map_defonce = BitmapFactory.decodeResource(getResources(), R.drawable.map_state2);

        current_map = map_clean.isMutable() ? map_clean : map_clean.copy(Bitmap.Config.ARGB_8888, true);

        Bitmap seringue1 = BitmapFactory.decodeResource(getResources(), R.drawable.bonus1);
        Bitmap seringue2 = BitmapFactory.decodeResource(getResources(), R.drawable.bonus2);
        Bitmap joint = BitmapFactory.decodeResource(getResources(), R.drawable.malus);
        bonus1 = seringue1.isMutable() ? seringue1 : seringue1.copy(Bitmap.Config.ARGB_8888, true);
        bonus2 = seringue2.isMutable() ? seringue2 : seringue2.copy(Bitmap.Config.ARGB_8888, true);
        malus = joint.isMutable() ? joint : joint.copy(Bitmap.Config.ARGB_8888, true);
        this.levelThread = new LevelThread(getHolder(), this);
        this.etat = Etat.CLEAN;
        setFocusable(true);
        chronometreGlobal.start();
    }

    public void update() {
        float xVector = gVector[1];
        float yVector = gVector[0];
        double futurX = -1;
        double futurY = -1;
        switch (etat) {
            case CLEAN:
                futurX = balle.getCx() + xVector;
                futurY = balle.getCy() + yVector;
                break;
            case HIGH:
                futurX = balle.getCx() - xVector;
                futurY = balle.getCy() - yVector;
                break;
            case DEFONCE:
                futurX = balle.getCx() - xVector;
                futurY = balle.getCy() + yVector;
                break;
        }

        if(restart){
            if(!demarre){
                chrono.start();
                demarre = true;
            }
        } else {
            chrono.stop();
            demarre = false;
        }

        if (futurX >= xMax - balle.getRadius() || futurX <= balle.getRadius()) {
            restartGame();
        }
        if (futurY >= yMax - balle.getRadius() || futurY <= balle.getRadius()) {
            restartGame();
        }

        if(1200 < futurX && futurX < 1250 && 230 < futurY && futurY < 280){ //joint
            malusactived = false;
            switch (etat) {
                case CLEAN:
                    etat = Etat.HIGH;
                    current_map = map_high.isMutable() ? map_high : map_high.copy(Bitmap.Config.ARGB_8888, true);
                    break;
                case HIGH:
                    etat = Etat.DEFONCE;
                    current_map = map_defonce.isMutable() ? map_defonce : map_defonce.copy(Bitmap.Config.ARGB_8888, true);
                    break;
            }
        }
        if(1200 < futurX && futurX < 1250 && 720 < futurY && futurY < 770){ //bonus1
            bonus1actived = false;
            switch (etat) {
                case HIGH:
                    etat = Etat.CLEAN;
                    current_map = map_clean.isMutable() ? map_clean : map_clean.copy(Bitmap.Config.ARGB_8888, true);
                    break;
                case DEFONCE:
                    etat = Etat.HIGH;
                    current_map = map_high.isMutable() ? map_high : map_high.copy(Bitmap.Config.ARGB_8888, true);
                    break;
            }
        }
        if(40 < futurX && futurX < 90 && 720 < futurY && futurY < 770){ //bonus2
            bonus2actived = false;
            switch (etat) {
                case HIGH:
                    etat = Etat.CLEAN;
                    current_map = map_clean.isMutable() ? map_clean : map_clean.copy(Bitmap.Config.ARGB_8888, true);
                    break;
                case DEFONCE:
                    etat = Etat.HIGH;
                    current_map = map_high.isMutable() ? map_high : map_high.copy(Bitmap.Config.ARGB_8888, true);
                    break;
            }
        }
        if(isOnPixel((int) futurX, (int) futurY, Color.GREEN)){ //arrivé
            end();
        }
        if (isOnPixel((int) futurX, (int) futurY, Color.BLACK)){ //mur
            restartGame();
        }
        else {
            balle.setCx((int) futurX);
            balle.setCy((int) futurY);
        }



        if(chronometreGlobal.getDuree() == 30) {
            switch (etat) {
                case CLEAN:
                    etat = Etat.HIGH;
                    current_map = map_high.isMutable() ? map_high : map_high.copy(Bitmap.Config.ARGB_8888, true);
                    break;
                case HIGH:
                    etat = Etat.DEFONCE;
                    current_map = map_defonce.isMutable() ? map_defonce : map_defonce.copy(Bitmap.Config.ARGB_8888, true);
                    break;
            }
        }
    }

    private static boolean isOnPixel(int futureX, int futureY, int color) {
        int r = balle.getRadius();
        int xi = futureX - balle.getRadius();
        int yi = futureY - balle.getRadius();

        while (yi < futureY + balle.getRadius()) {
            while (xi < futureX + balle.getRadius()) {
                if (xi >= xMax || xi <= 0 || yi >= yMax || yi <= 0) {
                    return true;
                }
                if (isInCircle(xi, yi, futureX, futureY, r) && current_map.getPixel(xi * (current_map.getWidth()-1) / xMax, yi * (current_map.getHeight()-1) / yMax) == color) {
                    return true;
                }
                xi = xi + 1;
            }
            xi = futureX - balle.getRadius();
            yi = yi + 1;
        }
        return false;
    }

    private static boolean isInCircle(int x, int y, int cx, int cy, int r) {
        return (x - cx)*(x - cx) + (y - cy)*(y - cy) <= r*r;
    }

    private void end(){
        if(!gameover){
            String score = String.valueOf(chronometreGlobal.getDuree());
            chronometreGlobal.stop();
            Intent intent = new Intent(levelGamePlayActivity, EndActivity.class);
            intent.putExtra("SCORE", score);
            levelGamePlayActivity.startActivity(intent);
            gameover = true;
        }
    }

    public static void restartGame(){
        balle.setCx(xDep);
        balle.setCy(yDep);
    }

    @Override
    public void draw(Canvas canvas){
        if(canvas != null) {
            super.draw(canvas);
            Paint paint = new Paint();

            canvas.drawBitmap(current_map, null, new Rect(0, 0, xMax, yMax), paint);
            if(bonus1actived)
                canvas.drawBitmap(bonus1, null, new Rect(1200, 720, 1250, 770), paint);
            if(bonus2actived)
                canvas.drawBitmap(bonus2, null, new Rect(40, 720, 90, 770), paint);
            if(malusactived)
                canvas.drawBitmap(malus, null, new Rect(1200, 230, 1250, 280), paint);
            paint.setColor(getColorBall());
            canvas.drawCircle(balle.getCx(), balle.getCy(), balle.getRadius(), paint);

            drawRestart(canvas, paint);

            Paint paintChrono = new Paint();
            paintChrono.setTextSize(40);
            paintChrono.setColor(Color.RED);
            paintChrono.setFakeBoldText(true);
            textChrono = "Time : " + String.valueOf(chronometreGlobal.getDuree());
            canvas.drawText(textChrono, (int)((float)xMax*0.42), (int)((float)yMax*0.065), paintChrono);
        }
    }

    private int getColorBall(){
        int r = (int) (luminosite*30)%255;
        int g = (int) (luminosite*60)%255;
        int b = (int) (luminosite*90)%255;
        return Color.rgb(r, g, b);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        levelThread.setRunning(true);
        levelThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) { }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                levelThread.setRunning(false);
                levelThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    private void drawRestart(Canvas canvas, Paint paint){
        if(restart){
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            canvas.drawRect(50, 50, 300, 100, paint);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.RED);
            long test = chrono.getDuree();
            if (test == 0) {
                canvas.drawRect(50, 50, 100, 100, paint);
            } else if(test == 1){
                canvas.drawRect(50, 50, 200, 100, paint);
            } else if(test == 2){
                canvas.drawRect(50, 50, 300, 100, paint);
            } else if(test == 3) {
                chronometreGlobal = new Chrono();
                restartGame();
                chronometreGlobal.start();
                bonus1actived = true;
                bonus2actived = true;
                malusactived = true;
                etat = Etat.CLEAN;
                current_map = map_clean.isMutable() ? map_clean : map_clean.copy(Bitmap.Config.ARGB_8888, true);
            }
        }
    }

}
