package com.example.benjamin.postup;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
                mlOutputAlert(response.body().getPolarity(), response.body().getAcc());
                Toast.makeText(getBaseContext(), "connection successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(getBaseContext(), "connection failed", Toast.LENGTH_SHORT).show();

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

        searchItem = (EditText) findViewById(R.id.search_item);
		searchItem.setTypeface(font);

		searchButton = (Button) findViewById(R.id.search_button);
		searchButton.setTypeface(font);

		searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post post = new Post (searchItem.getText().toString());
                sendNetworkRequest(post);
            }
        });

		searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
