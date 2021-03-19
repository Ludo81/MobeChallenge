package level_page.gameplay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import end_page.EndActivity;
import level_page.model.Chrono;

public class LevelView extends SurfaceView implements SurfaceHolder.Callback {

    private LevelThread levelThread;

    public static boolean restart;

    public static LevelGamePlayActivity levelGamePlayActivity;

    private Chrono chrono = new Chrono();
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
        this.levelThread = new LevelThread(getHolder(), this);
        setFocusable(true);
    }

    public void update() {
        if(restart){
            if(!demarre){
                chrono.start();
                demarre = true;
            }

        } else {
            chrono.stop();
            demarre = false;
        }
    }

    private void restart(){
        Intent intent = new Intent(levelGamePlayActivity, EndActivity.class);
        levelGamePlayActivity.startActivity(intent);
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        drawRestart(canvas);
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

    private void drawRestart(Canvas canvas){
        Paint paint = new Paint();

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
