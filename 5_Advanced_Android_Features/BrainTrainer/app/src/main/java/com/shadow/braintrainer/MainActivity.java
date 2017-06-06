package com.shadow.braintrainer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button answer, tl, tr, bl, br, playAgain;
    TextView txtTimer, equation, txtResults, txtScore;
    Random random;
    ArrayList<Integer> answers = new ArrayList<>();
    int btnWithCorrectAnswer;
    int correct = 0, total = 0;
    boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtTimer = (TextView)findViewById(R.id.txtTimer);
        txtResults = (TextView)findViewById(R.id.txtResults);
        txtScore = (TextView)findViewById(R.id.txtCorrect);
        equation = (TextView)findViewById(R.id.txtProblem);
        random = new Random();
        tl = (Button)findViewById(R.id.btnTopLeft);
        tr = (Button)findViewById(R.id.btnTopRight);
        bl = (Button)findViewById(R.id.btnBottomLeft);
        br = (Button)findViewById(R.id.btnBottomRight);
        playAgain = (Button)findViewById(R.id.btnPlayAgain);
    }

    public void setupGame(View view)
    {
        gameOver = false;
        correct = 0;
        total = 0;
        txtScore.setText("0/0");
        txtResults.setVisibility(View.INVISIBLE);

        Button startButton = (Button)findViewById(R.id.btnStart);
        LinearLayout linear = (LinearLayout)findViewById(R.id.linearGame);
        GridLayout grid = (GridLayout)findViewById(R.id.gridGame);
        startButton.setVisibility(View.INVISIBLE);
        linear.setVisibility(View.VISIBLE);
        grid.setVisibility(View.VISIBLE);

        generateProblem();

        new CountDownTimer(30 * 1000 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimer((int)(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                txtTimer.setText("0s");
                txtResults.setText("Your Total Score: " + correct + "/" + total);
                playAgain.setVisibility(View.VISIBLE);
                gameOver = true;
            }
        }.start();
    }

    private void updateTimer(int secondsLeft)
    {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft % 60; // Or you could do progress - minutes * 60 (Rob way)
        txtTimer.setText(seconds + "s");
    }

    private void generateProblem()
    {
        answers.clear();
        int first = random.nextInt(20 - 1 + 1) + 1;
        int second = random.nextInt(20 - 1 + 1) + 1;
        btnWithCorrectAnswer = random.nextInt(4);
        equation.setText(first + " + " + second);

        for(int i = 0; i < 4; i++)
        {
            if(i == btnWithCorrectAnswer)
                answers.add(first + second);
            else
            {
                int badAnswer = random.nextInt(40) + 1;
                while(badAnswer == first + second || answers.contains(badAnswer))
                    badAnswer = random.nextInt(40) + 1;
                answers.add(badAnswer);
            }
        }

        tl.setText(Integer.toString(answers.get(0)));
        tr.setText(Integer.toString(answers.get(1)));
        bl.setText(Integer.toString(answers.get(2)));
        br.setText(Integer.toString(answers.get(3)));

        // Make this work later instead of doing it Rob's way
        /*for(int i = 0; i < grid.getChildCount(); i++)
        {
            View viewButton = grid.getChildAt(i);
            if(Integer.parseInt(viewButton.getTag().toString()) == btnWithCorrectAnswer)
            {
                Button btn = (Button)viewButton;
                btn.setText(Integer.toString(first + second));
            }
        }*/

        /*equation.setText(first + " + " + second);
        answer = (Button)findViewById(R.id.btnTopRight);
        answer.setText(Integer.toString(first + second));*/

    }

    public void checkAnswer(View view)
    {
        if(!gameOver)
        {
            if(view.getTag().toString().equals(Integer.toString(btnWithCorrectAnswer)))
            {
                correct++;
                txtResults.setText("Correct!");
                txtResults.setVisibility(View.VISIBLE);
            }
            else
            {
                txtResults.setText("Incorrect!");
                txtResults.setVisibility(View.VISIBLE);
            }
            total++;
            txtScore.setText(correct + "/" + total);
            generateProblem();
        }
    }
}
