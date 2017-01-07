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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import util.ViewValidator;

import static android.text.Html.fromHtml;

/**
 * Created by akhil on 12/27/2016.
 */
public class FindMyRoommateActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = FindMyRoommateActivity.class.getSimpleName();
    private final static String IS_ADDITIONAL_PREFERENCES_UPDATED = "IS_ADDITIONAL_PREFERENCES_UPDATED";
    private static final String USER_ADDITIONAL_PREFERENCES = "USER_ADDITIONAL_PREFERENCES";
    private static final String USER_ADDRESS = "USER_ADDRESS";
    private static final String USER_SEX = "USER_SEX";
    private static final String USER_PROFESSION = "USER_PROFESSION";
    private static final String USER_DIETARY_PREFERENCES = "USER_DIETARY_PREFERENCES";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private SharedPreferences preferences;
    private RadioGroup groupRadio;
    private AutoCompleteTextView autoCompleteTextView;

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
            autoCompleteTextView.setText(fromHtml(addressLocation));
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

    private RadioGroup.OnCheckedChangeListener onRadioButtonClicked = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.same_address_location) {
                autoCompleteTextView.getText().clear();
                autoCompleteTextView.setText(preferences.getString(USER_ADDRESS, null));
                autoCompleteTextView.setEnabled(false);
            } else {
                autoCompleteTextView.setEnabled(true);
                autoCompleteTextView.setSelection(autoCompleteTextView.getText().length());
            }
        }
    };

    private void initializeLocationIdentifier() {
        mGoogleApiClient = new GoogleApiClient.Builder(FindMyRoommateActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .autoCompleteTextView);
        autoCompleteTextView.setThreshold(3);
        autoCompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        autoCompleteTextView.setAdapter(mPlaceArrayAdapter);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_my_roommates);
        preferences = this.getSharedPreferences("com.akhil.findmyroommate", Context.MODE_PRIVATE);
        populateDefaultAddress();
        initializeRadioGroupListeners();
        initializeLocationIdentifier();
        updateUserPreferenceOnUI();
        findViewById(R.id.find_my_roommates).setOnClickListener(this);
        getIntent();
    }

    private void initializeRadioGroupListeners() {
        groupRadio = (RadioGroup) findViewById(R.id.address_location);
        groupRadio.setOnCheckedChangeListener(onRadioButtonClicked);
    }

    @Override
    public void onClick(View view) {
        LinearLayout viewGroup = (LinearLayout) findViewById(R.id.find_my_roommates_view);
        if (view.getId() == R.id.find_my_roommates) {
            final EditText additionalPreferences = (EditText) findViewById(R.id.additional_preferences_body);
            String additionalPreferenceText = additionalPreferences.getText().toString();
            if (!ViewValidator.isFieldBlank(viewGroup) && !ViewValidator.isSpinnerValueSetToDefault(viewGroup)) {
                if (!TextUtils.isEmpty(additionalPreferenceText)) {
                    Log.d(TAG, "saving additional preferences.");
                    preferences.edit().putString(USER_ADDITIONAL_PREFERENCES, additionalPreferenceText).apply();
                    preferences.edit().putBoolean(IS_ADDITIONAL_PREFERENCES_UPDATED, true).apply();
                    //queryDatabaseToFetchMatchedUsers();
                } else
                    Log.d(TAG, "no additional preference");
                Intent intent = new Intent(this, UserProfileActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void updateUserPreferenceOnUI() {
        setTextValue(preferences, USER_ADDRESS, R.id.autoCompleteTextView);
        setTextValue(preferences, USER_ADDITIONAL_PREFERENCES, R.id.additional_preferences_body);
        setSpinnerTextValue(preferences, USER_SEX, R.id.spinner_sex);
        setSpinnerTextValue(preferences, USER_PROFESSION, R.id.spinner_profession);
        setSpinnerTextValue(preferences, USER_DIETARY_PREFERENCES, R.id.dietary_preference);

    }

    private void setTextValue(SharedPreferences preferences, String variableName, int id) {

        String value = preferences.getString(variableName, null);
        TextView textView = (TextView) findViewById(id);
        textView.setText(value);
    }

    private void setSpinnerTextValue(SharedPreferences preferences, String variableName, int id) {

        int value = preferences.getInt(variableName, -1);
        Spinner spinner = (Spinner) findViewById(id);
        spinner.setSelection(value);
    }

    private void populateDefaultAddress() {
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setText(preferences.getString(USER_ADDRESS, null));
        autoCompleteTextView.setEnabled(false);
    }
}

