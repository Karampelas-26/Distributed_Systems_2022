package com.example.distributedsystemsapp.ui.homepage;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.distributedsystemsapp.R;
import com.example.distributedsystemsapp.domain.Message;
import com.example.distributedsystemsapp.domain.UserNode;

import java.util.ArrayList;
import java.util.Objects;

public class HomepageModel extends AppCompatActivity implements HomepageView {

    ListView listView;

    UserNode usernode;
    String username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        ArrayList<Message> arrayList = null;

        Bundle bundle = getIntent().getExtras();

        usernode = (UserNode) getIntent().getSerializableExtra("usernode");
        username = bundle.getString("username");
        for(Message message: Objects.requireNonNull(usernode.getConversation().get(username))){
            arrayList.add(message);
        }

        listView = (ListView) findViewById(R.id.listViewAllConversations);
        
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(adapter);
    }




}


