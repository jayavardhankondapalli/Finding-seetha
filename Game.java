package com.example.yojitha.game;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

public class Game extends AppCompatActivity implements Communicator{

    private Toolbar toolbar;
    static int id=90;
    int[] roles_array={0,1,2};
    String[] characters={"ram","sitha","lakshman","baharat","anjanayalu"};
    static String all_papers_string;
    static int selected_character=0;
    static int ramais;
    static int sitha_is;
    static Handler mUpdateHandler;

    static String result;
    static int selected_sitha;
    static String selected_sitha_string;
    static int real_sitha;
    TextView tx;
    Context context;
    Button swipe;
    LinearLayout ll;
    SocketHelper kk;
    static int height,width;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;

        mUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg)
            {
                String recv_msg = msg.getData().getString("msg");
                Log.d("msg"," "+msg);
                if(recv_msg.equals("to_allpapers"))
                {
                    change_to_all_papers();
                }
              else if(recv_msg.equals("to_wait"))
                {
                    change_to_wait();
                }
               else if(recv_msg.equals("to_ownpapers"))
                {
                    change_to_ownpapers();
                }
                else if(recv_msg.equals("to_swipe"))
                {
                    change_to_swipe();
                }
                else if(recv_msg.equals("to_last"))
                {
                    change_to_last();
                }
            }
        };
        if(Game.id==0)
        {
            String l="{\"type\":\"client_count\",\"count\":"+SocketHelper.client_count+"}";
            new SocketHelper().send_to_all(l);
            SocketHelper.client_names[0]=SocketHelper.myname;
            String js="{\"type\":\"player_names\"";
            for(int i=0;i<=SocketHelper.client_count;i++)
            {
                js=js.concat(",\""+i+"\":\""+SocketHelper.client_names[i]+"\"");
            }
            js=js.concat("}");
            new SocketHelper().send_to_all(js);

            change_to_swipe();
        }
    }

    @Override
    public void change_to_all_papers()
    {
        Fragment fragment;
        fragment=new all_papers();
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.main_fragment,fragment);
        ft.commit();
    }
    @Override
    public void change_to_swipe()
    {
        Fragment fragment;

        fragment=new swipe();
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.main_fragment,fragment);
        ft.commit();
    }
    @Override
    public void change_to_wait()
    {
        Fragment fragment;
        fragment=new wait();
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.main_fragment,fragment);
        ft.commit();

    }
    @Override
    public void change_to_ownpapers()
    {
        Fragment fragment;
        fragment=new ownpaper();
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.main_fragment,fragment);
        ft.commit();
    }
    @Override
    public void change_to_sitha()
    {
        Fragment fragment;
        fragment=new finding_sitha();
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.main_fragment,fragment);
        ft.commit();
    }
    @Override
    public void change_to_last()
    {
        Fragment fragment;
        fragment=new last();
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.main_fragment,fragment);
        ft.commit();
    }


    void sendrama()
    {
        if(selected_character==0)
        {
            String json="{\"type\":\"merama\",\"id\":"+id+"}";
            new SocketHelper().sendtoleader(json);


        }
    }


}
