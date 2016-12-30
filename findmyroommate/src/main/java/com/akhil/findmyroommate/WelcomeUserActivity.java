package com.akhil.findmyroommate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.IdpResponse;

import static com.firebase.ui.auth.ui.ExtraConstants.EXTRA_IDP_RESPONSE;
import static java.lang.System.lineSeparator;

/**
 * Created by akhil on 12/27/2016.
 */
public class WelcomeUserActivity extends AppCompatActivity implements View.OnClickListener {

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
        Intent intent = getIntent();
        String userName = intent.getStringExtra(LoginActivity.USER_NAME);
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
