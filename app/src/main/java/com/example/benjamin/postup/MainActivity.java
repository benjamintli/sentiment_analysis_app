package com.example.benjamin.postup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Typeface;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.hide();
        TextView title = (TextView) findViewById(R.id.title);
        Typeface font = Typeface.createFromAsset(getAssets(), "lato_regular.ttf"); //set font to something nicer
		title.setTypeface(font);

		final EditText searchItem = (EditText) findViewById(R.id.search_item);
		searchItem.setTypeface(font);

		final Button searchButton = (Button) findViewById(R.id.search_button);
		searchButton.setTypeface(font);

		searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
