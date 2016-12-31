package com.akhil.findmyroommate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by akhil on 12/27/2016.
 */
public class UserBio extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = UserBio.class.getSimpleName();
    private static final String USER_BIO_TEXT = "USER_BIO_TEXT";
    private static final String IS_USER_BIO_UPDATED = "IS_USER_BIO_UPDATED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_bio);
        findViewById(R.id.save_user_bio).setOnClickListener(this);
        updateUserBioOnUI();
        getIntent();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.save_user_bio) {
            final EditText userBio = (EditText) findViewById(R.id.user_bio_body);
            String userBioText = userBio.getText().toString();
            if (!TextUtils.isEmpty(userBioText)) {
                Log.d(TAG, "saving user bio." + userBioText);
                SharedPreferences preferences = this.getSharedPreferences("com.akhil.findmyroommate", Context.MODE_PRIVATE);
                preferences.edit().putString(USER_BIO_TEXT, userBioText).apply();
                preferences.edit().putBoolean(IS_USER_BIO_UPDATED, true).apply();
                updateUserBioOnUI();
                //save additional bio in database.
            } else
                Log.d(TAG, "No user bio has been set up.");
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void updateUserBioOnUI() {
        SharedPreferences preferences = this.getSharedPreferences("com.akhil.findmyroommate", Context.MODE_PRIVATE);
        String userBio = preferences.getString(USER_BIO_TEXT, null);
        TextView userShortBio = (TextView) findViewById(R.id.user_bio_body);
        userShortBio.setText(userBio);
    }
}
