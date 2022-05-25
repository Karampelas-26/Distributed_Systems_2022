package com.example.distributedsystemsapp.ui.logIn;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.distributedsystemsapp.R;
import com.example.distributedsystemsapp.domain.Message;
import com.example.distributedsystemsapp.domain.MultimediaFile;
import com.example.distributedsystemsapp.domain.ProfileName;
import com.example.distributedsystemsapp.domain.UserNode;
import com.example.distributedsystemsapp.domain.Util;
import com.example.distributedsystemsapp.ui.homepage.HomepageModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class LogInModel extends AppCompatActivity implements LogInView{

    Button buttonLogIn;

    private LogInPresenter presenter;
    private Context context;
    ArrayList<String> users;
    EditText username;
    UserNode usernode;
    HashMap<String, Queue<Message>> conversations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

        users= initUsers();
        username=findViewById(R.id.textLogInName);


        presenter = new LogInPresenter(this);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        buttonLogIn = (Button) findViewById(R.id.logInBttn);
        buttonLogIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name= presenter.onLogIn();
                if(!name.equals(" ")){
                    setConversations(initConversations(name));
                    usernode = new UserNode("10.1.22.83",5000);
                    usernode.setConversation(conversations);
                    usernode.init();
                    usernode.communicateWithBroker(name);
                    Intent intent = new Intent(LogInModel.this, HomepageModel.class);
                    intent.putExtra("username",name);
                    intent.putExtra("usernode", usernode);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Invalid username. Please input a valid username!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public ArrayList<String> initUsers(){

        ArrayList<String> users= new ArrayList<>();
        BufferedReader br = null;

        try {
            br= new BufferedReader(
                    new InputStreamReader(getAssets().open("data/usernode/userConf.txt"), "UTF-8"));
            String line;
            while( (line = br.readLine()) != null){
                String[] dataFromLine = line.split(",");
                users.add(dataFromLine[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return users;
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
//                            System.err.println(tempLine);
//                            System.err.println(messages[1]);
//                            System.err.println(messages[2]);
//                            System.err.println(messages[3]);
//                            System.err.println(messages.length);
                            String profileName=messages[1],message=messages[2], date=messages[3];
//                            System.err.println("ProfileName: "+profileName);
//                            System.err.println("Message: "+message);
//                            System.err.println("Date: "+date);
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

    @Override
    public ArrayList<String> getUsers(){
        return this.users;
    }

    @Override
    public EditText getUsername(){
        return this.username;
    }

    public void setConversations(HashMap<String, Queue<Message>> conv){
        this.conversations= conv;
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
