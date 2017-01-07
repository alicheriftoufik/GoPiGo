package com.example.toufik.gopigo_master2_ise;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends Activity {

    Button up, down, right, left, start, pause;
    Socket socket;
    int stop=0;
    String last_command="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gopigo);

        start = (Button)findViewById(R.id.start);
        pause = (Button)findViewById(R.id.pause);
        up = (Button)findViewById(R.id.up);
        down = (Button)findViewById(R.id.down);
        right = (Button)findViewById(R.id.right);
        left = (Button)findViewById(R.id.left);
    }


    public void onClick(View view){
        final int id = view.getId();
        switch (id) {
            case R.id.start:
                MyClientTask myClientTask = new MyClientTask("192.168.43.178",12345);
                myClientTask.execute();
                break;
            case R.id.up:
                sendMessage("fwd");
                last_command="fwd";
                break;
            case R.id.down:
                sendMessage("bwd");
                last_command="bwd";
                break;
            case R.id.right:
                sendMessage("right");
                last_command="right";
                break;
            case R.id.left:
                sendMessage("left");
                last_command="left";
                break;
            case R.id.pause:
                if(stop==0){sendMessage("stop");
                }else{sendMessage(last_command);}
                stop=1-stop;
                break;
        }
    }

    private void sendMessage(String msg){
        if(socket==null){
            Toast.makeText(getApplicationContext(), "Press on Connection before", Toast.LENGTH_LONG).show();
        }else {
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())),
                        true);
                out.println(msg);
            } catch (IOException ioe) {
                Log.e(getClass().getSimpleName(), ioe.toString());
            }
        }
    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response;

        MyClientTask(String addr, int port){
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                socket = new Socket(dstAddress, dstPort);
                InputStream inputStream = socket.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1){
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }

                socket.close();
                response = byteArrayOutputStream.toString("UTF-8");

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //textResponse.setText(response);
            super.onPostExecute(result);
        }

    }
}