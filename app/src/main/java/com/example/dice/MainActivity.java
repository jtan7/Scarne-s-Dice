package com.example.dice;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;
import java.util.Random;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private int userOverallScore = 0;
    private int userTurnScore = 0;
    private int computerOverallScore = 0;
    private int computerTurnScore = 0;
    private int winScore = 10; // score needed to win

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button_roll = (Button) findViewById(R.id.button_roll);
        button_roll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int rollResult = roll();
                userTurn(rollResult);
            }
        });
    }

    public int roll() {
        // randomly select a dice value
        Random rand = new Random();
        int randomNum = rand.nextInt(6) + 1;

        // update the display to reflect the rolled value
        updateImageDisplay(randomNum);
        Log.d("randomNum", Integer.toString(randomNum));

        return randomNum;
    }

    public void userTurn(int rollResult) {
        final TextView score_message = (TextView) findViewById(R.id.score_message);
        if (rollResult == 1) {
            userTurnScore = 0;
            score_message.setText("Your score: " + userOverallScore + " computer score: "
                    + computerOverallScore + " your turn score: X");
            computerTurn();
        } else {
            userTurnScore += rollResult;
            score_message.setText("Your score: " + userOverallScore + " computer score: "
                    + computerOverallScore + " your turn score: " + userTurnScore);

        }
    }

    public void reset(View view) {
        userOverallScore = 0;
        userTurnScore = 0;
        computerOverallScore = 0;
        computerTurnScore = 0;
        final TextView score_message = (TextView) findViewById(R.id.score_message);
        score_message.setText("New Game\nYour score: 0 Computer score: 0");

        final Button button_roll = (Button) findViewById(R.id.button_roll);
        final Button button_hold = (Button) findViewById(R.id.button_hold);
        button_roll.setClickable(true);
        button_hold.setClickable(true);
    }

    public void hold(View view) {
        userOverallScore += userTurnScore;
        userTurnScore = 0;
        final TextView score_message = (TextView) findViewById(R.id.score_message);
        score_message.setText("Your score: " + userOverallScore + " Computer score: " +
                computerOverallScore);
        computerTurn();

        checkGameOver();
    }

    public void checkGameOver() {
        // if user or computer overall score is 100 or greater, game over
        final TextView score_message = (TextView) findViewById(R.id.score_message);
        final Button button_roll = (Button) findViewById(R.id.button_roll);
        final Button button_hold = (Button) findViewById(R.id.button_hold);
        if (userOverallScore < winScore && computerOverallScore < winScore) {
            return;
        }
        else if (userOverallScore >= winScore) {
            score_message.setText("You win!\nYour score: " + userOverallScore + " Computer score: " +
                    computerOverallScore);
        } else {
            score_message.setText("Computer wins!\nYour score: " + userOverallScore +
                    " Computer score: " + computerOverallScore);
        }
        button_roll.setClickable(false);
        button_hold.setClickable(false);
    }
    public void updateImageDisplay(int displayNum) {
        final ImageView image_dice = (ImageView) findViewById(R.id.image_dice);
        switch(displayNum){
            case 1:
                image_dice.setImageResource(R.drawable.dice1);
                break;
            case 2:
                image_dice.setImageResource(R.drawable.dice2);
                break;
            case 3:
                image_dice.setImageResource(R.drawable.dice3);
                break;
            case 4:
                image_dice.setImageResource(R.drawable.dice4);
                break;
            case 5:
                image_dice.setImageResource(R.drawable.dice5);
                break;
            case 6:
                image_dice.setImageResource(R.drawable.dice6);
                break;
        }
    }

    public void computerTurn() {
        final Button button_roll = (Button) findViewById(R.id.button_roll);
        final Button button_hold = (Button) findViewById(R.id.button_hold);
        button_roll.setClickable(false);
        button_hold.setClickable(false);
        final TextView score_message = (TextView) findViewById(R.id.score_message);

        final Handler h = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                int rollResult = roll();

                if (rollResult == 1) {
                    computerTurnScore = 0;
                    score_message.setText("Your score: " + userOverallScore + " computer score: "
                            + computerOverallScore + " Computer rolled a one");
                    Log.d("score_message", score_message.getText().toString());
                } else {
                    computerTurnScore += rollResult;
                    score_message.setText("Your score: " + userOverallScore + " computer score: "
                            + computerOverallScore + " Computer holds: " + computerTurnScore);
                    Log.d("score_message", score_message.getText().toString());
                }

                if (rollResult != 1 && computerTurnScore < 20) {
                    h.postDelayed(this, 1000);
                } else {
                    computerOverallScore += computerTurnScore;
                    score_message.setText("Your score: " + userOverallScore + " computer score: "
                            + computerOverallScore);

                    computerTurnScore = 0;
                    button_roll.setClickable(true);
                    button_hold.setClickable(true);

                    checkGameOver();
                }
            }
        };
        h.postDelayed(run, 1000);
    }
}
