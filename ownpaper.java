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


public class ownpaper extends Fragment {

    TextView characterview;
    static TextView rama,sitha;
    String character;
    String[] characters={"Ramudu","Sitha","Lakshmana","Bharata","Shatrughna","Hanuman"};
    Button find_sitha,myButton,new_game;
    Communicator comm;
    LinearLayout ll;
    static Handler own_UpdateHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

     final View rootview=inflater.inflate(R.layout.fragment_ownpaper, container,false);
        characterview=(TextView)rootview.findViewById(R.id.characterview);
        rama=(TextView)rootview.findViewById(R.id.rama);
        sitha=(TextView)rootview.findViewById(R.id.sitha);
        characterview.setText(characters[Game.selected_character]);
        own_UpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg)
            {
                String recv_msg = msg.getData().getString("msg");
                String ch=msg.getData().getString("ss");
                Log.d("msg"," "+msg);
                if(ch.equals("ra"))
                {
                    display_rama();
                }
                if(ch.equals("reesult"))
                {
                    display_result(recv_msg);
                }
                if(ch.equals("new_game"))
                {
                    new_game = new Button(getActivity());
                    new_game.setText("New Game");
                    int i=11;
                    new_game.setId(i+1);
                    new_game.setOnClickListener(btnclick);
                    ll = (LinearLayout)rootview.findViewById(R.id.own_layout);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    ll.addView(new_game, lp);
                }

            }
        };
        if(Game.selected_character==0)
        {


            myButton = new Button(getActivity());
            myButton.setText("find sitha");
            int i=6;
            myButton.setId(i+1);
            myButton.setOnClickListener(btnclick);
            ll = (LinearLayout)rootview.findViewById(R.id.own_layout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(myButton, lp);


        }

        if(Game.id!=0) {
            String json = "{\"type\":\"is_allocating_finished\"}";
            new SocketHelper().sendtoleader(json);
        }                                                                        //checking is allocation is finished or not , to know rama and sitha
        else
        {
            SocketHelper sc =new SocketHelper();
                   if(sc.allocating_finished()==1)
                   {
                       if(Game.selected_character==0)
                       {
                           String json="{\"type\":\"ramais\",\"id\":"+Game.id+"}";
                           Game.ramais=Game.id;
                           sc.send_to_all(json);
                           display_rama();

                       }
                       if(Game.selected_character==1)
                       {
                           Game.sitha_is=Game.id;
                       }
                       String json = "{\"type\":\"all_done\"}";
                       sc.send_to_all(json);
                   }

        }

        comm=(Communicator) getActivity();
        return rootview;
    }
         void display_rama()
        {
            if(Game.selected_character!=0) {
                String ss = "RAMA is " + SocketHelper.client_names[Game.ramais];
                rama.setText(ss);
            }
            else
            {
                String ss = "Good Luck";
                rama.setText(ss);
            }

        }
         void display_result(String msg)
         {
             sitha.setText(msg);
         }

    View.OnClickListener btnclick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            switch(view.getId()){
                case 7:
                    comm.change_to_sitha();
                    break;
                case 12:
                    comm.change_to_swipe();
                default:
                    break;
            }

        }
    };

    }

