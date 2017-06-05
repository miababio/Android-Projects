package com.shadow.connect3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private final int YELLOW = 0; // Red = 1
    private final int UNPLAYED = 2; // Represent empty spot on the board

    private int activePlayer = YELLOW;
    private boolean gameActive = true;
    private int[] gamestate = new int[9];

    private int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                                        {0, 3 ,6}, {1, 4, 7}, {2, 5, 8},
                                        {0, 4, 8}, {2, 4, 6}};

    public void dropIn(View view)
    {
        ImageView counter = (ImageView)view; // Don't need to search for id; We just want the one clicked

        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if(gamestate[tappedCounter] == UNPLAYED && gameActive)
        {
            gamestate[tappedCounter] = activePlayer;

            counter.setTranslationY(-1000f); // Move counter above the screen

            // Put appropriate image (red or yellow)
            if (activePlayer == YELLOW)
                counter.setImageResource(R.drawable.yellow);
            else
                counter.setImageResource(R.drawable.red);

            activePlayer = ~activePlayer; // Let's see if this works ;)
            counter.animate().translationYBy(1000f).rotation(360).setDuration(300); // animate counter back to place

            // Check if winning positions are the same, and it's not unplayed
            for(int[] winningPosition : winningPositions)
            {
                if(gamestate[winningPosition[0]] == gamestate[winningPosition[1]] &&
                   gamestate[winningPosition[1]] == gamestate[winningPosition[2]] &&
                   gamestate[winningPosition[0]] != UNPLAYED)
                {
                    // Someone has won

                    gameActive = false;
                    String winner = gamestate[winningPosition[0]] == YELLOW? "Yellow" : "Red";

                    TextView winnerMessage = (TextView)findViewById(R.id.txtWinner);
                    winnerMessage.setText(winner + " has won!");

                    LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);
                    layout.setVisibility(View.VISIBLE);
                }
                else
                {
                    boolean gameIsOver = true;
                    for(int counterState : gamestate)
                    {
                        if(counterState == UNPLAYED)
                            gameIsOver = false;
                    }

                    if(gameIsOver)
                    {
                        TextView winnerMessage = (TextView)findViewById(R.id.txtWinner);
                        winnerMessage.setText("It's a draw!");

                        LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);
                        layout.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    public void playAgain(View view)
    {
        LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);
        layout.setVisibility(View.INVISIBLE);

        activePlayer = 0;
        Arrays.fill(gamestate, UNPLAYED);

        GridLayout board = (GridLayout)findViewById(R.id.gridBoard);
        for(int i = 0; i < board.getChildCount(); i++) //getChildCount() -> Tells you how many views are in the layout
        {
            ((ImageView)board.getChildAt(i)).setImageResource(0); // 0 = Empty Image
        }

        gameActive = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Arrays.fill(gamestate, UNPLAYED); // Quick way to set all values to the same in array (for loop behind the scenes)
    }
}
