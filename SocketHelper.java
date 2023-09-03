package com.example.yojitha.game;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketHelper
{
    static ServerSocket socket;
    static Socket connected_client;
    static Socket[] connected_peers=new Socket[10];
    static int client_count=0;
    static Thread[] client_threads = new Thread[10];

    static Socket jsocket;
    static int id=1;
    Game game;
    static int[] check_available={0,0,0,0,0,0,0,0,0,0};
    static int[] selected_roles={0,0,0,0,0,0,0,0,0,0};
    static int[] scores={0,0,0,0,0,0,0,0,0,0};
    static String[] client_names=new String[10];
    static String myname;
    static int client_name_index=0;
    static int next_chance=0;
    static int safety=0;
    public SocketHelper()
    {

    }
    public SocketHelper(int i)
    {

        client_count=0;
        Thread serverthread=new Thread(new ServerThread());
        serverthread.start();
        game =new Game();
        Game.id=0;
        Log.d("socket","constructor");
    }

    public SocketHelper(InetAddress ip,int port)
    {
        Thread conn=new Thread(new connect(ip,port));
        conn.start();
        game =new Game();
        Log.d("client","constructor");
    }

    public class ServerThread implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                socket = new ServerSocket(2345);
                for(int i=0;i<7;i++)
                {
                    Log.d("before","accept");
                    connected_client = socket.accept();
                    Log.d("after","accept");
                    client_count++;
                    connected_peers[client_count]=connected_client;

                    client_threads[i]=new Thread(new ClientThread(connected_client));
                    client_threads[i].start();
                    String ip=connected_client.getInetAddress().toString();
                //    new CreateGameActivity().add_peer(ip);

                }
            }
            catch(Exception e)
            {                Log.e("Error is"," ",e);
            }
        }
    }

    class C_sender implements Runnable
    {
        String str;
        Socket socket;
        public C_sender(String str,Socket socket)
        {
            this.str=str;
            this.socket=socket;

        }
        @Override
        public void run()
        {
            try
            {
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                out.println(str);
            }
            catch(Exception e)
            {
                Log.e("Error is"," ",e);
            }
        }
    }

    public class ClientThread implements Runnable
    {
        private BufferedReader in;
        private Socket clientSocket;
        public ClientThread(Socket clientSocket)
        {
            this.clientSocket=clientSocket;
            try {
                in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            }
            catch(Exception e)
            {
                Log.e("Error is"," ",e);
            }
            String json = "{ \"type\":\"id\",\"id\":"+id+"}";
            Log.d("socket helper id alloc"," "+id);
            send_to_one(json,clientSocket);
            id++;
            json = "{ \"type\":\"name_request\"}";
            send_to_one(json,clientSocket);

        }
        @Override
        public void run()
        {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String read = in.readLine();

                    Log.d("le received_message ",read);
                    JSONObject mess=(new JSONObject(read));
                    String type=mess.getString("type");

                    if(type.equals("my_name_is"))
                    {
                        String na=mess.getString("name");
                        putname(na);
                        Bundle messageBundle = new Bundle();
                        messageBundle.putString("msg",na);

                        Message message = new Message();
                        message.setData(messageBundle);
                        CreateGameActivity.UpdateHandler.sendMessage(message);
                        Log.d("after","game.cc");

                    }
                    if(type.equals("check"))
                    {
                        Log.d("at ","check");
                        int selected=mess.getInt("selected");
                        if(check_available[selected]==0)
                        {
                            check_available[selected]=1;

                            String json="{\"type\":\"checking_result\",\"result\":1,\"selected\":"+selected+"}";
                            send_to_one(json,connected_peers[mess.getInt("id")]);
                            selected_roles[mess.getInt("id")]=selected;
                            Log.d("checking","available");
                        }
                        else
                        {
                            Log.d("checking","not available");
                            String json="{\"type\":\"checking_result\",\"result\":0,\"selected\":"+selected+"}";
                            send_to_one(json,connected_peers[mess.getInt("id")]);
                        }

                     /*   if(allocating_finished()==1)
                        {
                            if(Game.selected_character==0)
                            {
                                String json="{\"type\":\"ramais\",\"id\":"+id+"}";
                                Game.ramais=id;
                                send_to_all(json);
                            }
                            if(Game.selected_character==1)
                            {
                                Game.sitha_is=Game.id;
                            }

                            String json = "{\"type\":\"all_done\"}";
                            send_to_all(json);
                       }*/
                    }
                    if(type.equals("is_allocating_finished"))
                    {
                        if(allocating_finished()==1)
                        {

                            if(Game.selected_character==0)
                            {
                                String json="{\"type\":\"ramais\",\"id\":"+Game.id+"}";
                                Game.ramais=Game.id;
                                Bundle messageBundle = new Bundle();
                                messageBundle.putString("msg","");
                                messageBundle.putString("ss","ra");
                                Message message = new Message();
                                message.setData(messageBundle);
                                ownpaper.own_UpdateHandler.sendMessage(message);
                                send_to_all(json);

                            }
                            if(Game.selected_character==1)
                            {
                                Game.sitha_is=Game.id;
                            }
                            String json = "{\"type\":\"all_done\"}";
                            send_to_all(json);
                        }
                    }
                    if(type.equals("merama"))
                    {
                        int id= mess.getInt("id");                               //clients sending he is rama
                        String json="{\"type\":\"ramais\",\"id\":"+id+"}";
                        Game.ramais=id;

                        Bundle messageBundle = new Bundle();
                        messageBundle.putString("msg","");
                        messageBundle.putString("ss","ra");
                        Message message = new Message();
                        message.setData(messageBundle);
                        ownpaper.own_UpdateHandler.sendMessage(message);

                        send_to_all(json);
                    }
                    if(type.equals("mesitha"))
                    {
                        int id= mess.getInt("id");          // client sending he is sitha
                        Game.sitha_is=id;
                    }
                    if(type.equals("this_is_sitha"))
                    {
                            int id=mess.getInt("id");
                            String json12="";
                            String messanger="";
                            if(id==Game.sitha_is)                    //client choose he is sitha (game desiding)
                            {
                                    json12="{\"type\":\"game_result\",\"result\":\"correct\",\"re_si\":"+Game.sitha_is+"}";
                                    Log.d("result "," "+json12);
                                    //   send_to_all(json);
                                    Game.result="correct";
                                    Game.real_sitha=Game.sitha_is;
                                    messanger="RAMA SELECTED SITHA CORRECTLY \nRAMA IS "+client_names[Game.ramais]+"\nSITHA IS "+client_names[Game.sitha_is];
                                    scores[Game.ramais]+=1000;
                            }
                            else
                            {
                                json12="{\"type\":\"game_result\",\"result\":\"wrong\",\"re_si\":"+Game.sitha_is+",\"se_si\":\""+client_names[id]+"\"}";
                                Log.d("result "," "+json12);
                                //  send_to_all(json);
                                Game.result="wrong";
                                Game.real_sitha=Game.sitha_is;
                                Game.selected_sitha_string=client_names[id];
                                Game.selected_sitha=id;
                                messanger="RAMA SELECTED SITHA WRONGLY \nRAMA IS "+client_names[Game.ramais]+"\nSITHA IS "+client_names[Game.sitha_is]+"\n BUT RAMA SELECTED "+client_names[id];
                                scores[Game.sitha_is]+=1000;
                            }
                                update_score();
                                update_all();
                                String sco_sen ="{\"type\":\"score\"";
                                for(int i=0;i<=SocketHelper.client_count;i++)
                                {
                                    sco_sen=sco_sen.concat(",\""+i+"\":"+scores[i]);
                                }
                            sco_sen=sco_sen.concat("}");
                        //    send_to_all(sco_sen);
                            send_to_all(json12);

                            Bundle messageBundle = new Bundle();
                            messageBundle.putString("msg","to_last");
                            Message message = new Message();
                            message.setData(messageBundle);
                            Game.mUpdateHandler.sendMessage(message);
                            Log.d("after","game.cc");


                     /*       if(next_chance_is()==0)
                            {
                                if(Game.ramais==Game.id)
                                {
                                    update_all();
                                    Bundle messageBundl = new Bundle();
                                    messageBundl.putString("msg"," ");
                                    messageBundl.putString("ss","new_game");
                                    Message messag = new Message();
                                    messag.setData(messageBundl);
                                    finding_sitha.finding_sitha_UpdateHandler.sendMessage(messag);
                                    Log.d("after","game.cc");
                                }
                                else
                                {

                                    update_all();
                                    Bundle messageBundl = new Bundle();
                                    messageBundl.putString("msg","new_game");
                                  //  messageBundl.putString("ss","new_game");
                                    Message messag = new Message();
                                    messag.setData(messageBundl);
                                    last.last_UpdateHandler.sendMessage(messag);
                                    Log.d("after","game.cc");
                                }
                                update_next_chance();
                            }
                        else
                            {
                                new_game();
                            }*/
                    }
                    if(type.equals("last_came"))
                    {
                        int idd=mess.getInt("id");
                        String sco_sen ="{\"type\":\"score\"";
                        for(int i=0;i<=SocketHelper.client_count;i++)
                        {
                            sco_sen=sco_sen.concat(",\""+i+"\":"+scores[i]);
                        }
                        sco_sen=sco_sen.concat("}");
                        send_to_all(sco_sen);

                        if(idd==next_chance_is())
                        {
                            update_next_chance();
                            String json = "{\"type\":\"new_game\"}";
                            Log.d("sending to",""+next_chance);
                            send_to_one(json, connected_peers[next_chance]);
                        }


                    }

                    if(type.equals("other_swipe"))
                    {

                        String replaced = read.replace("other_swipe", "allpapers");
                        send_to_all(replaced);
                        Game.all_papers_string=replaced;
                        Bundle messageBundle = new Bundle();
                        messageBundle.putString("msg","to_allpapers");
                        Message message = new Message();
                        message.setData(messageBundle);
                        Game.mUpdateHandler.sendMessage(message);

                    }

                } catch (Exception e) {
                    Log.e("Error is"," ",e);
                }
            }
        }
    }

    class connect implements Runnable
    {
        InetAddress server_ip;
        int server_port;
        public connect(InetAddress ip,int port)
        {
            server_ip=ip;
            server_port=port;
        }
        @Override
        public void run()
        {
            try
            {
                Log.d("before","client connect");
                jsocket = new Socket(server_ip,server_port);
                Log.d("after","client connect");
                Thread receivethread=new Thread(new ReceiveThread(jsocket));
                receivethread.start();
            }
            catch(Exception e)
            {
                Log.e("Error is"," ",e);
            }

        }
    }

    class send_to_leader implements Runnable
    {
        String str;
        public send_to_leader(String str)
        {
            this.str=str;
        }
        @Override
        public void run()
        {
            try
            {
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(jsocket.getOutputStream())), true);
                out.println(str);
            }
            catch(Exception e)
            {
                Log.e("Error is"," ",e);
            }
        }
    }
    class ReceiveThread implements Runnable
    {
        Socket client_socket;
        BufferedReader input;
        public ReceiveThread(Socket conn_socket)
        {
            client_socket=conn_socket;
            try
            {
                this.input = new BufferedReader(new InputStreamReader(this.client_socket.getInputStream()));
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Log.e("Error is"," ",e);
            }
        }
        @Override
        public void run()
        {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String read = input.readLine();
                    Log.d("cli received_message ",read);

                    JSONObject mess=(new JSONObject(read));
                    String type=mess.getString("type");

                    if(type.equals("id"))
                    {
                        int id=mess.getInt("id");                 // leader send id
                        Log.d("recv id is"," "+id);
                        Game.id=id;
                    }

                    if(type.equals("name_request"))
                    {
                        Log.d("chudam name"," "+SocketHelper.myname);
                        Log.d("hai   "," hello");                                            //leader asks our name
                        String son="{\"type\":\"my_name_is\",\"name\":\""+SocketHelper.myname+"\"}";
                        sendtoleader(son);
                    }

                    if(type.equals("client_count"))
                    {
                        client_count=mess.getInt("count");             //leader sends clients count
                    }
                    if(type.equals("player_names"))
                    {

                        for(int i=0;i<=client_count;i++) {
                            client_names[i] = mess.getString(""+i);       //leader sends all player count
                        }
                    }
                    if(type.equals("allpapers"))
                    {
                        Log.d("recev","all papers");
                        Log.d("before","game.cc");
                        Game.all_papers_string=read;
                        Bundle messageBundle = new Bundle();                    // allpapers
                        messageBundle.putString("msg","to_allpapers");

                        Message message = new Message();
                        message.setData(messageBundle);
                        Game.mUpdateHandler.sendMessage(message);
                        Log.d("after","game.cc");

                    }

                    if(type.equals("checking_result"))
                    {
                        Log.d("at checking result"," before");
                        if(mess.getInt("result")==1)
                        {
                            Log.d("result"," 1");
                            Game.selected_character=mess.getInt("selected");               // our paper selection
                            Log.d("game.selected_character"," "+Game.selected_character);
                            Bundle messageBundle = new Bundle();
                            messageBundle.putString("msg","to_ownpapers");
                            Message message = new Message();                                 // our choice is selected
                            message.setData(messageBundle);
                            Game.mUpdateHandler.sendMessage(message);
                            Log.d("after","game.cc");
                        }
                        else
                        {
                            Bundle messageBundle = new Bundle();
                            messageBundle.putString("msg","cc");
                            messageBundle.putString("ss",""+mess.getInt("selected"));
                            Message message = new Message();                                    // our choice is not selected
                            message.setData(messageBundle);
                            all_papers.allpapers_UpdateHandler.sendMessage(message);
                            Log.d("after","game.cc");
                            Log.d("sockets","alredy finished");
                        }
                    }
                    if(type.equals("all_done"))
                    {
                      if(Game.selected_character==0)
                      {
                          String json = "{\"type\":\"merama\",\"id\":" + Game.id + "}";        // game initilization process is done
                          sendtoleader(json);                                                  // sending if iam rama or sitha
                      }
                        if(Game.selected_character==1)
                        {
                            String json="{\"type\":\"mesitha\",\"id\":"+Game.id+"}";
                            new SocketHelper().sendtoleader(json);
                        }
                    }
                    if(type.equals("ramais"))
                    {

                        Game.ramais=mess.getInt("id");
                                                                       // who rama is
                        Bundle messageBundle = new Bundle();
                        messageBundle.putString("msg","k");
                        messageBundle.putString("ss","ra");
                        Message message = new Message();
                        message.setData(messageBundle);
                        ownpaper.own_UpdateHandler.sendMessage(message);
                        Log.d("after","game.cc");
                    }

                    if(type.equals("game_result"))
                    {
              /*          if(Game.selected_character!=0) {
                            Log.d("at final"," sitha goes to own paper");
                            String messager = mess.getString("message");
                            Bundle messageBundle = new Bundle();
                            messageBundle.putString("msg", messager);
                            messageBundle.putString("ss", "reesult");
                            Message message = new Message();
                            message.setData(messageBundle);
                            ownpaper.own_UpdateHandler.sendMessage(message);
                            Log.d("after", "game.cc");
                        }
                        else
                        {
                            Log.d("at"," sitha goes to findpaper");
                            String messager = mess.getString("message");
                            Bundle messageBundle = new Bundle();
                            messageBundle.putString("msg", messager);
                            messageBundle.putString("ss", "reesult");
                            Message message = new Message();
                            message.setData(messageBundle);
                            finding_sitha.finding_sitha_UpdateHandler.sendMessage(message);
                            Log.d("after", "game.cc");
                        }*/

                        Game.result=mess.getString("result");
                        Log.d("game result",""+Game.result);

                        if(Game.result.equals("correct"))
                        {
                            Log.d("at game _resut","scsc");
                            Game.result="correct";
                            Game.real_sitha=mess.getInt("re_si");
                        }
                        else
                        {
                            Log.d("at game _resut","scsccwww");
                            Game.result="wrong";
                            Game.real_sitha=mess.getInt("re_si");
                            Game.selected_sitha_string=mess.getString("se_si");
                            //Game.selected_sitha=mess.getInt("se_si");
                        }
                        Bundle messageBundle = new Bundle();                    // allpapers
                        messageBundle.putString("msg","to_last");
                        Message message = new Message();
                        message.setData(messageBundle);
                        Game.mUpdateHandler.sendMessage(message);
                        Log.d("last","initialised");
                    }
                    if(type.equals("score"))
                    {
                        for(int i=0;i<=SocketHelper.client_count;i++)
                        {
                            scores[i]=mess.getInt(""+i);                    //updating score
                        }

                        Bundle messageBundle = new Bundle();
                        messageBundle.putString("msg","update_score");
                        messageBundle.putString("ss", "update_score");
                        Message message = new Message();                    //intimating last for score
                        message.setData(messageBundle);
                        last.last_UpdateHandler.sendMessage(message);

                    }
                    if(type.equals("new_game"))
                    {
                       /* if(Game.ramais==Game.id)
                        {
                            Bundle messageBundl = new Bundle();
                            messageBundl.putString("msg", " ");
                            messageBundl.putString("ss","new_game");
                            Message messag = new Message();
                            messag.setData(messageBundl);
                            finding_sitha.finding_sitha_UpdateHandler.sendMessage(messag);
                            Log.d("after","game.cc");
                        }
                        else
                        {
                            Bundle messageBundl = new Bundle();
                            messageBundl.putString("msg","");
                            messageBundl.putString("ss","new_game");
                            Message messag = new Message();
                            messag.setData(messageBundl);
                            ownpaper.own_UpdateHandler.sendMessage(messag);
                            Log.d("after","game.cc");
                        }*/
                        Bundle messageBundl = new Bundle();
                        messageBundl.putString("msg","new_game");
                       // messageBundl.putString("ss","new_game");
                        Message messag = new Message();
                        messag.setData(messageBundl);
                        last.last_UpdateHandler.sendMessage(messag);
                    }

                } catch (Exception e)
                    {
                        Log.e("Error is"," ",e);
                    }
            }
         }
    }

    void send_to_all(String str)
    {
        for(int i=0;i<client_count;i++)
        {
            Thread sendert = new Thread(new C_sender(str, connected_peers[i+1]));
            sendert.start();
        }
    }

    void send_to_one(String str,Socket socket)
    {
        Thread sender_one= new Thread(new C_sender(str,socket));
        sender_one.start();
    }
    void sendtoleader(String str)
    {
        Thread sendl=new Thread( new send_to_leader(str));
        sendl.start();
    }
    int allocating_finished()
    {
        for(int i=0;i<=client_count;i++)
        {
            if(check_available[i]==0)
            {
                return 0;
            }
        }
        return 1;
    }
    int c_getport()
    {
        int port=2345;
        return port;
    }

    void  putname(String name)
    {
        client_names[++client_name_index]=name;
    }
    void update_next_chance()
    {
        if(next_chance==client_count)
        {
            next_chance=0;
        }
        else
        {
            next_chance++;
        }
    }
    int next_chance_is()
    {
        if(next_chance==client_count)
        {
            Log.d("returning","0");
            return 0;
        }
        else
        {Log.d("returning",""+next_chance+1);
           return  next_chance+1;
        }
    }
    void new_game()
    {
        Log.d("calling","new game at sockerfinder");
       update_all();
        update_next_chance();
        if(next_chance==0)
        {
           /* Bundle messageBundle = new Bundle();
            messageBundle.putString("msg","to_swipe");
            Message message = new Message();
            message.setData(messageBundle);
            Game.mUpdateHandler.sendMessage(message);*/
        }
        else
        {
            String json = "{\"type\":\"new_game\"}";
            Log.d("sending to",""+next_chance);
            send_to_one(json, connected_peers[next_chance]);
            Log.d("sent","newgame");
        }
    }
   void update_all()
   {
       for(int i=0;i<=client_count;i++) {
           check_available[i] = 0;
           selected_roles[i] = 0;
       }
   }
    void update_score()
    {
        for(int i=0;i<=client_count;i++)
        {
            if(selected_roles[i]!=0 && selected_roles[i]!=1)
            {
                scores[i]=scores[i]+(1000-((selected_roles[i]-1)*100));
            }

        }
    }
    void display_scores()
    {
        for(int i=0;i<=client_count;i++)
        {
            Log.d("scores ",""+i+" "+scores[i]);
        }
    }
}
