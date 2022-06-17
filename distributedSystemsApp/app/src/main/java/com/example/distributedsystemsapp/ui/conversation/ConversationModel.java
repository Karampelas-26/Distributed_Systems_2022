package com.example.distributedsystemsapp.ui.conversation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.distributedsystemsapp.R;
import com.example.distributedsystemsapp.domain.Message;
import com.example.distributedsystemsapp.domain.MultimediaFile;
import com.example.distributedsystemsapp.domain.Publisher;
import com.example.distributedsystemsapp.domain.UserNode;
import com.example.distributedsystemsapp.ui.homepage.HomepageModel;
import com.example.distributedsystemsapp.ui.services.ConnectionService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.Inflater;

public class ConversationModel extends AppCompatActivity implements ConversationView {


    private final int PICK_GALLERY = 1;
    private final int TAKE_PICTURE = 2;
    private final int PICK_VIDEO = 3;
    private final int TAKE_VIDEO = 4;


    private String topic;
    private UserNode usernode;
    private Queue<Message> conversation;
    ListView listView;
    Button sendMessageButton, sendMediaButton;
    EditText textField;
    ArrayList<String> messages;
    ArrayAdapter<String> adapter;

    private Bitmap media;
    private MediaMetadataRetriever video;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.conversation);
        Bundle bundle = getIntent().getExtras();
        topic = bundle.getString("topic");

        ((ConnectionService) this.getApplication()).getUserNode().checkBroker(topic);

        messages = ((ConnectionService) this.getApplication()).getTopicMessages(topic);

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
                if(message.trim().length()>0){
                    sendMessage(message);
                }
            }
        });


        sendMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageOptionsDialog();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String res = typeOfMessage(i);

                if(res.equals("v")){
                    showAlertDialog("v");
                }
                else if (res.equals("p")){
                   showAlertDialog("p");
                }

            }
        });


        loopInAnotherThread();
    }

    private String typeOfMessage(int i){

        LinkedList<Message> conversation = (LinkedList<Message>) ((ConnectionService) this.getApplication()).getConversation(topic);

        Message temp = conversation.get(i);

        if (temp.getFiles() == null){
            return "m";
        }
        else {
            String file = temp.getFiles().get(0).getMultimediaFileName();
            String[] fileNameParts = file.split("\\.");
            String fileType = fileNameParts[fileNameParts.length - 1];
            return fileType.equals("mp4") ? "v" : "p";
        }


    }

    private void showImageOptionsDialog() {
        String[] choices = {"Picture from gallery",
                "Take picture",
                "Video from gallery",
                "Take video"};

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int choice) {
                switch (choice) {
                    case 0:
                        choosePicture();
                        break;

                    case 1:
                        takePicture();
                        break;
                    case 2:
                        chooseVideo();
                        break;
                    case 3:
                        takeVideo();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialogInterface.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ConversationModel.this);
        builder.setTitle("Choose source of media: ").setItems(choices, dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
    }

    private void choosePicture() {
        Intent choosePictureIntent = new Intent();
        choosePictureIntent.setType("image/*");
        choosePictureIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(choosePictureIntent, PICK_GALLERY);
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, TAKE_PICTURE);
    }

    private void chooseVideo() {
        Intent choosePictureIntent = new Intent();
        choosePictureIntent.setType("video/*");
        choosePictureIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(choosePictureIntent, PICK_VIDEO);
    }

    private void takeVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(takeVideoIntent,TAKE_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("pushinnggg", "onActivityResult: i m in");
        if (requestCode == TAKE_PICTURE) {
            media = null;
            Bundle extras = data.getExtras();

            media = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            media.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytearray = stream.toByteArray();
            media.recycle();
            sendMediaToBroker(bytearray,".jpg");
            MultimediaFile multimediaFile = new MultimediaFile();
            multimediaFile.setMultimediaFileChunk(bytearray);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            multimediaFile.setMultimediaFileName(timeStamp + ".jpg");
            ArrayList<MultimediaFile> files = new ArrayList<>();

        }
        else if (requestCode == PICK_GALLERY) {

            media = null;
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getApplication().getContentResolver().openInputStream(imageUri);
                media = BitmapFactory.decodeStream(inputStream);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                media.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytearray = stream.toByteArray();
                media.recycle();
                sendMediaToBroker(bytearray,".jpg");
                MultimediaFile multimediaFile = new MultimediaFile();
                multimediaFile.setMultimediaFileChunk(bytearray);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                multimediaFile.setMultimediaFileName(timeStamp + ".jpg");
                ArrayList<MultimediaFile> files = new ArrayList<>();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode == PICK_VIDEO) {
            media = null;
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getApplication().getContentResolver().openInputStream(imageUri);
                media = BitmapFactory.decodeStream(inputStream);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                media.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytearray = stream.toByteArray();
                media.recycle();
                sendMediaToBroker(bytearray,".mp4");
                MultimediaFile multimediaFile = new MultimediaFile();
                multimediaFile.setMultimediaFileChunk(bytearray);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                multimediaFile.setMultimediaFileName(timeStamp + ".mp4");
                ArrayList<MultimediaFile> files = new ArrayList<>();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == TAKE_VIDEO){

            Uri videoUri= null;

            if (resultCode == RESULT_OK) {
                videoUri = data.getData();
            }

            InputStream fis;
            try {
                fis = getContentResolver().openInputStream(videoUri);
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];

                int len = 0;
                while ((len = fis.read(buffer)) != -1) {
                    byteBuffer.write(buffer, 0, len);
                }
                byte[] bytearray = byteBuffer.toByteArray();
                Log.d("vidd", String.valueOf(getApplicationContext().getFilesDir().getAbsolutePath()));
                sendMediaToBroker(bytearray,".mp4");
                MultimediaFile multimediaFile = new MultimediaFile();
                multimediaFile.setMultimediaFileChunk(bytearray);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                multimediaFile.setMultimediaFileName(timeStamp + ".mp4");
                ArrayList<MultimediaFile> files = new ArrayList<>();

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("vidd", "mpoulo");
            }

        }
    }

    private void sendMediaToBroker(byte[] media, String typeOfMedia){
        Publisher publisher = ((ConnectionService) this.getApplication()).getPublisher();
        publisher.push(topic, media, typeOfMedia);
    }

    private void sendMessage(String message) {
        ((ConnectionService) this.getApplication()).getPublisher().sendMessage(topic, message);
    }

    private void checkForMessages() {
        int difference = ((ConnectionService) this.getApplication()).showConversation(topic);

        if (difference > 0) {

            ArrayList<String> newMessages = ((ConnectionService) this.getApplication()).getLastMessages(topic, difference);
            for (String message : newMessages) {
                adapter.add(message);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void loopInAnotherThread() {
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                checkForMessages();
                            }
                        });
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void showAlertDialog(String type) {
        AlertDialog.Builder builderDialog;
        AlertDialog alertDialog;
        builderDialog = new AlertDialog.Builder(getApplicationContext());
        View layout;

        if(type.equals("v")){
            layout = getLayoutInflater().inflate(R.layout.video_dialog,null);
            VideoView video = findViewById(R.id.video);
        }
        else{
            layout = getLayoutInflater().inflate(R.layout.image_dialog, null);
            ImageView image = findViewById(R.id.picture);
        }

        Button dialogBtn = findViewById(R.id.backBtn);

        builderDialog.setView(layout);
        alertDialog = builderDialog.create();
        alertDialog.show();

        //Click on "back" button
        dialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Dismiss Dialog
                alertDialog.dismiss();
            }
        });
    }
}
