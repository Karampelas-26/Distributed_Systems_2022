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
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.distributedsystemsapp.R;
import com.example.distributedsystemsapp.domain.Message;
import com.example.distributedsystemsapp.domain.MultimediaFile;
import com.example.distributedsystemsapp.domain.Publisher;
import com.example.distributedsystemsapp.domain.UserNode;
import com.example.distributedsystemsapp.ui.services.ConnectionService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Queue;

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
                sendMessage(message);
                adapter.add(message);
            }
        });


        sendMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageOptionsDialog();
            }
        });

        loopInAnotherThread();
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
        choosePictureIntent.setType("image/*");
        choosePictureIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(choosePictureIntent, PICK_VIDEO);
    }

    private void takeVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, TAKE_VIDEO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("pushinnggg", "onActivityResult: i m in");
        if (requestCode == TAKE_PICTURE) {
            media = null;
            Bundle extras = data.getExtras();

            media = (Bitmap) extras.get("data");

//            Uri imageUri = data.getData();
            //                InputStream inputStream = getApplication().getContentResolver().openInputStream(imageUri);
//                media = BitmapFactory.decodeStream(inputStream);
//                image.setImageBitmap(media);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            media.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytearray = stream.toByteArray();
            media.recycle();
            sendMediaToBroker(bytearray);
            MultimediaFile multimediaFile = new MultimediaFile();
            multimediaFile.setMultimediaFileChunk(bytearray);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            multimediaFile.setMultimediaFileName(timeStamp + ".jpg");
            ArrayList<MultimediaFile> files = new ArrayList<>();
//            Message message = new Message();
//            message.setFiles(files);
//            String name = ((ConnectionService) this.getApplication()).getName();
//            message.setName(new ProfileName(name));

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
                sendMediaToBroker(bytearray);
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
                sendMediaToBroker(bytearray);
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
            video = null;
            Uri videoUri = data.getData();

            String path = getRealPathFromURI(this, videoUri);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try {
                FileInputStream fis = new FileInputStream(new File(path));
                byte[] buf = new byte[1024];
                int n;
                while (-1 != (n = fis.read(buf)))
                    baos.write(buf, 0, n);

                byte[] bytearray = baos.toByteArray();


                sendMediaToBroker(bytearray);
                MultimediaFile multimediaFile = new MultimediaFile();
                multimediaFile.setMultimediaFileChunk(bytearray);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                multimediaFile.setMultimediaFileName(timeStamp + ".mp4");
                ArrayList<MultimediaFile> files = new ArrayList<>();

            } catch (IOException e) {
                e.printStackTrace();
            }





        }
    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



    private void sendMediaToBroker(byte[] media){
        Publisher publisher = ((ConnectionService) this.getApplication()).getPublisher();
        publisher.push(topic, media, ".jpg");
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
}
