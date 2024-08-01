package com.example.pray;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try{
            IO.Options options = new IO.Options();
            options.transports = new String[]{"websocket"};

            socket = IO.socket("http://10.0.2.2:3000/", options);


            socket.on(Socket.EVENT_CONNECT, onConnect);


            socket.on("connect_error", args -> {
                Object error = args[0];
                Log.d("SocketIO", "Connection error: "+error.toString());


            });
            socket.on("messageServer", onMessage);

        }catch(URISyntaxException e){
            System.out.println("Error: "+e.getMessage());
        }

        socket.connect();
    }


    final private Emitter.Listener onConnect = args -> {
        Log.d("SocketIO", "Connected");

        socket.emit("message", "Hi from android");
    };

    final private Emitter.Listener onMessage = args -> {
       Log.d("SocketIO", "Message received: "+args[0]);

       Intent intent = new Intent(this, Lock.class);
       startActivity(intent);
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            Log.d("SocketIO", "Disconnecting");
            socket.disconnect();
  }
    }
}
    /*private void initializeSocket() {
        try {
            socket = IO.socket("http://127.0.0.1:3000/"); // Replace with your server URL
            socket.on(Socket.EVENT_CONNECT, onConnect);
            socket.connect(); // Initiate the connection
        } catch (Exception e) {
            // Handle connection errors gracefully, e.g., log the error
            e.printStackTrace();
        }
    }

    private Emitter.Listener onConnect = new Emitter.Listener(){
        @Override
        public void call(Object... args){
            System.out.println("Connected");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            socket.disconnect(); // Disconnectthe socket when the activity is destroyed
        }
    }

} */

