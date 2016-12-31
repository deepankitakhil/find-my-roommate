package com.akhil.findmyroommate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by akhil on 12/27/2016.
 */
public class UserAdditionalPreferenceActivity extends AppCompatActivity implements SearchView.OnClickListener {

    private static final String TAG = UserAdditionalPreferenceActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_additional_peferences);
        findViewById(R.id.send_additional_preferences).setOnClickListener(this);
        getIntent();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send_additional_preferences) {
            final EditText feedbackField = (EditText) findViewById(R.id.additional_preferences_body);
            String feedback = feedbackField.getText().toString();
            if (!TextUtils.isEmpty(feedback)) {
                Log.d(TAG, "saving additional preferences.");
                //save additional preference in database.
            } else
                Log.d(TAG, "no additional preference");
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
