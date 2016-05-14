package com.inorexstudio.gauravdeshmukh.foodex;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.factual.driver.Circle;
import com.factual.driver.Factual;
import com.factual.driver.Filter;
import com.factual.driver.Query;
import com.factual.driver.ReadResponse;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.common.collect.Lists;
import com.lapism.searchview.adapter.SearchAdapter;
import com.lapism.searchview.adapter.SearchItem;
import com.lapism.searchview.history.SearchHistoryTable;
import com.lapism.searchview.view.SearchCodes;
import com.lapism.searchview.view.SearchView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    ProgressBar progressBar;
//    EditText searchEditText;
    double latitude, longitude;
    private static final String TAG = "MAIN_ACTIVITY_ASYNC";
    public static final int USE_ADDRESS_NAME = 1;
    public static final int USE_ADDRESS_LOCATION = 2;

    int fetchType = USE_ADDRESS_NAME;
    Factual factual;

    String check = "Resume", name="";
    //String finalUrl = null;
   // NetworkInfo networkInfo;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    ListView lv;
    SearchView mSearchView = null;
    private int mVersion = SearchCodes.VERSION_TOOLBAR;
    private int mStyle = SearchCodes.STYLE_TOOLBAR_CLASSIC;
    private int mTheme = SearchCodes.THEME_LIGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        searchEditText = (EditText) findViewById(R.id.searchEditText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        factual = new Factual("L6nKIQGMoGgrwI69OE9eWQXDzEkVh6akI2F3eCWW","API_KEY");

        lv = (ListView) findViewById(R.id.card_listView);
        mSearchView = (SearchView) findViewById(R.id.searchView);


        // SearchView basic attributes  ------------------------------------------------------------
        mSearchView = (SearchView) findViewById(R.id.searchView);
        mSearchView.setVersion(mVersion);
        mSearchView.setStyle(mStyle);
        mSearchView.setTheme(mTheme);
        // -----------------------------------------------------------------------------------------
        mSearchView.setDivider(true);
        mSearchView.setHint("Search in the City");
        mSearchView.setHintSize(getResources().getDimension(R.dimen.search_text_medium));
        mSearchView.setVoice(false);
//        mSearchView.setVoiceText("Voice");
        mSearchView.setAnimationDuration(300);
        mSearchView.setShadowColor(Color.LTGRAY);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                name = query;
                mSearchView.setHint("City: " + query);
                new GeocodeAsyncTask().execute();
                mSearchView.hide(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mSearchView.setOnSearchMenuListener(new SearchView.SearchMenuListener() {
            @Override
            public void onMenuClick() {
                Toast.makeText(MainActivity.this, "DUMMY", Toast.LENGTH_LONG).show();
            }
        });

//        getLocation();
    }

    private void getLocation() {
//        progressBar.setVisibility(View.VISIBLE);

        if (!checkLocation())
        return;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if(fetchType==USE_ADDRESS_LOCATION) {
            if (check.equals("Pause")) {
                locationManager.removeUpdates(locationListenerNetwork);
                check = "Resume";
            } else {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 60 * 1000, 10, locationListenerNetwork);
                Toast.makeText(this, "Network provider started running", Toast.LENGTH_LONG).show();
                check = "Pause";
            }
        }
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void toggleNetwork(View view) {
        progressBar.setVisibility(View.VISIBLE);

        if (!checkLocation())
            return;
        if (fetchType==USE_ADDRESS_NAME)
            new GeocodeAsyncTask().execute();
    }

    private final LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Your Location Updated", Toast.LENGTH_SHORT).show();
                }
            });
            new GeocodeAsyncTask().execute();

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.inorexstudio.gauravdeshmukh.foodex/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.inorexstudio.gauravdeshmukh.foodex/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    class GeocodeAsyncTask extends AsyncTask<Void, Void, Address> {

        String errorMessage = "";

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Address doInBackground(Void... none) {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addresses = null;

            if (fetchType == USE_ADDRESS_NAME) {
                try {
                    addresses = geocoder.getFromLocationName(name, 1);
                } catch (IOException e) {
                    errorMessage = "Service not available";
                    Log.e(TAG, errorMessage, e);
                }
            } else if (fetchType == USE_ADDRESS_LOCATION) {
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    fetchType = USE_ADDRESS_NAME;
                } catch (IOException ioException) {
                    errorMessage = "Service Not Available";
                    Log.e(TAG, errorMessage, ioException);
                } catch (IllegalArgumentException illegalArgumentException) {
                    errorMessage = "Invalid Latitude or Longitude Used";
                    Log.e(TAG, errorMessage + ". " +
                            "Latitude = " + latitude + ", Longitude = " +
                            longitude, illegalArgumentException);
                }
            } else {
                errorMessage = "Unknown Type";
                Log.e(TAG, errorMessage);
            }

            if (addresses != null && addresses.size() > 0)
                return addresses.get(0);

            return null;
        }

        protected void onPostExecute(Address address) {
            if (address == null) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            } else {
                String addressName = "";
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressName += " --- " + address.getAddressLine(i);
                }
                latitude = address.getLatitude();
                longitude = address.getLongitude();
                Log.e("TAG", latitude + "|" + longitude);

                Query query = new Query()
                        .within(new Circle(latitude, longitude, 5000))
                        .field("category_ids").includesAny(348, 347, 312, 355, 361)
                        .only("name", "address", "rating", "cuisine")
                        .limit(50);

                new FactualRetrievalTask().execute(query);
            }
        }
    }

    protected class FactualRetrievalTask extends AsyncTask<Query, Integer, List<ReadResponse>> {
        @Override
        protected List<ReadResponse> doInBackground(Query... params) {
            List<ReadResponse> results = Lists.newArrayList();
            for (Query q : params) {
                results.add(factual.fetch("restaurants-us", q));
            }
            return results;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected void onPostExecute(List<ReadResponse> responses) {
            ArrayList<SearchResults> results = new ArrayList<SearchResults>();

            SearchResults sr;
            for (ReadResponse response : responses) {
                for (Map<String, Object> restaurant : response.getData()) {
                    String name = (String) restaurant.get("name");
                    String address = (String) restaurant.get("address");
                    Number rating = (Number) restaurant.get("rating");
                    JSONArray cuisineArray = null;
                    String cuisine = "";
                    sr = new SearchResults();
                    if (restaurant.get("cuisine")!=null) {
                        try {
                            cuisineArray = new JSONArray(restaurant.get("cuisine").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < cuisineArray.length(); i++) {
                            try {
                                if(i==0)
                                    sr.setTopCuisine(cuisineArray.get(i).toString());

                                if(i==cuisineArray.length()-1)
                                    cuisine = cuisine + cuisineArray.get(i).toString();
                                else
                                    cuisine = cuisine + cuisineArray.get(i).toString() + ", ";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else
                        cuisine = "-";
                    sr.setName(name);
                    sr.setCityState(address);
                    sr.setRating(rating);
                    sr.setCuisine(cuisine);
                    results.add(sr);
                }
            }
            lv.setAdapter(new CustomBaseAdapter(MainActivity.this, results));
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
}
