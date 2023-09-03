package com.example.yojitha.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Random;
public class swipe extends Fragment
{
    int[] roles_array;
    int[] arr;
    Button swipe,checking;
    View view;

    Communicator comm;
    String tag="swipe";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View rootview=inflater.inflate(R.layout.fragment_swipe, container, false);

        swipe=(Button)rootview.findViewById(R.id.swipe);
        swipe.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View arg0)
            {
                random_roles();
            }
        });
        comm=(Communicator) getActivity();
        return rootview;
    }

    public void random_roles()
    {
        int random = new Random().nextInt(fact(SocketHelper.client_count+1)) + 1;
        roles_array=new int[SocketHelper.client_count+1];
        arr=new int[SocketHelper.client_count+1];
        int rel=SocketHelper.client_count,index=0,t,p;
        int temp,q,r,h;
        int fact=1;
        for(int i=0;i<=SocketHelper.client_count;i++)
        {
            arr[i]=i;
        }
        for(int i=SocketHelper.client_count;i>=1;i--)
        {
            fact=1;
            for(int j=1;j<=i;j++)
            {
                fact=fact*j;
            }
            q=random/fact;
            r=random%fact;
            if(r==0)
            {
                h=--q;
                t=arr[h];
                for(int j=h;j<rel;j++)
                {
                    arr[j]=arr[j+1];
                }
                rel--;
                roles_array[index++]=t;
                for(int j=rel;j>=0;j--)
                {
                    roles_array[index++]=arr[j];
                }
                break;
            }
            h=q;
            t=arr[h];
            for(int j=h;j<rel;j++)
            {
                arr[j]=arr[j+1];
            }
            rel--;
            roles_array[index++]=t;
            random=r;
        }
        String json="{\"type\":\"allpapers\"";
        for(int i=0;i<=SocketHelper.client_count;i++)
        {
            json=json.concat(",\""+i+"\":"+roles_array[i]);
        }
        json=json.concat("}");
        //json="{\"type\":\"allpapers\",\"1\":"+roles_array[0]+",\"2\":"+roles_array[1]+",\"3\":"+roles_array[2]+"}";
        Game.all_papers_string=json;
        if(Game.id==0)
        {
            Log.d("allpapers_line",""+json);
            new SocketHelper().send_to_all(json);
        }
        else
        {
            String replaced = json.replace( "allpapers","other_swipe");
            Log.d("replaced",""+replaced);
            //String json1="{\"type\":\"other_swipe\",\"1\":"+roles_array[0]+",\"2\":"+roles_array[1]+",\"3\":"+roles_array[2]+"}";
            new SocketHelper().sendtoleader(replaced);
        }
        Log.d("before","all_papers");
        comm.change_to_all_papers();
        Log.d("after","all_papers");

    }

    int fact(int n)
    {
        int f=1;
        for(int i=1;i<=n;i++)
        {
            f=f*i;
        }
        return f;
    }
}
