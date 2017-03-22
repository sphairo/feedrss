package com.amco.dev.podcast.feedrssmanager.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amco.dev.podcast.feedrssmanager.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mEditText;
    private Button mSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        setSupportActionBar(mToolbar);
        mEditText.setText("http://feeds.soundcloud.com/users/soundcloud:users:213836126/sounds.rss");
    }

    private void setupViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mEditText = (EditText) findViewById(R.id.edit_text_feed_url);
        mSearchButton = (Button) findViewById(R.id.search_buttton);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.length() != 0) {
                    String feedUrl = mEditText.getText().toString();
                    if(isValidUrl(feedUrl)) {
                        DetailActivity.startActivity(getApplicationContext(), feedUrl);
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.no_url_available, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.no_url_available, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }
}