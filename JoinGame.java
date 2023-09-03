package com.example.yojitha.game;

import android.content.Intent;
import android.net.nsd.NsdServiceInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;

public class JoinGame extends AppCompatActivity {

    NsdHelper nsdhelper;
    static SocketHelper sockethelper;
    String[] service_names={"hai",};
    int count=1;
    ArrayAdapter adapter;
    ListView listview;
    HashMap<String,NsdServiceInfo> hm;
    Button start_game;
    EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        name=(EditText) findViewById(R.id.player_name);

//        Log.d("name check",SocketHelper.myname);
        sockethelper=new SocketHelper();
        nsdhelper=new NsdHelper(this);
        nsdhelper.initializeNsd();
        nsdhelper.discoverServices();

       /* hm=new HashMap<String,NsdServiceInfo>();
        adapter=new ArrayAdapter<String>(this,R.layout.join_list,service_names);
        listview=(ListView)findViewById(R.id.service_list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = (String) parent.getItemAtPosition(position);
                NsdServiceInfo service=hm.get(selectedItem);
                sockethelper=new SocketHelper(service.getHost(),service.getPort());

                Intent inent = new Intent(JoinGame.this, Game.class);
                startActivity(inent);
            }
        });*/

        start_game=(Button)findViewById(R.id.start_game);
        start_game.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View arg0)
            {
                NsdServiceInfo service = nsdhelper.getChosenServiceInfo();
                Log.d("Success", "Connecting.");
                Log.d("Success","ip "+service.getHost()+"port "+ service.getPort());
                SocketHelper.myname=name.getText().toString();
                sockethelper =new SocketHelper(service.getHost(),service.getPort());

                Intent inent = new Intent(JoinGame.this, Game.class);
                startActivity(inent);
            }
        });
    }

 /*   public void addservice(String str)
    {
        service_names[count]=str;
        adapter.notifyDataSetChanged();
    }
    public void add_hashmap(String str,NsdServiceInfo serviceinfo)
    {
        hm.put(str,serviceinfo);
    }*/
}
