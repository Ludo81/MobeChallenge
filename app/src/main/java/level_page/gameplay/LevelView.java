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
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mobechallengeproject.R;

import level_page.model.Balle;

import end_page.EndActivity;
import level_page.model.Chrono;

public class LevelView extends SurfaceView implements SurfaceHolder.Callback {

    public static float luminosite;

    public static LevelGamePlayActivity activity;

    private static Balle balle = new Balle(25, 500, 500);

    private LevelThread levelThread;

    public static float[] gVector = new float[]{0, 0, 0};

    public static int xMax;
    public static int yMax;

    private Bitmap map_clean;
    private Bitmap map_high;
    private Bitmap map_defonce;
    private Bitmap current_map;

    public static boolean restart;

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
        this.levelThread = new LevelThread(getHolder(), this);
        this.etat = Etat.CLEAN;
        setFocusable(true);
        chronometreGlobal.start();
    }

    public void update() {
        double futurX = -1;
        double futurY = -1;
        switch (etat) {
            case CLEAN:
                futurX = balle.getCx() + gVector[1];
                futurY = balle.getCy() + gVector[0];
                break;
            case HIGH:
                futurX = balle.getCx() - gVector[1];
                futurY = balle.getCy() - gVector[0];
                break;
            case DEFONCE:
                futurX = balle.getCx() - gVector[1];
                futurY = balle.getCy() + gVector[0];
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

        if (futurX >= xMax - balle.getRadius() || futurX <= balle.getRadius() ) {
            futurX = balle.getCx();
        }
        if (futurY >= yMax - balle.getRadius() || futurY <= balle.getRadius()) {
            futurY = balle.getCy();
        }

        balle.setCx((int) futurX);
        balle.setCy((int) futurY);

        if(chronometreGlobal.getDuree() == 4) {
            etat = Etat.HIGH;
            current_map = map_high.isMutable() ? map_high : map_high.copy(Bitmap.Config.ARGB_8888, true);

        }

        if(chronometreGlobal.getDuree() == 8) {
            etat = Etat.DEFONCE;
            current_map = map_defonce.isMutable() ? map_defonce : map_defonce.copy(Bitmap.Config.ARGB_8888, true);
        }
    }

    private void restart(){
        Intent intent = new Intent(levelGamePlayActivity, EndActivity.class);
        levelGamePlayActivity.startActivity(intent);
    }

    @Override
    public void draw(Canvas canvas){
        if(canvas != null) {
            super.draw(canvas);
            Paint paint = new Paint();

            canvas.drawBitmap(current_map, null, new Rect(0, 0, xMax, yMax), paint);
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
            } else if(test == 3)
                restart();
        }
    }

}
