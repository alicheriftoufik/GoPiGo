package com.example.toufik.gopigo_master2_ise;

/**
 * Created by toufik on 06/01/2017.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class RPIDataActivity extends Activity {

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    private static String url_all_measures = "http://192.168.43.178/db_view.php";

    // JSON Node
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_SENSOR = "sensor";
    private static final String TAG_ID = "id";
    private static final String TAG_MEASURE = "measure";

    JSONArray measures = null;

    ExpandableListAdapter listAdapter;
    ExpandableListView listView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bdd_data_layout);

        listView = (ExpandableListView) findViewById(R.id.listView);

        // Loading products in Background Thread
        new LoadAllMeasures().execute();
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllMeasures extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RPIDataActivity.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_measures, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    measures = json.getJSONArray(TAG_SENSOR);

                    listDataHeader = new ArrayList<String>();
                    listDataChild = new HashMap<String, List<String>>();

                    // looping through All Products
                    for (int i = 0; i < measures.length(); i++) {
                        //add to the nodes of the list
                        listDataHeader.add(measures.getJSONObject(i).getString(TAG_MEASURE));
                        ArrayList<String> list_data = new ArrayList<>();
                        list_data.add(measures.getJSONObject(i).getString(TAG_ID));
                        list_data.add(measures.getJSONObject(i).getString(TAG_MEASURE));
                        listDataChild.put(listDataHeader.get(i),list_data);
                    }

                    //save data
                    DataHolder.getInstance().setList(listDataHeader);
                } else {
                    // no products found
                    pDialog = new ProgressDialog(RPIDataActivity.this);
                    pDialog.setMessage("No measure found");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    listAdapter = new ExpandableListAdapter(RPIDataActivity.this, listDataHeader, listDataChild);
                    listView.setAdapter(listAdapter);
                }
            });

        }
    }
}