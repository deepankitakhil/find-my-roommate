package com.akhil.findmyroommate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by akhil on 12/27/2016.
 */
public class EditUserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String USER_EMAIL_ID = "USER_EMAIL_ID";
    private static final String ERROR = "text field can't be empty.";
    private static final String USER_MOBILE_NUMBER = "USER_MOBILE_NUMBER";
    private static final String USER_ADDRESS = "USER_ADDRESS";
    private static final String USER_NAME = "USER_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_profile);
        findViewById(R.id.send_user_profile_response).setOnClickListener(this);
        getIntent();
        addUserEmailIDInFeedbackBody();
        updateUserProfileOnUI();
    }

    private void updateUserProfileOnUI() {
        SharedPreferences preferences = this.getSharedPreferences("com.akhil.findmyroommate", Context.MODE_PRIVATE);
        setTextValue(preferences, USER_NAME, R.id.edit_user_name);
        setTextValue(preferences, USER_MOBILE_NUMBER, R.id.edit_user_mobile_number);
        setTextValue(preferences, USER_ADDRESS, R.id.edit_user_profile_address);
        preferences.edit().putString(USER_NAME, preferences.getString(USER_NAME, null)).apply();
    }

    private void setTextValue(SharedPreferences preferences, String variableName, int id) {
        String value = preferences.getString(variableName, null);
        TextView textView = (TextView) findViewById(id);
        textView.setText(value);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send_user_profile_response) {

            final EditText firstNameField = getEditText(R.id.edit_user_name);
            final String firstName = getEditTextValue(firstNameField);
            assignValueInTextField(firstNameField, USER_NAME, firstName);

            final EditText mobileNumberField = getEditText(R.id.edit_user_mobile_number);
            final String mobileNumber = getEditTextValue(mobileNumberField);
            assignValueInTextField(mobileNumberField, USER_MOBILE_NUMBER, mobileNumber);

            final EditText userAddressField = getEditText(R.id.edit_user_profile_address);
            final String userAddress = getEditTextValue(userAddressField);
            assignValueInTextField(userAddressField, USER_ADDRESS, userAddress);

            LinearLayout viewGroup = (LinearLayout) findViewById(R.id.edit_user_profile_id);
            if (!checkBlankFields(viewGroup)) {
                Intent intent = new Intent(this, UserProfileActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private boolean checkBlankFields(ViewGroup viewGroup) {
        boolean isBlank = false;
        int count = viewGroup.getChildCount();
        for (int index = 0; index < count; index++) {
            View view = viewGroup.getChildAt(index);
            if (view instanceof EditText) {
                EditText edittext = (EditText) view;
                if (TextUtils.isEmpty(edittext.getText().toString())) {
                    edittext.setError(ERROR);
                    isBlank = true;
                }
            }
        }
        return isBlank;
    }

    @NonNull
    private String getEditTextValue(EditText editText) {
        return editText.getText().toString();
    }

    private EditText getEditText(int id) {
        return (EditText) findViewById(id);
    }

    private void assignValueInTextField(EditText editText, String constField, String value) {
        editText.setText(value);
        SharedPreferences preferences = this.getSharedPreferences("com.akhil.findmyroommate", Context.MODE_PRIVATE);
        preferences.edit().putString(constField, value).apply();
    }

    private void addUserEmailIDInFeedbackBody() {
        String emailID = getEmailID();
        TextView view = (TextView) findViewById(R.id.user_email_id);
        String displayedContent = getString(R.string.feedback_email) + "\t" + emailID;
        view.setText(displayedContent);
    }

    private String getEmailID() {
        SharedPreferences preferences = this.getSharedPreferences("com.akhil.findmyroommate", Context.MODE_PRIVATE);
        return preferences.getString(USER_EMAIL_ID, null);
    }
}
