package com.akhil.findmyroommate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

import vo.User;

/**
 * Created by akhil on 1/9/2017.
 */
public class UserSearchResult extends AppCompatActivity implements View.OnClickListener {

    private static final String SEARCH_RESULT = "searchResult";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_search_result);
        findViewById(R.id.send_message_to_selected_user).setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        ArrayList<User> searchResult = bundle.getParcelableArrayList(SEARCH_RESULT);
        for (User user : searchResult) {
            //print user in text box
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
        finish();
    }
}
