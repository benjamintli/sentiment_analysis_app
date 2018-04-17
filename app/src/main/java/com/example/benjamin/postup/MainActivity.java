package com.example.benjamin.postup;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.Toast;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public Double accuracy;
    public String polarity;
    EditText searchItem;
    Button searchButton;
    Heroku heroku;
    ProgressBar progressBar;
    TableLayout output;

    public void mlOutputAlert(String polarity, double accuracy){
        AlertDialog.Builder output = new AlertDialog.Builder(MainActivity.this);
        output.setTitle("Sentiment Output")
                .setMessage("Accuracy: " + accuracy + "\nPolarity: " + polarity)
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            searchItem.setText("");
                        }
                    });
        AlertDialog dialog = output.create();
        dialog.show();
        Typeface font = Typeface.createFromAsset(getAssets(), "lato_regular.ttf"); //set font to something nicer
        TextView message = (TextView) dialog.getWindow().findViewById(android.R.id.message);
        message.setTypeface(font);
    }

    public void sendNetworkRequest(Post post) {
        String BASE_URL = "https://keras-sentiment-analysis.herokuapp.com/";
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        heroku = retrofit.create(Heroku.class);
        Call<Post> call = heroku.analyze(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                searchButton.setText(R.string.search);
                progressBar.setVisibility(View.GONE);
                textOutput(response.body().getPolarity(), response.body().getAcc());
                Toast.makeText(getBaseContext(), "connection successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                searchButton.setText(R.string.search);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getBaseContext(), "connection failed, try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void textOutput (String polarity, double accuracy) {
        Typeface font = Typeface.createFromAsset(getAssets(), "lato_regular.ttf"); //set font to something nicer
        String percent = String.format("%.2f", (accuracy * 100));
        TextView phrase = findViewById(R.id.phrase);
        TextView pol = findViewById(R.id.polarity);
        TextView acc = findViewById(R.id.accuracy);
        phrase.setTypeface(font);
        pol.setTypeface(font);
        acc.setTypeface(font);

        phrase.setText("Phrase: " + searchItem.getText().toString());
        pol.setText("Polarity: " + polarity);
        acc.setText("Accuracy: " + percent + "%");

        TableLayout output = findViewById(R.id.output);
        output.setVisibility(View.VISIBLE);
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        output.startAnimation(fadeIn);
        fadeIn.setDuration(500);
        fadeIn.setFillAfter(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.hide();
        TextView title = (TextView) findViewById(R.id.title);
        Typeface font = Typeface.createFromAsset(getAssets(), "lato_regular.ttf"); //set font to something nicer
		title.setTypeface(font);
        output = findViewById(R.id.output);
        output.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);

        searchItem = (EditText) findViewById(R.id.search_item);
		searchItem.setTypeface(font);

		searchButton = (Button) findViewById(R.id.search_button);
		searchButton.setTypeface(font);

		searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchButton.setText("");
                progressBar.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        searchItem.getWindowToken(), 0);
                Post post = new Post (searchItem.getText().toString());
                sendNetworkRequest(post);
            }
        });

		searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchItem.getText().toString().equals("Type a phrase to be analyzed"))
                    searchItem.setText("");
            }
        });

        searchItem.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if(i == KeyEvent.KEYCODE_ENTER) {
                        searchButton.callOnClick();
                        return true;
                    }
                }
                return false;
            }
        });



    }
}
