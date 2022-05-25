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

    UserNode usernode;
    String username;
    HashMap<String, Queue<Message>> conversations;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        ArrayList<String> arrayList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();

        usernode = (UserNode) getIntent().getSerializableExtra("usernode");
        username = bundle.getString("username");

        conversations = initConversations(username);

        usernode = new UserNode("192.168.56.1",5000);
        usernode.setConversation(conversations);

        Log.d("usernode", "mpika ");

        LogInAsyncTask logInAsyncTask = new LogInAsyncTask();
        logInAsyncTask.execute(username);

        Log.d("usernode", "async done");

        for(Map.Entry<String, Queue<Message>> topic: usernode.getConversation().entrySet()) {
            Log.d(logMessage, topic.getKey());
            arrayList.add(topic.getKey());
        }

        listView = (ListView) findViewById(R.id.listViewAllConversations);
        Log.d(logMessage, "listview");
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(HomepageModel.this, ConversationModel.class);
                intent.putExtra("topic",arrayList.get(i));
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

    public HashMap<String, Queue<Message>> initConversations(String name){
        HashMap<String, Queue<Message>> conversations = new HashMap<>();
        BufferedReader br = null;
        try {
            br= new BufferedReader(
                    new InputStreamReader(getAssets().open("data/usernode/userConf.txt"), "UTF-8"));
            String line;
            //gia na broume ton xristi
            while( (line = br.readLine()) != null){
                Queue<Message> tempQueue = new LinkedList<>();
                String[] dataFromLine = line.split(",");
                if(dataFromLine[1].equals(name)){
                    //gia kathe topic
                    for(int j = 2; j < dataFromLine.length; j++){
                        String topic = dataFromLine[j];
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(getAssets().open("data/usernode/"+name+"/"+topic+".txt")));
                        String tempLine;
                        //diabazoume sunomilia
                        while( (tempLine = reader.readLine())!= null){
                            String[] messages = tempLine.split("#");
                            String profileName=messages[1],message=messages[2], date=messages[3];
                            ProfileName userName = new ProfileName(profileName);
                            Message tempMessage = new Message();
                            Date dateSend = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
                            tempMessage.setDate(dateSend);
                            tempMessage.setName(userName);

                            if(message.charAt(0)=='$'){
                                String multimediaFile = message.substring(1);
                                System.err.println("LOOK AT HERE");
                                System.err.println(multimediaFile);
                                AssetFileDescriptor assetFileDescriptor = getAssets().openFd("data/usernode/"+name+"/"+multimediaFile);
                                List<byte[]> listOfChunks = Util.splitFileToChunks(loadFile(assetFileDescriptor), 1024*16);
                                int numOfChunks = listOfChunks.size();
                                ArrayList<MultimediaFile> listOfMultimediaFiles = new ArrayList<>();
                                for (int i = 0; i < numOfChunks; i++){
                                    byte[] tempArr = listOfChunks.get(i);
                                    MultimediaFile tempFile = new MultimediaFile(multimediaFile, profileName, tempArr.length, tempArr);
                                    tempFile.setDateCreated(dateSend);
                                    listOfMultimediaFiles.add(tempFile);
                                }
                                tempMessage.setFiles(listOfMultimediaFiles);
                                tempQueue.add(tempMessage);
                            }
                            else{
                                tempMessage.setMessage(message);
                                tempQueue.add(tempMessage);
                            }
                        }
                        conversations.put(topic,tempQueue);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return conversations;
    }

    public byte[] loadFile(AssetFileDescriptor assetFileDescriptor){
        byte[] fileData = new byte[(int) assetFileDescriptor.getLength()];
        try {
            FileInputStream fis = assetFileDescriptor.createInputStream();
            fis.read(fileData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileData;
    }

}


