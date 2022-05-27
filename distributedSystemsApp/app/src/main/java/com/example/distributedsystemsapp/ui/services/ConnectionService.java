package com.example.distributedsystemsapp.ui.services;

import android.app.Application;
import android.app.Service;
import android.content.Entity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.distributedsystemsapp.domain.Message;
import com.example.distributedsystemsapp.domain.MultimediaFile;
import com.example.distributedsystemsapp.domain.ProfileName;
import com.example.distributedsystemsapp.domain.UserNode;
import com.example.distributedsystemsapp.domain.Util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class ConnectionService extends Application {

    UserNode userNode;
    String name;
    private final String SERVICES = "connectionService";
    private final String READCONV = "readconversationsfromfiles";

    public void connect(){
        Log.d(SERVICES, "i got in connectionService");

        userNode = new UserNode("192.168.56.1",5000);
        userNode.setConversation(initConversations(this.name));
        userNode.init();
        userNode.communicateWithBroker(this.name);
    }

    public ArrayList<String> getTopicOfUser(){
        ArrayList<String> arrayList = new ArrayList<>();

        HashMap<String, Queue<Message>> conversations = userNode.getConversation();

        for(String topic: conversations.keySet()){
            arrayList.add(topic);
            Log.d(SERVICES, "getTopicOfUser: " + topic);
        }

        return arrayList;
    }

    public Queue<Message> getConversation(String topic){
        return userNode.getConversation().get(topic);
    }


    public boolean isCon(){
        return userNode.isSocketAlive();
    }

    public ArrayList<String> getTopicMessages(String topic){


        ArrayList<String> messages = new ArrayList<>();

        Log.d("thisistopic", "topic is: " + topic);

        Queue<Message> conversation = userNode.getConversation().get(topic);


        while (!conversation.isEmpty()){

            Message tmpMessage = conversation.poll();

            String strMessage = "";
            if(tmpMessage.getFiles() == null){
                Log.d("conv", "this is str message");

                strMessage = "Name: " + tmpMessage.getName().getProfileName() + "\n" +
                        "Message: " + tmpMessage.getMessage() + "\n" +
                        "Date: " + tmpMessage.getDate();
            }
            else {
                Log.d("conv", "this is file message");
                strMessage = "Name: " + tmpMessage.getName().getProfileName() + "\n" +
                        "Message: " + tmpMessage.getFiles().get(0).getMultimediaFileName() + "\n" +
                        "Date: " + tmpMessage.getDate();
            }

            Log.d("readmessages", "getTopicMessages: " + strMessage);
            Log.d("topicAndMessage", "Topic: " + topic + "\n" + strMessage);
            messages.add(strMessage);
        }

        return messages;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public HashMap<String, Queue<Message>> initConversations(String name){
        HashMap<String, Queue<Message>> conversations = new HashMap<>();
        BufferedReader br = null;
        try {
            br= new BufferedReader(
                    new InputStreamReader(getAssets().open("data/usernode/userConf.txt"), "UTF-8"));
            String line;
            //gia na broume ton xristi
            nameLoop:
            while( (line = br.readLine()) != null){
                String[] dataFromLine = line.split(",");
                if(dataFromLine[1].equals(name)){
                    //gia kathe topic
                    for(int j = 2; j < dataFromLine.length; j++){
                        Queue<Message> tempQueue = new LinkedList<>();
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
                    break nameLoop;
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
