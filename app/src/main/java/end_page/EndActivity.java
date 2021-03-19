package end_page;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.mobechallengeproject.R;

import level_page.gameplay.LevelGamePlayActivity;

public class EndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        MediaPlayer music = MediaPlayer.create(EndActivity.this, R.raw.end_music);
        music.start();
    }
}