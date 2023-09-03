package com.example.yojitha.game;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreateGameActivity extends AppCompatActivity {

    NsdHelper nsdhelper;
    static SocketHelper sockethelper;
    TextView peers_list,mynaame;
    static Handler UpdateHandler;
    Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        mynaame=(TextView)findViewById(R.id.naame);
        UpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg)
            {
                String recv_msg = msg.getData().getString("msg");
                String sam=peers_list.getText().toString()+"\n"+recv_msg;
                peers_list.setText(sam);
            }
        };

        sockethelper=new SocketHelper(1);
        nsdhelper=new NsdHelper(this);
        nsdhelper.initializeNsd();
        Log.d("port","hai "+sockethelper.c_getport());
        nsdhelper.registerService(sockethelper.c_getport());
        Log.d("hai","hello");
        peers_list=(TextView)findViewById(R.id.peers_list);
        start=(Button)findViewById(R.id.start_game);
        start.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View arg0)
            {
                SocketHelper.myname=mynaame.getText().toString();
                Intent inent = new Intent(CreateGameActivity.this,Game.class);
                startActivity(inent);
                    }
        });
    }

    void add_peer(String str)
    {
        peers_list.setText(peers_list.getText().toString()+"\n"+str);
    }

}
