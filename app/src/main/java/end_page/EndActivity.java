package end_page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mobechallengeproject.R;

import level_page.gameplay.LevelGamePlayActivity;
import start_page.StartActivity;


public class EndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        final TextView score = findViewById(R.id.score_text);
        Intent intent = getIntent();
        String score_intent = intent.getStringExtra("score");
        score.append("score_intent");

        final Button restart_button = findViewById(R.id.restart_button);
        restart_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EndActivity.this, LevelGamePlayActivity.class);
                EndActivity.this.startActivity(intent);
            }
        });

        final Button menu_button = findViewById(R.id.menu_button);
        menu_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EndActivity.this, StartActivity.class);
                EndActivity.this.startActivity(intent);
            }
        });

        MediaPlayer music = MediaPlayer.create(EndActivity.this, R.raw.end_music);
        music.start();

    }
}