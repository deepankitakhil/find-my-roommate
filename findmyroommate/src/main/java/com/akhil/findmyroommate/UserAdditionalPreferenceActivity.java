package com.akhil.findmyroommate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by akhil on 12/27/2016.
 */
public class UserAdditionalPreferenceActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = UserAdditionalPreferenceActivity.class.getSimpleName();
    private final static String IS_ADDITIONAL_PREFERENCES_UPDATED = "IS_ADDITIONAL_PREFERENCES_UPDATED";
    private static final String ADDITIONAL_PREFERENCES = "ADDITIONAL_PREFERENCES";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_additional_peferences);
        updateAdditionalPreferenceOnUI();
        findViewById(R.id.send_additional_preferences).setOnClickListener(this);
        getIntent();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send_additional_preferences) {
            final EditText additionalPreferences = (EditText) findViewById(R.id.additional_preferences_body);
            String additionalPreferenceText = additionalPreferences.getText().toString();
            if (!TextUtils.isEmpty(additionalPreferenceText)) {
                Log.d(TAG, "saving additional preferences.");
                SharedPreferences preferences = this.getSharedPreferences("com.akhil.findmyroommate", Context.MODE_PRIVATE);
                preferences.edit().putString(ADDITIONAL_PREFERENCES, additionalPreferenceText).apply();
                preferences.edit().putBoolean(IS_ADDITIONAL_PREFERENCES_UPDATED, true).apply();
                //save additional preference in database.
            } else
                Log.d(TAG, "no additional preference");
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void updateAdditionalPreferenceOnUI() {
        SharedPreferences preferences = this.getSharedPreferences("com.akhil.findmyroommate", Context.MODE_PRIVATE);
        String additionalPreference = preferences.getString(ADDITIONAL_PREFERENCES, null);
        TextView additionalPreferenceText = (TextView) findViewById(R.id.additional_preferences_body);
        additionalPreferenceText.setText(additionalPreference);
    }
}
