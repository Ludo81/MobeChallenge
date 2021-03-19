package level_page.gameplay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import level_page.model.Balle;

public class LevelView extends SurfaceView implements SurfaceHolder.Callback {

    private static Balle balle = new Balle(750, 50, 25);

    private LevelThread levelThread;

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

    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
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
