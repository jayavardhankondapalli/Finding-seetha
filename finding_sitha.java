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

public class finding_sitha extends Fragment {

    Button[] butt=new Button[10];
    Button new_game;
    TextView ree;
    Communicator comm;
    LinearLayout ll;
    static Handler finding_sitha_UpdateHandler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview=inflater.inflate(R.layout.fragment_finding_sitha, container, false);
        finding_sitha_UpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg)
            {
                String recv_msg = msg.getData().getString("msg");
                String ch = msg.getData().getString("ss");
                Log.d("msg"," "+msg);


                if(ch.equals("reesult"))
                {
                    ree.setText(recv_msg);
                }
                if(ch.equals("new_game")) {
                    new_game = new Button(getActivity());
                    new_game.setText("New Game");
                    int i = 11;
                    new_game.setId(i + 1);
                    new_game.setOnClickListener(click);
                    ll = (LinearLayout) rootview.findViewById(R.id.find_sitha);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    ll.addView(new_game, lp);
                }
            }
        };
        ree=(TextView)rootview.findViewById(R.id.reesult);
        for(int i=0;i<SocketHelper.client_count+1;i++)
        {
            if(i==Game.id)
            {
                continue;
            }
            butt[i] = new Button(getActivity());
            butt[i].setText(SocketHelper.client_names[i]);
            butt[i].setId(i + 1);
            butt[i].setOnClickListener(click);
            ll = (LinearLayout) rootview.findViewById(R.id.find_sitha);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(butt[i], lp);
        }
        comm=(Communicator) getActivity();
        return rootview;
    }

    public void result_checking(int id)
    {
        Log.d("win or fut","Id "+id);
        if(Game.id!=0)
        {
            String json = "{\"type\":\"this_is_sitha\",\"id\":" + id + "}";
            new SocketHelper().sendtoleader(json);

        }
        else
        {
            String json12="";
            if(id==Game.sitha_is)
            {
                json12="{\"type\":\"game_result\",\"result\":\"correct\",\"re_si\":"+Game.sitha_is+"}";
               // new SocketHelper().send_to_all(json);
             //   String p="RAMA SELECTED SITHA CORRECTLY RAMA IS"+SocketHelper.client_names[Game.ramais]+"\nSITHA IS "+SocketHelper.client_names[Game.sitha_is]+"}";
                Game.result="correct";
                Game.real_sitha=Game.sitha_is;
               // ree.setText(p);
                SocketHelper.scores[0]=SocketHelper.scores[0]+1000;
            }
            else
            {
                 json12="{\"type\":\"game_result\",\"result\":\"wrong\",\"re_si\":"+Game.sitha_is+",\"se_si\":\""+SocketHelper.client_names[id]+"\"}";
             //   new SocketHelper().send_to_all(json);
              //  String p="RAMA SELECTED SITHA WRONGLY RAMA IS "+SocketHelper.client_names[Game.ramais]+"\nSITHA IS "+SocketHelper.client_names[Game.sitha_is]+"\n BUT RAMA SELECTED "+SocketHelper.client_names[id];
                Game.result="wrong";
                Game.real_sitha=Game.sitha_is;
                Game.selected_sitha_string=SocketHelper.client_names[id];
                Game.selected_sitha=id;
               // ree.setText(p);
                SocketHelper.scores[Game.sitha_is]=SocketHelper.scores[Game.sitha_is]+1000;
            }
            SocketHelper sock=new SocketHelper();
            sock.update_score();
            String sco_sen ="{\"type\":\"score\"";
            for(int i=0;i<=SocketHelper.client_count;i++)
            {
                sco_sen=sco_sen.concat(",\""+i+"\":"+sock.scores[i]);
            }
            sco_sen=sco_sen.concat("}");
        //    sock.send_to_all(sco_sen);
            sock.send_to_all(json12);
            sock.update_all();
  /*          if(sock.next_chance_is()==0)
            {
               if(Game.ramais==Game.id)
                {
                    sock.update_all();
                    Bundle messageBundl = new Bundle();
                    messageBundl.putString("ss","new_game");
                    messageBundl.putString("msg","new_game");
                    Message messag = new Message();
                    messag.setData(messageBundl);
                    finding_sitha.finding_sitha_UpdateHandler.sendMessage(messag);
                    Log.d("after","game.cc");
                }
                else
                {
                    sock.update_all();
                    Bundle messageBundl = new Bundle();
                    messageBundl.putString("msg","");
                    messageBundl.putString("ss","new_game");
                    Message messag = new Message();
                    messag.setData(messageBundl);
                    ownpaper.own_UpdateHandler.sendMessage(messag);
                    Log.d("after","game.cc");
                }
              //  sock.update_next_chance();
            }
            else
            {
                Log.d("calloing new game","finding_sitha");
                sock.new_game();
            }*/


        }
        Log.d("at kindhaa","finding_sitha");
        if(Game.id==0)
        {
            comm.change_to_last();
        }
    }



    View.OnClickListener click = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            switch(view.getId()){
                case 1:
                    result_checking(0);
                    break;
                case 2:
                    result_checking(1);
                    break;
                case 3:
                    result_checking(2);
                    break;
                case 4:
                    result_checking(3);
                    break;
                case 5:
                    result_checking(4);
                    break;
                case 6:
                    result_checking(5);
                    break;
                case 7:
                    result_checking(6);
                    break;
                case 8:
                    result_checking(7);
                    break;
                case 9:
                    result_checking(8);
                    break;
                case 12:
                    comm.change_to_swipe();
                    break;
                default:
                    break;
            }

        }
    };

}
