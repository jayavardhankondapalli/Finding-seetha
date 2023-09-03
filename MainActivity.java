package com.example.yojitha.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button create_game,join_game;
    NsdHelper nsdhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nsdhelper=new NsdHelper(this);
        create_game=(Button)findViewById(R.id.create_game);
        join_game=(Button)findViewById(R.id.join_game);

        create_game.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View arg0)
            {
                Intent inent = new Intent(MainActivity.this, CreateGameActivity.class);
                startActivity(inent);
            }
        });
        join_game.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View arg0)
            {
                Intent inent = new Intent(MainActivity.this, JoinGame.class);
                startActivity(inent);
            }
        });

    }
}
