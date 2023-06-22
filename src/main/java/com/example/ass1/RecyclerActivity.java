package com.example.ass1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ass1.DataClass.MathProblem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity {



    private TextView all_test;
    private SharedPreferences sharedPreferences ;

    private RecyclerView recyclerView;
    private QuestionsAdapter questionsAdapter;

    Gson gson ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        all_test = findViewById(R.id.all_test);
        gson = new Gson();

        recyclerView = findViewById(R.id.my_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String storedJson = sharedPreferences.getString("questionList", null);
        Toast.makeText(this, ""+sharedPreferences, Toast.LENGTH_SHORT).show();
        if (storedJson != null) {
            Type type = new TypeToken<List<MathProblem>>() {}.getType();
            List<MathProblem> retrievedList = gson.fromJson(storedJson, type);

            questionsAdapter = new QuestionsAdapter(retrievedList);
            // Use the retrievedList as needed
        } else {
            // Handle case when no data is stored
        }

        recyclerView.setAdapter(questionsAdapter);


    }
}