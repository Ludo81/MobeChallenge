package level_page.gameplay;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class LevelThread extends Thread {

    private boolean running;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private LevelView levelView;

    public LevelThread(SurfaceHolder surfaceHolder, LevelView levelView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.levelView = levelView;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        while (running) {
            canvas = null;
            try {
                sleep(15);
                canvas = this.surfaceHolder.lockCanvas();
                synchronized(surfaceHolder) {
                    this.levelView.update();
                    this.levelView.draw(canvas);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
