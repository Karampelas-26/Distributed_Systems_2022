package com.example.distributedsystemsapp.ui.homepage;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.distributedsystemsapp.R;

import java.util.ArrayList;

public class HomepageModel extends AppCompatActivity implements HomepageView {

    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        ArrayList<String> arrayList = new ArrayList<String>();

        listView = (ListView) findViewById(R.id.listViewAllConversations);

        arrayList.add("antonis");
        arrayList.add("george");
        arrayList.add("marios");
        arrayList.add("sotiris");
        arrayList.add("nikos");
        arrayList.add("teodor");
        arrayList.add("antonis");
        arrayList.add("george");
        arrayList.add("marios");
        arrayList.add("sotiris");
        arrayList.add("nikos");
        arrayList.add("teodor");
        arrayList.add("antonis");
        arrayList.add("george");
        arrayList.add("marios");
        arrayList.add("sotiris");
        arrayList.add("nikos");
        arrayList.add("teodor");

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(adapter);
    }




}


