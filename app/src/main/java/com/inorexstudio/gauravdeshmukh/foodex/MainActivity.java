package com.inorexstudio.gauravdeshmukh.foodex;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.factual.driver.Circle;
import com.factual.driver.Factual;
import com.factual.driver.Query;
import com.factual.driver.ReadResponse;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.common.collect.Lists;
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
    double latitude, longitude;
    private static final String TAG = "MAIN_ACTIVITY_ASYNC";
    public static final int USE_ADDRESS_NAME = 1;
    public static final int USE_ADDRESS_LOCATION = 2;

    int fetchType = USE_ADDRESS_NAME;
    Factual factual;

    String check = "Resume", name="";

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

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        factual = new Factual("your_key","your_secret");

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
                Toast.makeText(MainActivity.this, "Gaurav Deshmukh", Toast.LENGTH_LONG).show();
            }
        });

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
