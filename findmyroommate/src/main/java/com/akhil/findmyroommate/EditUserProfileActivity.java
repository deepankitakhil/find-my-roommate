package com.akhil.findmyroommate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import static android.text.Html.fromHtml;

/**
 * Created by akhil on 12/27/2016.
 */
public class EditUserProfileActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final String USER_EMAIL_ID = "USER_EMAIL_ID";
    private static final String ERROR = "text field can't be empty.";
    private static final String USER_MOBILE_NUMBER = "USER_MOBILE_NUMBER";
    private static final String USER_ADDRESS = "USER_ADDRESS";
    private static final String USER_NAME = "USER_NAME";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final String TAG = EditUserProfileActivity.class.getSimpleName();
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    SharedPreferences preferences;
    private AutoCompleteTextView mAutocompleteTextView;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @SuppressWarnings("deprecation")
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            final Place place = places.get(0);
            final String addressLocation = place.getAddress().toString();
            mAutocompleteTextView.setText(fromHtml(addressLocation));
            preferences.edit().putString(USER_ADDRESS, addressLocation).apply();
        }
    };

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item != null ? item.placeId : null);
            Log.i(TAG, "Selected: " + (item != null ? item.description : null));
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Fetching details for ID: " + (item != null ? item.placeId : null));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = this.getSharedPreferences("com.akhil.findmyroommate", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_profile);
        findViewById(R.id.send_user_profile_response).setOnClickListener(this);
        getIntent();
        addUserEmailIDInFeedbackBody();
        initializeLocationIdentifier();
        updateUserProfileOnUI();
    }

    private void initializeLocationIdentifier() {
        mGoogleApiClient = new GoogleApiClient.Builder(EditUserProfileActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");
    }

    private void updateUserProfileOnUI() {
        setTextValue(preferences, USER_NAME, R.id.edit_user_name);
        setTextValue(preferences, USER_MOBILE_NUMBER, R.id.edit_user_mobile_number);
        setTextValue(preferences, USER_ADDRESS, R.id.autoCompleteTextView);
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
        preferences.edit().putString(constField, value).apply();
    }

    private void addUserEmailIDInFeedbackBody() {
        String emailID = getEmailID();
        TextView view = (TextView) findViewById(R.id.user_email_id);
        String displayedContent = getString(R.string.feedback_email) + "\t" + emailID;
        view.setText(displayedContent);
    }

    private String getEmailID() {
        return preferences.getString(USER_EMAIL_ID, null);
    }
}
