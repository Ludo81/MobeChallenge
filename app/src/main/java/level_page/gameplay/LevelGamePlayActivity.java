package level_page.gameplay;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import end_page.EndActivity;

import com.example.mobechallengeproject.R;

public class LevelGamePlayActivity extends Activity implements SensorEventListener, View.OnTouchListener{
    private SensorManager sensorManager;
    private Sensor accelSensor;
    private Sensor lightSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        LevelView.yMax = height+80;
        LevelView.xMax = width;

        LevelView.activity = this;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        LevelView levelView = new LevelView(this);
        levelView.setOnTouchListener(this);
        setContentView(levelView);

        MediaPlayer music = MediaPlayer.create(LevelGamePlayActivity.this, R.raw.level_music);
        music.start();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                LevelView.gVector = sensorEvent.values;
                break;
            case Sensor.TYPE_LIGHT:
                LevelView.luminosite = sensorEvent.values[0];
                break;
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

}