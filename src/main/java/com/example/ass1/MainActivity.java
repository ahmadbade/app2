package com.example.ass1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.ass1.Data.MockData;
import com.example.ass1.DataClass.MathProblem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    private List<MathProblem> mathProblems;
    private int currentProblemIndex = 0;

    private TextView problemTextView;
    private RadioGroup solutionsRadioGroup;
    private Button checkButton;
    private TextView solutionTextView;
    private Button nextButton;

    private  int number_qustion = 0 ;

    SharedPreferences sharedPreferences ;
    private List<MathProblem> questionList;
    private String json ;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        questionList = new ArrayList<>(); // Replace with your list of Question objects
        gson = new Gson();





        mathProblems = MockData.getMathProblems();

        problemTextView = findViewById(R.id.problemTextView);
        solutionsRadioGroup = findViewById(R.id.choicesRadioGroup);
        checkButton = findViewById(R.id.checkButton);
        solutionTextView = findViewById(R.id.solutionTextView);
        nextButton = findViewById(R.id.nextButton);

        showProblem();
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextProblem();
            }
        });



    }

    private void showProblem() {

        MathProblem problem = mathProblems.get(currentProblemIndex);
        problemTextView.setText(problem.getProblem());
        solutionsRadioGroup.removeAllViews();
        List<String> solutions = new ArrayList<>();
        solutions.add(problem.getSolution());

        questionList.add(problem);

        while (solutions.size() < 4) {
            String randomSolution = getRandomSolution();
            if (!solutions.contains(randomSolution)) {
                solutions.add(randomSolution);
            }
        }
        Collections.shuffle(solutions);
        for (int i = 0; i < solutions.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(solutions.get(i));
            solutionsRadioGroup.addView(radioButton);
        }
        solutionTextView.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
    }

    private String getRandomSolution() {
        int operand1 = (int) (Math.random() * 10);
        int operand2 = (int) (Math.random() * 10);
        String operator = Math.random() < 0.5 ? "+" : "-";
        int solution = operator.equals("+") ? operand1 + operand2 : operand1 - operand2;
        return String.valueOf(solution);
    }

    private void checkAnswer() {
        MathProblem problem = mathProblems.get(currentProblemIndex);
        RadioButton selectedRadioButton = findViewById(solutionsRadioGroup.getCheckedRadioButtonId());
        if (selectedRadioButton != null) {
            String selectedSolution = selectedRadioButton.getText().toString();
            if (selectedSolution.equals(problem.getSolution())) {
                solutionTextView.setText("Correct Answer!");
                solutionTextView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
            } else {
                solutionTextView.setText("Not Correct! The answer is " + problem.getSolution());
                solutionTextView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            }
            solutionTextView.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
            checkButton.setVisibility(View.GONE);
        }
    }

    private void showNextProblem() {
        number_qustion += 1 ;
        currentProblemIndex++;
        if (currentProblemIndex >= mathProblems.size()) {
            currentProblemIndex = 0;
        }
        showProblem();
        checkButton.setVisibility(View.VISIBLE);

        if (number_qustion > 5){
            addtoSharePref();

            Intent intent = new Intent(getApplicationContext(), RecyclerActivity.class);


            startActivity(intent);
        }
    }


    private void  addtoSharePref(){

        json = gson.toJson(questionList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("questionList", json);
        editor.apply();
    }
}

