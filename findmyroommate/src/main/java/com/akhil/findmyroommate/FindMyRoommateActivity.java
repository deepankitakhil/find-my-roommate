package com.akhil.findmyroommate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import util.ViewValidator;
import vo.ApplicationConstants;
import vo.User;

import static android.text.Html.fromHtml;

/**
 * Created by akhil on 12/27/2016.
 */
public class FindMyRoommateActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, ValueEventListener {

    private static final String TAG = FindMyRoommateActivity.class.getSimpleName();
    private static final String USER_ADDITIONAL_PREFERENCES = "USER_ADDITIONAL_PREFERENCES";
    private static final String USER_ADDRESS = "USER_ADDRESS";
    private static final String USER_SEX = "USER_SEX";
    private static final String USER_PROFESSION = "USER_PROFESSION";
    private static final String USER_SEX_VALUE = "USER_SEX_VALUE";
    private static final String USER_PROFESSION_VALUE = "USER_PROFESSION_VALUE";
    private static final String USER_DIETARY_PREFERENCES = "USER_DIETARY_PREFERENCES";
    private static final String USER_DIETARY_PREFERENCES_VALUE = "USER_DIETARY_PREFERENCES_VALUE";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final String USER_DESIRED_LOCATION_PREFERENCE = "USER_DESIRED_LOCATION_PREFERENCE";
    private static final String USER_SEARCH_CRITERIA = "USER_SEARCH_CRITERIA";
    private static final String USER_SEARCH_CRITERIA_VALUE = "USER_SEARCH_CRITERIA_VALUE";
    private static final String ERROR_MESSAGE = "Google Places API connection failed with error code:";

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private SharedPreferences preferences;
    private RadioGroup groupRadio;
    private AutoCompleteTextView autoCompleteTextView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<User> filteredUserList;

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
            preferences.edit().putString(USER_DESIRED_LOCATION_PREFERENCE, addressLocation).apply();

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
                preferences.edit().putString(USER_DESIRED_LOCATION_PREFERENCE, preferences.getString(USER_ADDRESS, null)).apply();
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
                ERROR_MESSAGE +
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
        preferences = this.getSharedPreferences(ApplicationConstants.APPLICATION_PACKAGE_NAME.getValue(), Context.MODE_PRIVATE);
        populateDefaultAddress();
        initializeRadioGroupListeners();
        initializeLocationIdentifier();
        initializeFireBaseDB();
        updateUserPreferenceOnUI();
        databaseReference = databaseReference.child(ApplicationConstants.APPLICATION_DB_ROOT_REFERENCE.getValue());
        databaseReference.addValueEventListener(this);
        findViewById(R.id.find_my_roommates).setOnClickListener(this);
        getIntent();
    }

    private void initializeRadioGroupListeners() {
        groupRadio = (RadioGroup) findViewById(R.id.address_location);
        groupRadio.setOnCheckedChangeListener(onRadioButtonClicked);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.find_my_roommates) {
            LinearLayout viewGroup = (LinearLayout) findViewById(R.id.find_my_roommates_view);

            final EditText additionalPreferences = getEditText(R.id.additional_preferences_body);
            final String additionalPreferencesValue = getEditTextValue(additionalPreferences);
            assignValueInTextField(additionalPreferences, USER_ADDITIONAL_PREFERENCES, additionalPreferencesValue);

            final Spinner searchCriteria = getSpinnerText(R.id.search_criteria);
            preferences.edit().putString(USER_SEARCH_CRITERIA_VALUE, searchCriteria.getSelectedItem().toString()).apply();
            setSelectionIndex(searchCriteria, USER_SEARCH_CRITERIA, preferences);

            final Spinner dietaryPreference = getSpinnerText(R.id.dietary_preference);
            preferences.edit().putString(USER_DIETARY_PREFERENCES_VALUE, dietaryPreference.getSelectedItem().toString()).apply();
            setSelectionIndex(dietaryPreference, USER_DIETARY_PREFERENCES, preferences);

            final Spinner sex = getSpinnerText(R.id.spinner_sex);
            preferences.edit().putString(USER_SEX_VALUE, sex.getSelectedItem().toString()).apply();
            setSelectionIndex(sex, USER_SEX, preferences);

            final Spinner profession = getSpinnerText(R.id.spinner_profession);
            preferences.edit().putString(USER_PROFESSION_VALUE, profession.getSelectedItem().toString()).apply();
            setSelectionIndex(profession, USER_PROFESSION, preferences);

            if (!ViewValidator.isFieldBlank(viewGroup) && !ViewValidator.isSpinnerValueSetToDefault(viewGroup)) {
                queryDatabaseToFetchMatchedUsers();
                Intent intent = new Intent(this, UserProfileActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private Spinner getSpinnerText(int id) {
        return (Spinner) findViewById(id);
    }

    private void setSelectionIndex(Spinner spinner, String input, SharedPreferences preferences) {
        preferences.edit().putInt(input, spinner.getSelectedItemPosition()).apply();
    }

    private void assignValueInTextField(EditText editText, String constField, String value) {
        editText.setText(value);
        preferences.edit().putString(constField, value).apply();
    }

    @NonNull
    private String getEditTextValue(EditText editText) {
        return editText.getText().toString();
    }

    private EditText getEditText(int id) {
        return (EditText) findViewById(id);
    }

    private List<User> queryDatabaseToFetchMatchedUsers() {
        return filteredUserList;
    }

    private void initializeFireBaseDB() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void updateUserPreferenceOnUI() {
        setTextValue(preferences, USER_ADDRESS, R.id.autoCompleteTextView);
        setTextValue(preferences, USER_ADDITIONAL_PREFERENCES, R.id.additional_preferences_body);
        setSpinnerTextValue(preferences, USER_SEX, R.id.spinner_sex);
        setSpinnerTextValue(preferences, USER_PROFESSION, R.id.spinner_profession);
        setSpinnerTextValue(preferences, USER_DIETARY_PREFERENCES, R.id.dietary_preference);
        setSpinnerTextValue(preferences, USER_SEARCH_CRITERIA, R.id.search_criteria);
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

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        filteredUserList = new ArrayList<>();
        final String desiredSex = preferences.getString(USER_SEX_VALUE, null);
        final String desiredProfession = preferences.getString(USER_PROFESSION_VALUE, null);
        final String desiredDietaryPreference = preferences.getString(USER_DIETARY_PREFERENCES_VALUE, null);
        final String desiredLocationPreference = preferences.getString(USER_DESIRED_LOCATION_PREFERENCE, null);
        final String additionalPreference = preferences.getString(USER_ADDITIONAL_PREFERENCES, null);
        final String searchCriteria = preferences.getString(USER_SEARCH_CRITERIA_VALUE, null);

        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
            User user = userSnapshot.getValue(User.class);

            filteredUserList.add(user);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}

