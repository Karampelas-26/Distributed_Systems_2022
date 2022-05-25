package com.example.distributedsystemsapp.ui.conversation;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ConversationModel extends AppCompatActivity implements ConversationView {

    private String topic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();

        topic = bundle.getString("topic");
        Toast.makeText(getApplicationContext(),topic,Toast.LENGTH_LONG ).show();

    }
}
