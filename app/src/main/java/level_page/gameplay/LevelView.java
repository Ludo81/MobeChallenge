package level_page.gameplay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class LevelView extends SurfaceView implements SurfaceHolder.Callback {

    public static float luminosite;

    public static LevelGamePlayActivity activity;

    private static Balle balle = new Balle(25, 500, 500);

    private LevelThread levelThread;

    public static int xMax;
    public static int yMax;

    private static Bitmap current_map;

    public LevelView(Context context) {
        super(context);
        getHolder().addCallback(this);
        this.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        Bitmap map = BitmapFactory.decodeResource(getResources(), R.drawable.map_state1);
        current_map = map.isMutable() ? map : map.copy(Bitmap.Config.ARGB_8888, true);
        this.levelThread = new LevelThread(getHolder(), this);
        setFocusable(true);
    }

    public void update() {

    }

    @Override
    public void draw(Canvas canvas){
        if(canvas != null) {
            super.draw(canvas);
            Paint paint = new Paint();
            canvas.drawBitmap(current_map, null, new Rect(0, 0, xMax, yMax), paint);

            drawBall(canvas, paint);
            paint.setColor(Color.WHITE);
            canvas.drawCircle(balle.getCx(), balle.getCy(), balle.getRadius(), paint);
        }
    }

    private void drawBall(Canvas canvas, Paint paint){

    }

    private int getColorBall(){
        int r = (int) ((luminosite*30)%255);
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
}
