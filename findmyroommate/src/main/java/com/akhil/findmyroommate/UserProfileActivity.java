package com.akhil.findmyroommate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by akhil on 12/27/2016.
 */
public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity);
        getIntent();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.edit_user_bio:
                intent = new Intent(this, UserBio.class);
                break;
            case R.id.edit_user_profile:
                intent = new Intent(this, EditUserProfile.class);
                break;
            case R.id.user_preference:
                intent = new Intent(this, UserPreferenceActivity.class);
                break;
            case R.id.user_additional_preference:
                intent = new Intent(this, UserAdditionalPreferenceActivity.class);
                break;
            case R.id.find_my_roommates:
                intent = new Intent(this, FindRoommate.class);
                break;
            case R.id.user_feedback:
                intent = new Intent(this, UserFeedBack.class);
                break;
            case R.id.user_logout:
                intent = new Intent(this, UserLogOut.class);
                break;
            default:
                intent = new Intent(this, DeleteUserAccount.class);
                break;
        }
        startActivity(intent);
        finish();
    }
}
