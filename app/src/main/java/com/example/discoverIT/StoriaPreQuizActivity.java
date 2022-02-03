package com.example.discoverIT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StoriaPreQuizActivity extends AppCompatActivity {

    private String TAG = "StoriaPreQuiz";

    private Button startquiz;
    private TextView storia;
    private String selectedQrCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storia_pre_quiz);

        storia = (TextView) findViewById(R.id.storia);
        // get Topic Name and User Name from StartActivity via Intent
        final String getTopicName = getIntent().getStringExtra("selectedTopic");

        // set Topic Name to TextView
        extractInfoQrCode info = new extractInfoQrCode(getTopicName);
        Log.d(TAG, "info: "+info.findProvince(getTopicName));
        History h = new History(getTopicName);
        storia.setText(h.findHistory(getTopicName));

        startquiz = (Button) findViewById(R.id.startquiz);
        startquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedQrCode = getTopicName;
                // Create an Object of Intent to open quiz questions screen
                final Intent intent = new Intent(StoriaPreQuizActivity.this, MainQuiz.class);

                //put user entered name and selected topic name to intent for use in next activity
                intent.putExtra("selectedTopic", selectedQrCode);

                // call startActivity to open next activity along with data(userName, selectedTopicName)
                startActivity(intent);

                finish(); // finish (destroy) this activity
            }
        });

    }
}