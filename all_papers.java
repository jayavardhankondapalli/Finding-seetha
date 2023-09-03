package com.example.yojitha.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;


public class all_papers extends Fragment {

    String[] characters={"ram","sitha","lakshman","baharat","anjanayalu"};
    String json="";
    int[] button_values=new int[10];
    Button oneb,twob,threeb,fourb,fiveb;
    Communicator comm;
    RelativeLayout simpleRelativeLayout;
    Button[] butt=new Button[10];
    int[] roles;
    int h,w,by=1;
    static Handler allpapers_UpdateHandler;
     int select_cc;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
           Log.d("at all_papers "," on create view");

        View rootview=inflater.inflate(R.layout.fragment_all_papers, container, false);
        allpapers_UpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg)
            {
                String recv_msg = msg.getData().getString("msg");
                Log.d("msg"," "+msg);
                if(recv_msg.equals("cc"))
                {
                    butt[select_cc-1].setBackgroundColor(Color.parseColor("#90a098"));
                }


            }
        };

        comm=(Communicator) getActivity();
        simpleRelativeLayout = (RelativeLayout) rootview.findViewById(R.id.xml_layout);


        roles=new int[SocketHelper.client_count+1];
        try {
            JSONObject mess = (new JSONObject(Game.all_papers_string));
            String type = mess.getString("type");
            for (int i = 0; i <= SocketHelper.client_count; i++) {
                roles[i] =mess.getInt(""+i);
            }
        }
        catch(Exception e)
        {
            Log.e("Error is"," ",e);
        }

        if(SocketHelper.client_count+1>2)by=2;
        if(SocketHelper.client_count+1>4)by=3;
        if(SocketHelper.client_count+1>6)by=4;

        for(int i=0;i<SocketHelper.client_count+1;i++)
        {
            RelativeLayout.LayoutParams buttonParam = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            butt[i] = new Button(getActivity());
            butt[i].setText(""+i);
            butt[i].setId(i + 1);

            butt[i].setOnClickListener(alclick);
            buttonParam.leftMargin = 20;
            buttonParam.topMargin = 20;
            h=((Game.height-58)/by);
            w=(Game.width/2)-40;
            buttonParam.height=h-50;
            buttonParam.width=w;

            if(i==1 || i==3 ||i==5)
            {
                buttonParam.leftMargin =w+60;
            }
            if(i<=1)
            {
                buttonParam.bottomMargin =20;
            }
            if(i>1 && i<4)
            {
                buttonParam.topMargin =h;
                buttonParam.bottomMargin =20;
            }
            if(i>3 && i<6)
            {
                buttonParam.topMargin =2*h-20;
                buttonParam.bottomMargin =20;
            }
            if(i>5 && i<8 )
            {
                buttonParam.topMargin =3*h-20;
                buttonParam.bottomMargin =20;
            }
            butt[i].setLayoutParams(buttonParam);
            butt[i].setTextColor(Color.WHITE);
            butt[i].setBackgroundColor(Color.parseColor("#95C03C"));
            simpleRelativeLayout.addView(butt[i]);




        }


        return rootview;
    }

    public void selection(int selected)
    {
       String json="{\"type\":\"check\",\"id\":"+Game.id+",\"selected\":"+selected+"}";
            if(Game.id!=0)
            {
                  new SocketHelper().sendtoleader(json);
                Log.d("selected"," send");
            }
            else
            {
                if(SocketHelper.check_available[selected]==0)
                {
                    SocketHelper.check_available[selected]=1;
                    Game.selected_character=selected;
                    SocketHelper.selected_roles[0]=selected;
                    comm.change_to_ownpapers();
                }
                else
                {
                    Log.d("id is",""+select_cc);
                    butt[select_cc-1].setBackgroundColor(Color.parseColor("#90a098"));
                    Log.d("id is",""+select_cc);
                    Log.d("check","already selected");
                }
            }
    }

    View.OnClickListener alclick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            switch(view.getId()){
                case 1:
                    Log.d("haii","knk");
                    select_cc=1;
                    selection(roles[0]);
                    break;
                case 2:
                    select_cc=2;
                    selection(roles[1]);

                    break;
                case 3:
                    select_cc=3;
                    selection(roles[2]);

                    break;
                case 4:
                    select_cc=4;
                    selection(roles[3]);

                    break;
                case 5:
                    select_cc=5;
                    selection(roles[4]);

                    break;
                case 6:
                    select_cc=6;
                    selection(roles[5]);

                    break;
                case 7:
                    select_cc=7;
                    selection(roles[6]);

                    break;
                case 8:
                    select_cc=8;
                    selection(roles[7]);

                    break;
                case 9:
                    select_cc=9;
                    selection(roles[8]);

                    break;
                case 10:
                    select_cc=10;
                    selection(roles[9]);

                default:
                    break;
            }

        }
    };
}
