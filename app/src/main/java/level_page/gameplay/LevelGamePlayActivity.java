package level_page.gameplay;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.mobechallengeproject.R;

public class LevelGamePlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_game_play);

        MediaPlayer ring= MediaPlayer.create(LevelGamePlayActivity.this, R.raw.century_fox_flute);
        ring.start();
    }
}