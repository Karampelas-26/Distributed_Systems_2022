package com.example.distributedsystemsapp.ui.conversation;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.distributedsystemsapp.R;
import com.example.distributedsystemsapp.domain.Message;
import com.example.distributedsystemsapp.domain.UserNode;
import com.example.distributedsystemsapp.ui.services.ConnectionService;

import java.util.ArrayList;
import java.util.Queue;

public class ConversationModel extends AppCompatActivity implements ConversationView {

    private String topic;
    private UserNode usernode;
    private Queue<Message> conversation;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.conversation);


        Bundle bundle = getIntent().getExtras();
        topic = bundle.getString("topic");

//        Queue<Message> selectedConversation = ((ConnectionService) this.getApplication()).getConversation(topic);

        Log.d("usernode", "ksanampika");

//        Log.d("messages", "conv: " + selectedConversation.peek().getMessage());

        Log.d("usernode", "this is usernode name: " + ((ConnectionService) this.getApplication()).isCon());


        ArrayList<String> messages = ((ConnectionService) this.getApplication()).getTopicMessages(topic);

        Log.d("messages", "onCreate: " + messages.size());

//
//
//        ArrayList<String> messages =  new ArrayList<>();
//
//        messages.add("hey");
//        messages.add("hey");
//        messages.add("hey");
//        messages.add("hey");
//        messages.add("hey");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messages);

        listView = (ListView) findViewById(R.id.conversationListView);

        listView.setAdapter(adapter);



        Toast.makeText(getApplicationContext(),topic,Toast.LENGTH_LONG ).show();
    }


}
