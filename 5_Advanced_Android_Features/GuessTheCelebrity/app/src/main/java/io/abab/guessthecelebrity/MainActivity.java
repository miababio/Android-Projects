package io.abab.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private LinkedHashMap<Bitmap, String> celebrities;
    private ArrayList<String> celebImg, celebName, answers;
    private Random random;
    private ImageView imageView;
    private Button buttonA, buttonB, buttonC, buttonD;
    private int correctAnswer;
    private String finalAnswer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        celebrities = new LinkedHashMap<>();
        celebImg = new ArrayList<>();
        celebName = new ArrayList<>();
        answers = new ArrayList<>();
        random = new Random();
        imageView = (ImageView)findViewById(R.id.imgCelebrity);
        buttonA = (Button)findViewById(R.id.buttonA);
        buttonB = (Button)findViewById(R.id.buttonB);
        buttonC = (Button)findViewById(R.id.buttonC);
        buttonD = (Button)findViewById(R.id.buttonD);
        DownloadTask task = new DownloadTask();
        ImageDownloader imageDownloader = new ImageDownloader();
        String result = null;
        try
        {
            result = task.execute("http://www.posh24.se/kandisar").get();
            String[] splitResult = result.split("<div class=\"sidebarContainer\""); //removes those extra 5 images from the end
            Pattern p = Pattern.compile("img src=\"(.*?)\"");
            Matcher m = p.matcher(splitResult[0]); // we want the first half of the split (everything but the 5 side images)
            while(m.find())
                celebImg.add(m.group(1));
            p = Pattern.compile("alt=\"(.*?)\"");
            m = p.matcher(splitResult[0]);
            while(m.find())
                celebName.add(m.group(1));
            /*for(int i = 0; i < 50; i++)
                Log.i("Content", "Img: " + celebImg.get(i) + ", Name: " + celebName.get(i));*/

            celebrities = imageDownloader.execute(celebImg).get();
            /*for(HashMap.Entry<Bitmap, String> entry : celebrities.entrySet())
            {
                Log.i("Key/Value", entry.getKey().toString() + ", " + entry.getValue());
            }*/
            generateCelebrity();

            /**
             * Maybe smarter idea, but rob chooses a random url from the celebImg Arraylist ( chosenCeleb = random.nextInt(celebImg.size()) ),
             * and then calls the imageDownloader.execute method (celebImage = imageDownloader.execute(celebImg.get(chosenCeleb)).get();
             *
             * Pros:
             *  - Calls to fetch the image depends on the number of times you want to play the game (could largely scale but doubt it)
             *  - Only fetching one image vs. 99 images all at once
             *
             * Cons:
             *  - Game could be slow, since after each question you call to get a new image, compared to slow beginning by fetching all 99 images,
             *    but then you have less lag for each question
             */


        }
        catch(InterruptedException | ExecutionException ex)
        {
            ex.printStackTrace();
        }

    }

    public void guessAnswer(View view)
    {
        if(view.getTag().toString().equals(Integer.toString(correctAnswer)))
            Toast.makeText(this, "That's Correct!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Incorrect! It was " + finalAnswer, Toast.LENGTH_SHORT).show();
        generateCelebrity();
    }

    public void generateCelebrity()
    {
        answers.clear();

        // get a random celeb
        List<Bitmap> keys = new ArrayList<>(celebrities.keySet());
        Bitmap randomKey = keys.get(random.nextInt(keys.size()));
        imageView.setImageBitmap(randomKey);
        String goodValue = celebrities.get(randomKey);
        finalAnswer = goodValue;

        correctAnswer = random.nextInt(4);
        for(int i = 0; i < 4; i++)
        {
            if(i == correctAnswer) {
                answers.add(goodValue);
            }
            else
            {
                randomKey = keys.get(random.nextInt(keys.size()));
                String badValue = celebrities.get(randomKey);
                while(badValue.equals(goodValue) || answers.contains(badValue))
                {
                    randomKey = keys.get(random.nextInt(keys.size()));
                    badValue = celebrities.get(randomKey);
                }
                answers.add(badValue);
            }
        }

        buttonA.setText(answers.get(0));
        buttonB.setText(answers.get(1));
        buttonC.setText(answers.get(2));
        buttonD.setText(answers.get(3));
    }

    public class DownloadTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            URL url;
            HttpURLConnection urlConnection = null;

            try
            {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = reader.readLine()) != null)
                {
                    if(line.contains("http://cdn.posh24.se/images/:profile/c/2696692")) continue; // 55. Benjamin Wahlgren Ingrosso is missing his photo; fix later
                    result.append(line);
//                    result.append("\n");
                }
                return result.toString();
            }
            catch(MalformedURLException ex)
            {
                ex.printStackTrace();
                return "Failed";
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
                return "Failed";
            }
            finally
            {
                urlConnection.disconnect();
            }
        }
    }

    public class ImageDownloader extends AsyncTask<ArrayList<String>, Void, LinkedHashMap<Bitmap, String>>
    {
        @Override
        protected LinkedHashMap<Bitmap, String> doInBackground(ArrayList<String>... urls) {
            LinkedHashMap<Bitmap, String> result = new LinkedHashMap<>();
            ArrayList<String> images = urls[0];
            try
            {
                for (int i = 0; i < images.size(); i++)
                {
                    URL url = new URL(images.get(i));
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    connection.disconnect();
                    result.put(bitmap, celebName.get(i));
                }
                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
}
