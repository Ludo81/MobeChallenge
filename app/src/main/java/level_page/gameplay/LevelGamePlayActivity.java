package level_page.gameplay;


import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import end_page.EndActivity;
import start_page.StartActivity;

public class LevelGamePlayActivity extends Activity implements SensorEventListener, View.OnTouchListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LevelView levelView = new LevelView(this);
        levelView.setOnTouchListener(this);
        setContentView(levelView);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int count = event.getPointerCount();
        if (count == 2) {
            Intent intent = new Intent(LevelGamePlayActivity.this, EndActivity.class);
            LevelGamePlayActivity.this.startActivity(intent);
        }
        return true;
    }
}