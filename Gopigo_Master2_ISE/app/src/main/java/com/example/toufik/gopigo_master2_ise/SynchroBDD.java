package com.example.toufik.gopigo_master2_ise;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toufik on 06/01/2017.
 */

public class SynchroBDD {

    Context context;
    JSONParser jParser = new JSONParser();
    private static String url_all_measures = "http://192.168.43.88/db_view.php";

    // JSON Node
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_SENSOR = "sensor";
    private static final String TAG_ID = "id";
    private static final String TAG_MEASURE = "measure";

    JSONArray measures = null;
    List<String> listData;

    public SynchroBDD(Context context) {
        this.context=context;
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

                    listData = new ArrayList<>();

                    // looping through All Products
                    for (int i = 0; i < measures.length(); i++) {
                        listData.add(measures.getJSONObject(i).getString(TAG_MEASURE));
                    }
                } else {
                    Log.e(getClass().getSimpleName(),"No measure found");
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
            //save data
            DataHolder.getInstance().setList(listData);

            //save data in SQLite database
            MeasureBDD measureBDD = new MeasureBDD(context);
            measureBDD.open();
            measureBDD.cleanTable();
            for(int i=0; i<DataHolder.getInstance().getList().size(); i++){
                measureBDD.insertMeasure(new Measure(i,Integer.parseInt(DataHolder.getInstance().getList().get(i))));
            }
            measureBDD.close();
        }
    }
}