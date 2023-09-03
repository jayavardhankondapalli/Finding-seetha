package com.example.yojitha.game;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class last extends Fragment {



    Communicator comm;
    TextView resulte,sccores;
    Button new_game;
    LinearLayout ll;
    int k=1;
    static Handler  last_UpdateHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview=inflater.inflate(R.layout.fragment_last, container, false);
        last_UpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg)
            {
                String recv_msg = msg.getData().getString("msg");
                Log.d("msg"," "+msg);
                if(recv_msg.equals("new_game"))
                {
                    new_game = new Button(getActivity());
                    new_game.setText("New Game");
                    new_game.setId(k + 1);                                // new game button
                    Log.d("bbbbb","at handler ");
                    new_game.setOnClickListener(xclick);
                    ll = (LinearLayout) rootview.findViewById(R.id.last_layout);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    ll.addView(new_game, lp);
                }
                if(recv_msg.equals("update_score"))
                {
                    String sam = "";
                    for (int l = 0; l <= SocketHelper.client_count; l++)
                    {                                                                                  //displaying score
                        Log.d("at display score", "chudam");
                        sam = sam.concat(SocketHelper.client_names[l] + "  " + SocketHelper.scores[l] + "\n");
                    }
                    sccores.setText(sam);
                }
            }
        };

        resulte=(TextView)rootview.findViewById(R.id.result);
        sccores=(TextView)rootview.findViewById(R.id.sccores);
        Log.d("game result",""+Game.result);
        if(Game.result.equals("correct"))
        {
            resulte.setText("CORRECT\nRamudu is "+SocketHelper.client_names[Game.ramais]+"\nSitha is "+SocketHelper.client_names[Game.real_sitha]+"");
        }
        if(Game.result.equals("wrong"))
        {
            resulte.setText("WRONG\nRamudu is "+SocketHelper.client_names[Game.ramais]+"\nSitha is "+SocketHelper.client_names[Game.real_sitha]+"\n Ramudu selected "+Game.selected_sitha_string);
        }
 /*       SocketHelper sock=new SocketHelper();
        for(int l=0;l<=SocketHelper.client_count;l++)
        {
            Log.d("at display score","chudam");
            sccores.setText(sccores.getText().toString().concat(sock.client_names[l]+"  "+sock.scores[l]+"\n"));
        }



        if(SocketHelper.next_chance==0 && Game.id==0)
        {
            Log.d("at leader newgame","cgeckkkk");
            new_game = new Button(getActivity());
            new_game.setText("New Game");
            int i = 11;
            new_game.setId(k + 1);
            new_game.setOnClickListener(xclick);
            ll = (LinearLayout) rootview.findViewById(R.id.last_layout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(new_game, lp);
            sock.update_next_chance();
        }
        comm=(Communicator) getActivity();
        SocketHelper.safety=2;*/

        if(Game.id!=0)
        {
            String json = "{\"type\":\"last_came\",\"id\":"+Game.id+"}";
            new SocketHelper().sendtoleader(json);
        }
        else
        {
            String sam = "";
            for (int l = 0; l <= SocketHelper.client_count; l++)
            {                                                                                  //displaying score
                Log.d("at display score", "chudam");
                sam = sam.concat(SocketHelper.client_names[l] + "  " + SocketHelper.scores[l] + "\n");
            }
            sccores.setText(sam);

            if(new SocketHelper().next_chance_is()==0 && Game.id==0)
            {
                Log.d("display","next chance is "+new SocketHelper().next_chance_is());
                new_game = new Button(getActivity());                new_game.setText("New Game");

                new_game.setId(k + 1);                                // new game button
                Log.d("at last","for new game");
                new_game.setOnClickListener(xclick);
                ll = (LinearLayout) rootview.findViewById(R.id.last_layout);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll.addView(new_game, lp);
                new SocketHelper().update_next_chance();
            }

        }
        comm=(Communicator) getActivity();
        return rootview;
    }

    View.OnClickListener xclick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            switch(view.getId()){
                case 2:
                    comm.change_to_swipe();
                    break;
                default:
                    break;
            }

        }
    };
}
