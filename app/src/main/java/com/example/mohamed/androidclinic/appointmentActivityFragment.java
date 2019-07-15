package com.example.mohamed.androidclinic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class appointmentActivityFragment extends Fragment {

    ArrayList<Reservation> appointmentList;
    AdapterObject appointmentAdapter;
    int doctorId;
    int clinicId;

    public appointmentActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appointment, container, false);

        appointmentList= new ArrayList<Reservation>();

        appointmentAdapter = new AdapterObject(
                getActivity(),
                appointmentList
        );

        ListView listView = (ListView) rootView.findViewById(
                R.id.listView2);

        listView.setAdapter(appointmentAdapter);


        return rootView;
    }

    private void updateAppointment(){
        FetchAppointmentTask appointmentTask =new FetchAppointmentTask();
        Integer[]in=new Integer[2];
        in[0]=22;
        in[1]=30;
        doctorId=22;
        int test= in[0].intValue();

        appointmentTask.execute(in);
    }

    @Override
    public void onStart(){
        super.onStart();
        Toast.makeText(getActivity(), "On start", Toast.LENGTH_SHORT).show();

        updateAppointment();
    }


    public class FetchAppointmentTask extends AsyncTask<Integer, Void, Reservation[] > {

        private final String LOG_TAG =FetchAppointmentTask.class.getSimpleName();




        @Override
        protected Reservation[] doInBackground(Integer... params){
            if(params.length == 0){
                return  null;
            }
            // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

// Will contain the raw JSON response as a string.

            String clinicJsonStr = null;

            int doctorId = params[0].intValue();

            int clinicId = params[1].intValue();

            try {
                StringBuffer buffer = new StringBuffer();

                String BASE_URL = "http://10.0.2.2:888/clinic_final_sw2/public/doctor/JsonMyAppointments/"+doctorId+"/"+clinicId;

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

            return new Reservation[0];
        }


        @Override
        protected void onPostExecute(Reservation[] result) {

            if(result != null){
                Toast.makeText(getActivity(), "you  are posted", Toast.LENGTH_LONG).show();

                appointmentAdapter.clear();
                for (Reservation reservation : result){
                  //  Toast.makeText(getActivity(), clinic.name, Toast.LENGTH_SHORT).show();
                    appointmentAdapter.add(reservation);
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
        private Reservation[] getMovieDataFromJson(String appointmentJsonStr)
                throws JSONException {

            String OWM_ID = "id";

            String PATIENT_ID = "patient_id";


            String OWM_NAME = "patientName";

            String PATIENT_NAME = "patientName";

            String OWM_LIST = "data";


            //JSONObject clinicJson = new JSONObject(movieJsonStr);


            JSONObject jsonObject=new JSONObject(appointmentJsonStr);
            JSONArray appointmentArray = jsonObject.getJSONArray(OWM_LIST);

            Reservation[] resultStrs = new Reservation[appointmentArray.length()];



            for (int i =0;i< appointmentArray.length();i++){
                JSONObject reservation = appointmentArray.getJSONObject(i);

                resultStrs[i] = new Reservation();
                resultStrs[i].id = reservation.getInt(OWM_ID);
                resultStrs[i].patient_name = reservation.getString(OWM_NAME);
                resultStrs[i].patient_id=reservation.getInt(PATIENT_ID);
            }


            //  Movie[] resultStrs = new Movie[0];
            return resultStrs;
        }

        /**
         * Prepare the weather high/lows for presentation.
         */
    }



}
