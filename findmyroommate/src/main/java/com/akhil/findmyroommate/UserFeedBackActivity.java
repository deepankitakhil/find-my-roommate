package com.akhil.findmyroommate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import static java.lang.System.lineSeparator;

/**
 * Created by akhil on 12/27/2016.
 */
public class UserFeedBackActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MESSAGE_RFC822 = "message/rfc822";
    private static final String ADMIN_EMAIL_ID = "deepankitakhil@gmail.com";
    private static final String ERROR = "Feedback text can't be empty.";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_EMAIL_ID = "USER_EMAIL_ID";
    private static final String FEEDBACK_ON_APP = "Feedback on Find My Roommate";
    private static final String TAG = UserFeedBackActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_feedback);
        findViewById(R.id.send_feedback_response).setOnClickListener(this);
        getIntent();
        addUserNameInFeedbackBody();
        addUserEmailIDInFeedbackBody();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send_feedback_response) {
            final EditText feedbackField = (EditText) findViewById(R.id.feedback_body);
            String feedback = feedbackField.getText().toString();
            if (TextUtils.isEmpty(feedback)) {
                feedbackField.setError(ERROR);
                return;
            }

            final Spinner feedbackSpinner = (Spinner) findViewById(R.id.spinner_feedback_type);
            String feedbackType = feedbackSpinner.getSelectedItem().toString();

            String emailID = getEmailID();
            sendEmail(emailID, feedback, feedbackType);

            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void sendEmail(String emailID, String feedback, String feedbackType) {
        if (isMailClientPresent(this)) {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.setType(MESSAGE_RFC822);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailID, ADMIN_EMAIL_ID});
            email.putExtra(Intent.EXTRA_SUBJECT, FEEDBACK_ON_APP);
            email.putExtra(Intent.EXTRA_TEXT, feedback + lineSeparator() + feedbackType);
            startActivity(Intent.createChooser(email, "Send Email."));
            finish();
        } else {
            Log.d(TAG, "No email ID provider installed. System will not be able to send email!");
        }
    }

    private void addUserNameInFeedbackBody() {
        SharedPreferences preferences = this.getSharedPreferences("com.akhil.findmyroommate", Context.MODE_PRIVATE);
        String userName = preferences.getString(USER_NAME, null);
        TextView view = (TextView) findViewById(R.id.user_name);
        String displayedContent = getString(R.string.feedback_name) + "\t" + userName;
        view.setText(displayedContent);
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

    private boolean isMailClientPresent(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(MESSAGE_RFC822);
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, 0);
        if (list.size() == 0)
            return false;
        else
            return true;
    }
}
