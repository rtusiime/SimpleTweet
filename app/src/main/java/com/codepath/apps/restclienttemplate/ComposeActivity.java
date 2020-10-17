package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {
    EditText etCompose;
    Button btnTweet;
    public static final  int MAX_TWEET_LENGTH =140;
    //add reference to twitter client
    TwitterClient client;
    public static final String TAG ="ComposeActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);
        client = TwitterApp.getRestClient(this);
        btnTweet.setEnabled(false);

        //Set click Listener on Button
    btnTweet.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String tweetContent = etCompose.getText().toString();
            if (tweetContent.isEmpty()) {

                return;
            }
            if (tweetContent.length() > MAX_TWEET_LENGTH) {

                return;
            }
            Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_SHORT).show();

            //Make an API call to Twitter to publish the tweet

            client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess to publish tweet");
                    try {
                        Tweet tweet = Tweet.fromJson(json.jsonObject);
                        Log.i(TAG, "Published Tweet says: " + tweet.body);
                        Intent intent = new Intent();
                        // set result code and bundle data for response
                        intent.putExtra("tweet", Parcels.wrap(tweet));
                        // closes the activity, pass data to parent

                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "onFailure to publish tweet");

                }
            });

        }
    });

    }
}