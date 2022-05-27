package com.example.distributedsystemsapp.ui.homepage;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.distributedsystemsapp.R;
import com.example.distributedsystemsapp.domain.Message;
import com.example.distributedsystemsapp.domain.MultimediaFile;
import com.example.distributedsystemsapp.domain.ProfileName;
import com.example.distributedsystemsapp.domain.UserNode;
import com.example.distributedsystemsapp.domain.Util;
import com.example.distributedsystemsapp.ui.conversation.ConversationModel;
import com.example.distributedsystemsapp.ui.services.ConnectionService;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

public class HomepageModel extends AppCompatActivity implements HomepageView {
    private final String logMessage= "usernode";
    ListView listView;

    private final String HOMEPAGE = "homepageServices";
    UserNode usernode;
    String username;
    HashMap<String, Queue<Message>> conversations;

    ConnectionService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);


        Bundle bundle = getIntent().getExtras();
//
        username = bundle.getString("username");

        ((ConnectionService) this.getApplication()).setName(username);

        ((ConnectionService) this.getApplication()).connect();
//
        Log.d(HOMEPAGE, "i got extra from log in");

        ArrayList<String> arrayList = ((ConnectionService) this.getApplication()).getTopicOfUser();

        Log.d(HOMEPAGE, "onCreate: " + arrayList.toString());


        listView = (ListView) findViewById(R.id.listViewAllConversations);
        Log.d(logMessage, "listview");
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(HomepageModel.this, ConversationModel.class);
                intent.putExtra("topic",arrayList.get(i));
                Log.d(logMessage, "prospathw na mpw");
                startActivity(intent);
            }
        });
    }

    private class LogInAsyncTask extends AsyncTask<String, String, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            String name = params[0];
            usernode.init();
            usernode.communicateWithBroker(name);
            return null;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }

    }

}


