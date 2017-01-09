package com.akhil.findmyroommate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vo.SearchResult;
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
        Bundle bundle = getIntent().getExtras();
        ArrayList<User> searchResult = bundle.getParcelableArrayList(SEARCH_RESULT);
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
        button.setText("Contact Users");
        button.setOnClickListener(this);
        layout.addView(button);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
        finish();
    }
}
