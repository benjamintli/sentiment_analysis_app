package com.example.benjamin.postup;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.annotations.Expose;

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
                mlOutputAlert(response.body().getPolarity(), response.body().getAcc());
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.hide();
        TextView title = (TextView) findViewById(R.id.title);
        Typeface font = Typeface.createFromAsset(getAssets(), "lato_regular.ttf"); //set font to something nicer
		title.setTypeface(font);

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
