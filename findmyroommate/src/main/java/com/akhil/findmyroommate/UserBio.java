package com.akhil.findmyroommate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.EditText;

/**
 * Created by akhil on 12/27/2016.
 */
public class UserBio extends AppCompatActivity implements SearchView.OnClickListener {
    public static final String PARAMETER = "";
    public static final String DEFAULT_USER_BIO = "tell us something about yourself";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_bio);
        getIntent();

    }

    @Override
    public void onClick(View view) {
        if (R.id.edit_user_bio == view.getId()) {
            EditText inputTxt = (EditText) findViewById(R.id.edit_user_bio);
            String userBio = inputTxt.getText().toString();
            Intent intent = new Intent(this, UserProfileActivity.class);
            intent.putExtra(PARAMETER, userBio != null ? userBio : DEFAULT_USER_BIO);
            startActivity(intent);
            finish();
        }

    }
}
