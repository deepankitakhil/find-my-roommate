package com.akhil.findmyroommate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static java.lang.System.lineSeparator;

/**
 * Created by akhil on 12/27/2016.
 */
public class WelcomeUserActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String USER_NAME = "";
    RelativeLayout welcomeMessageLayout;
    RelativeLayout applicationContentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen_activity);
        welcomeMessageLayout = (RelativeLayout) findViewById(R.id.welcome_message_layout);
        applicationContentLayout = (RelativeLayout) findViewById(R.id.app_content_layout);
        findViewById(R.id.dismiss_welcome_box).setOnClickListener(this);
        findViewById(R.id.dismiss_get_started).setOnClickListener(this);
        addUserNameInIntroMessage();
    }

    private void addUserNameInIntroMessage() {
        SharedPreferences preferences = this.getSharedPreferences("com.akhil.findmyroommate", Context.MODE_PRIVATE);
        String userName = preferences.getString(USER_NAME, null);
        TextView view = (TextView) findViewById(R.id.welcome_title);
        String welcomeMessage = getString(R.string.welcome_title) + lineSeparator() + userName;
        view.setText(welcomeMessage);
    }

    public void dismissWelcomeMessageBox(View view) {
        welcomeMessageLayout.setVisibility(View.INVISIBLE);
        applicationContentLayout.setVisibility(View.VISIBLE);

    }

    public void dismissGetStarted(View view) {
        applicationContentLayout.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dismiss_welcome_box)
            dismissWelcomeMessageBox(view);
        if (view.getId() == R.id.dismiss_get_started)
            dismissGetStarted(view);
    }
}
