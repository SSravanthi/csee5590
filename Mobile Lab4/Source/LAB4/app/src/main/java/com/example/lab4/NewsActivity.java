package com.example.lab4;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NewsActivity extends AppCompatActivity {

    String API_KEY = "8190df9eb51445228e397e4185311a66";//Api used from newsapi
    String NEWS_SOURCE = "bbc-sport";// Adding News Channel
    ListView listNews;//list news
    ProgressBar loader;// Progress bar when it takes time to load the page
    // List of data once page load example title display,date
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    static final String KEY_AUTHOR = "author";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_URL = "url";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_PUBLISHEDAT = "publishedAt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news2);
        listNews = (ListView) findViewById(R.id.listNews);
        loader = (ProgressBar) findViewById(R.id.loader);
        listNews.setEmptyView(loader);
        if(NewsConnect.isNetworkAvailable(getApplicationContext()))//Connecting to Network
        {
            DownloadNews newsTask = new DownloadNews();//Download News
            newsTask.execute();
        }else{
            Toast.makeText(getApplicationContext(), "Sorry Cannot load page", Toast.LENGTH_LONG).show();
        }

    }
    class DownloadNews extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected String doInBackground(String... args) {
            String xml = "";
            String urlParameters = "";// Connecting to newsapi and Get articles
            xml = NewsConnect.excuteGet("https://newsapi.org/v1/articles?source="+NEWS_SOURCE+"&sortBy=top&apiKey="+API_KEY, urlParameters);
            return  xml;
        }
        @Override
        protected void onPostExecute(String xml) {

            if(xml.length()>6){

                try {
                    JSONObject jsonResponse = new JSONObject(xml);
                    JSONArray jsonArray = jsonResponse.optJSONArray("articles");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_AUTHOR, jsonObject.optString(KEY_AUTHOR).toString());
                        map.put(KEY_TITLE, jsonObject.optString(KEY_TITLE).toString());
                        map.put(KEY_DESCRIPTION, jsonObject.optString(KEY_DESCRIPTION).toString());
                        map.put(KEY_URL, jsonObject.optString(KEY_URL).toString());
                        map.put(KEY_URLTOIMAGE, jsonObject.optString(KEY_URLTOIMAGE).toString());
                        map.put(KEY_PUBLISHEDAT, jsonObject.optString(KEY_PUBLISHEDAT).toString());
                        dataList.add(map);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Sorry Cannot load page", Toast.LENGTH_SHORT).show();
                }

                ListNewsAdapter adapter = new ListNewsAdapter(NewsActivity.this, dataList);//Listing news
                listNews.setAdapter(adapter);
            } }
    }



}
