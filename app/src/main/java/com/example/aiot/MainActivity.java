package com.example.aiot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;

import org.opencv.android.OpenCVLoader;

import java.net.InetSocketAddress;

public class MainActivity extends AppCompatActivity {
    private SimpleServer server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (OpenCVLoader.initDebug()) {
            Log.d("OpenCV Log", "OpenCV initialized");
        }

        String host = "192.168.0.4";
        int port = 3000;

        new Thread(() -> {
            server = new SimpleServer(new InetSocketAddress(host, port), this);
            server.run();
        }).start();

        Switch switch1 = findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener((compoundButton, b) -> server.broadcast("{\n" +
                "    msgType: \"command\",\n" +
                "    deviceType: \"led\",\n" +
                "    status: \"" + (b ? "on" : "off") + "\",\n" +
                "  }"));

        Button upButton = findViewById(R.id.upButton);
        upButton.setOnClickListener(view -> {
            server.broadcast("{\n" +
                    "    msgType: \"command\",\n" +
                    "    deviceType: \"stepper\",\n" +
                    "    status: \"up\",\n" +
                    "  }");
            server.setDeltaHeight(0);
        });

        Button downButton = findViewById(R.id.downButton);
        downButton.setOnClickListener(view -> {
            server.broadcast("{\n" +
                    "    msgType: \"command\",\n" +
                    "    deviceType: \"stepper\",\n" +
                    "    status: \"down\",\n" +
                    "  }");
            server.setDeltaHeight(0);
        });
    }
}