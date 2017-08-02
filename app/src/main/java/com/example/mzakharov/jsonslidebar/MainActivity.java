package com.example.mzakharov.jsonslidebar;


import android.app.Activity;
import android.app.ListActivity;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends Activity {
    ListView list;
    ManagersAdapter adapter;
    ArrayList<Managers> managersList;
    String url = "http://dev.tabasko.ga/api/public/v1/managers";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.list);
        managersList = new ArrayList<Managers>();
        try {
            new ManagersAsynTask().execute("http://dev.tabasko.ga/api/public/v1/managers");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public class ManagersAsynTask extends AsyncTask<String,Void,Boolean>{

        URL obj = new URL(url);

        public ManagersAsynTask() throws MalformedURLException {
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                //connection.setDoOutput(true);*/
                connection.setDoInput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("POST");
                int status = connection.getResponseCode();
                if (status != 200) {
                    throw new IOException("Post failed with error code " + status);
                }
                BufferedReader  bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                JSONObject jsonObj = new JSONObject(new JSONTokener(bufferedReader.toString()));
                JSONArray jsonArray = jsonObj.getJSONArray("Managers");
                for (int i =0; i<jsonArray.length();i++)
                {
                    Managers manager= new Managers();
                    JSONObject jsonExactObject= (JSONObject) jsonArray.get(i);
                    manager.setId(jsonExactObject.getInt("id"));
                    manager.setAvatar(jsonExactObject.getString("avatar"));
                    manager.setFirstname(jsonExactObject.getString("firstname"));
                    manager.setSurname(jsonExactObject.getString("surname"));

                    managersList.add(manager);
                }

                return true;


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return false;
        }


        @Override
        protected  void onPostExecute(Boolean result){
            super.onPostExecute(result);

            if (result==false){

                // data was not parsed

            }
            else {

                ManagersAdapter adapter = new ManagersAdapter(getApplicationContext(),R.layout.list_adapter,managersList);
                list.setAdapter(adapter);
            }
        }
    }


}


