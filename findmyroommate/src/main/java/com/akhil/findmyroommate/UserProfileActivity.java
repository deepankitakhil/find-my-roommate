package com.akhil.findmyroommate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import vo.ApplicationConstants;

/**
 * Created by akhil on 12/27/2016.
 */
public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String USER_BIO_TEXT = "USER_BIO_TEXT";
    private static final String IS_USER_BIO_UPDATED = "IS_USER_BIO_UPDATED";
    private static final String USER_NAME = "USER_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity);
        updateUserInfo();
        getIntent();
    }

    @Override
    public void onClick(View view) {
        updateUserInfo();
        Intent intent;
        switch (view.getId()) {
            case R.id.edit_user_bio:
                intent = new Intent(this, UserBioActivity.class);
                break;
            case R.id.edit_user_profile:
                intent = new Intent(this, EditUserProfileActivity.class);
                break;
            case R.id.find_my_roommates:
                intent = new Intent(this, FindMyRoommateActivity.class);
                break;
            case R.id.user_feedback:
                intent = new Intent(this, UserFeedBackActivity.class);
                break;
            case R.id.user_logout:
                intent = new Intent(this, UserLogOutActivity.class);
                break;
            default:
                intent = new Intent(this, DeleteUserAccountActivity.class);
                break;
        }
        startActivity(intent);
        finish();
    }

    private void updateUserInfo() {

        SharedPreferences preferences = this.getSharedPreferences(ApplicationConstants.APPLICATION_PACKAGE_NAME.getValue(), Context.MODE_PRIVATE);
        String userBio = preferences.getString(USER_BIO_TEXT, null);
        boolean bioUpdated = preferences.getBoolean(IS_USER_BIO_UPDATED, false);
        if (bioUpdated) {
            TextView userShortBio = (TextView) findViewById(R.id.user_profile_short_bio);
            userShortBio.setText(userBio);
        }
        String userName = preferences.getString(USER_NAME, null);
        TextView userProfile = (TextView) findViewById(R.id.user_profile_name);
        userProfile.setText(userName);
    }
}
