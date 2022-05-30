package com.example.distributedsystemsapp.ui.conversation;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    ListView listView;
    Button sendMessageButton, sendMediaButton;
    EditText textField;
    ArrayList<String> messages;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.conversation);


        Bundle bundle = getIntent().getExtras();
        topic = bundle.getString("topic");

        ((ConnectionService) this.getApplication()).getUserNode().checkBroker(topic);

//        Queue<Message> selectedConversation = ((ConnectionService) this.getApplication()).getConversation(topic);

        Log.d("usernode", "ksanampika");

//        Log.d("messages", "conv: " + selectedConversation.peek().getMessage());

//        Log.d("usernode", "this is usernode name: " + ((ConnectionService) this.getApplication()).isConnected());


        messages = ((ConnectionService) this.getApplication()).getTopicMessages(topic);

        Log.d("messages", "onCreate: " + messages.size());

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messages);

        listView = (ListView) findViewById(R.id.conversationListView);


        sendMessageButton = (Button) findViewById(R.id.messageButton);
        sendMediaButton = (Button) findViewById(R.id.mediaButton);
        textField = (EditText) findViewById(R.id.newMessage);

        listView.setAdapter(adapter);


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = textField.getText().toString();
                sendMessage(message);
                adapter.add(message);
            }
        });



        sendMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("newMessages", "onClick: ");
//                checkForMessages();
                ReadMessagesFromServerAsync r = new ReadMessagesFromServerAsync();
                r.execute();
            }
        });




    }

    private void sendMessage(String message){
        ((ConnectionService) this.getApplication()).getPublisher().sendMessage(topic, message);
    }

    private void checkForMessages(){
        int difference = ((ConnectionService) this.getApplication()).showConversation(topic);
        Log.d("newMessages", "checkForMessages: " +  difference);

        if (difference > 0) {

            ArrayList<String> newMessages = ((ConnectionService) this.getApplication()).getLastMessages(topic, difference);
            Log.d("newMessages", "checkForMessages: " + newMessages) ;
            adapter.addAll(newMessages);
        }

    }




    @Override
    protected void onStart() {
        super.onStart();


//        while(((ConnectionService) this.getApplication()).isConnected()){
//            Log.d("xontreeeeee", "edw pathainw chocho");
//
//
//            int difference = ((ConnectionService) this.getApplication()).showConversation(topic);
//
//            if (difference > 0) {
//
//                ArrayList<String> newMessages = ((ConnectionService) this.getApplication()).getLastMessages(topic, difference);
//                adapter.addAll(newMessages);
//            }
//
//
//        }

    }

    private class ReadMessagesFromServerAsync extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {

            checkForMessages();

            return null;
        }
    }

}
