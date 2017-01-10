package com.akhil.findmyroommate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vo.ApplicationConstants;
import vo.SearchResult;
import vo.User;

/**
 * Created by akhil on 1/9/2017.
 */
public class UserSearchResult extends AppCompatActivity implements View.OnClickListener {

    private static final String SEARCH_RESULT = "searchResult";
    private static final String CONTACT_USERS = "Contact Users";
    private static final String SUCCESS_MESSAGE = "Message Sent successfully to ";
    private static final String FAILURE_MESSAGE = "Failed to send message to ";
    private static final String USER_NAME = "USER_NAME";
    private static final String MESSAGE = " is looking out for a room mate. Interested? Don't hesitate to call user and " +
            "see if its a good fit";
    private ArrayList<User> searchResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_search_result);
        Bundle bundle = getIntent().getExtras();
        searchResult = bundle.getParcelableArrayList(SEARCH_RESULT);
        List<SearchResult> displaySearchResultList = new ArrayList<>();
        for (User user : searchResult)
            displaySearchResultList.add(new SearchResult(user.getName(), user.getProfession(), user.getUserBio(),
                    user.getSex(), user.getDietaryPreference(), user.getAddress(), user.getAdditionalPreferences()));
        final ViewGroup layout = (ViewGroup) findViewById(R.id.user_search_result);

        for (int index = 0; index < displaySearchResultList.size(); index++) {
            TextView textView = new TextView(this);
            textView.setText(displaySearchResultList.get(0).toString());
            layout.addView(textView);
        }

        Button button = new Button(this);
        button.setText(CONTACT_USERS);
        button.setOnClickListener(this);
        layout.addView(button);
    }

    @Override
    public void onClick(View view) {
        sendMessageToMatchedUsers();
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendMessageToMatchedUsers() {
        for (int index = 0; index < searchResult.size(); index++) {
            final User user = searchResult.get(index);
            final String textMessage = getUserName() + MESSAGE;
            try {
                final SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(user.getPhoneNumber(), null, textMessage, null, null);
                Toast.makeText(UserSearchResult.this, SUCCESS_MESSAGE + user.getName(),
                        Toast.LENGTH_LONG).show();
            } catch (Exception exception) {
                Toast.makeText(UserSearchResult.this, FAILURE_MESSAGE + user.getName(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getUserName() {
        final SharedPreferences sharedPreferences =
                this.getSharedPreferences(ApplicationConstants.APPLICATION_PACKAGE_NAME.getValue(), Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, null);
    }
}