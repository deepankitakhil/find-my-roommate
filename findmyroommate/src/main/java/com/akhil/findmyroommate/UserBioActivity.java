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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import util.KeyUtils;
import vo.ApplicationConstants;
import vo.User;

/**
 * Created by akhil on 12/27/2016.
 */
public class UserBioActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = UserBioActivity.class.getSimpleName();
    private static final String USER_BIO_TEXT = "USER_BIO_TEXT";
    private static final String IS_USER_BIO_UPDATED = "IS_USER_BIO_UPDATED";
    private static final String EMPTY = "";
    private static final String USER_EMAIL_ID = "USER_EMAIL_ID";
    private DatabaseReference databaseReference;


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

            SharedPreferences preferences = this.getSharedPreferences(ApplicationConstants.APPLICATION_PACKAGE_NAME.getValue(), Context.MODE_PRIVATE);

            final EditText userBio = (EditText) findViewById(R.id.user_bio_body);
            String userBioText = userBio.getText().toString();

            if (!TextUtils.isEmpty(userBioText)) {
                Log.d(TAG, "saving user bio." + userBioText);
                preferences.edit().putString(USER_BIO_TEXT, userBioText).apply();
                preferences.edit().putBoolean(IS_USER_BIO_UPDATED, true).apply();
                updateUserBioOnUI();
                updateUserBioOnDB();
            } else {
                preferences.edit().putString(USER_BIO_TEXT, EMPTY).apply();
                preferences.edit().putBoolean(IS_USER_BIO_UPDATED, false).apply();
                Log.d(TAG, "No user bio has been set up.");
            }
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void updateUserBioOnDB() {
        databaseReference = FirebaseDatabase.getInstance().getReference(ApplicationConstants.APPLICATION_DB_ROOT_REFERENCE.getValue());
        final SharedPreferences preferences = this.getSharedPreferences(ApplicationConstants.APPLICATION_PACKAGE_NAME.getValue(),
                Context.MODE_PRIVATE);
        String email = preferences.getString(USER_EMAIL_ID, null);
        final String key = KeyUtils.encodeFireBaseKey(email);
        databaseReference = databaseReference.child(key);
        databaseReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                final User mutableDataValue = mutableData.getValue(User.class);
                if (mutableDataValue == null) {
                    return Transaction.success(mutableData);
                }
                mutableDataValue.updateUserBio(preferences.getString(USER_BIO_TEXT, null));
                mutableData.setValue(mutableDataValue);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    private void updateUserBioOnUI() {
        SharedPreferences preferences = this.getSharedPreferences(ApplicationConstants.APPLICATION_PACKAGE_NAME.getValue(), Context.MODE_PRIVATE);
        String userBio = preferences.getString(USER_BIO_TEXT, null);
        TextView userShortBio = (TextView) findViewById(R.id.user_bio_body);
        userShortBio.setText(userBio);
    }
}
