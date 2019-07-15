package com.example.mohamed.androidclinic;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ArrayList<Clinic> clinicList;
    ArrayAdapter clinicAdapter;
    int doctorId;

    public MainActivityFragment() {
        doctorId=22;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        clinicList= new ArrayList<Clinic>();

        clinicAdapter = new CustomAdapter(
                getActivity(),
                clinicList
        );

        ListView listView = (ListView) rootView.findViewById(
                R.id.listView);

        listView.setAdapter(clinicAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                Clinic clinic = (Clinic) adapterView.getItemAtPosition(postion);

                Intent intent;
                intent = new Intent(getActivity(), appointmentActivity.class);
                Bundle extras= new Bundle();

                extras.putInt("ClinicId",clinic.id);
                extras.putInt("doctorId", doctorId);

                intent.putExtras(extras);

                startActivity(intent);

                //    Toast.makeText(getActivity(), adapterView.getItemAtPosition(postion).toString(), Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;

    }

    private void updateClinic(){
        FetchClinicTask clinicTask =new FetchClinicTask();
        Integer[]in=new Integer[1];
        in[0]=22;
        doctorId=22;
        int test= in[0].intValue();

        clinicTask.execute(in);
    }

    @Override
    public void onStart(){
        super.onStart();
        Toast.makeText(getActivity(),"On start" , Toast.LENGTH_SHORT).show();

        updateClinic();
    }


    public class FetchClinicTask extends AsyncTask<Integer, Void, Clinic[] > {

        private final String LOG_TAG =FetchClinicTask.class.getSimpleName();




        @Override
        protected Clinic[] doInBackground(Integer... params){
            if(params.length == 0){
                return  null;
            }
            // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

// Will contain the raw JSON response as a string.

            String clinicJsonStr = null;

            int doctorid = params[0].intValue();

            try {
                    StringBuffer buffer = new StringBuffer();

                    String BASE_URL = "http://10.0.2.2:888/clinic_final_sw2/public/doctor/JsonMyClinics/"+doctorid;

                    URL url = new URL(BASE_URL);

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                        // Read the input stream into a String
                        InputStream inputStream = urlConnection.getInputStream();
                        if (inputStream == null) {
                            // Nothing to do.
                            return null;
                            //forecastJsonStr = null;
                        }
                        reader = new BufferedReader(new InputStreamReader(inputStream));

                        String line;
                        if(buffer.length() != 0){
                            buffer.append(',');
                        }
                        while ((line = reader.readLine()) != null) {
                            // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                            // But it does make debugging a *lot* easier if you print out the completed
                            // buffer for debugging.
                            buffer.append(line + "\n");
                        }



                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                    //forecastJsonStr = null;
                }
                clinicJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // to parse it
                //
                return null;
                //forecastJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getMovieDataFromJson(clinicJsonStr);
            }
            catch (JSONException e){
                Log.e(LOG_TAG,e.getMessage(),e);
                    e.printStackTrace();
            }

            return new Clinic[0];
        }


        @Override
        protected void onPostExecute(Clinic[] result) {

            if(result != null){
                Toast.makeText(getActivity(), "you  are posted", Toast.LENGTH_LONG).show();

                clinicAdapter.clear();
                for (Clinic clinic : result){
                    Toast.makeText(getActivity(), clinic.name, Toast.LENGTH_SHORT).show();
                    clinicAdapter.add(clinic);
                }

            }
        }

        /**
         * Prepare the weather high/lows for presentation.
         */

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private Clinic[] getMovieDataFromJson(String clinicJsonStr)
                throws JSONException {

            String OWM_ID = "id";

            String OWM_NAME = "name";

            String OWM_LIST = "result";


            //JSONObject clinicJson = new JSONObject(movieJsonStr);


            JSONArray dataArray = new JSONArray(clinicJsonStr);
            JSONArray clinicArray = dataArray.getJSONArray(0);

            Clinic[] resultStrs = new Clinic[clinicArray.length()];



            for (int i =0;i< clinicArray.length();i++){
                JSONObject clinic = clinicArray.getJSONObject(i);

                resultStrs[i] = new Clinic();
                resultStrs[i].id = clinic.getInt(OWM_ID);
                resultStrs[i].name = clinic.getString(OWM_NAME);
                }


            //  Movie[] resultStrs = new Movie[0];
            return resultStrs;
        }

        /**
         * Prepare the weather high/lows for presentation.
         */
    }



}
