package brandon.example.com.marvelapp;

import android.app.Activity;
import android.content.ClipData;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import brandon.example.com.marvelapp.adapters.ItuneArrayAdapter;
import brandon.example.com.marvelapp.pojo.Itune;

public class MainActivity extends Activity {

    private EditText editText;
    private ImageButton atras, adelante;
    private ListView listView;
    private RequestQueue mQueue;
    private int max = 0;

    //private ArrayAdapter<String> arrayAdapter;

    private ItuneArrayAdapter ituneArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.lista);
        atras = (ImageButton) findViewById(R.id.atras);
        adelante = (ImageButton) findViewById(R.id.adelante);


        metodoAdapter(max);



    }

    public void metodoAdapter(int max){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList<String>());
        listView.setAdapter(adapter);
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        jsonMarvel(getMarvelString(max), adapter);
    }

    public void adelante(View view){

        max += 100;
        if(max>=1000){
            Toast.makeText(this, "Llegaste al limite superior", Toast.LENGTH_SHORT).show();
            return;
        }else{
            metodoAdapter(max);
        }


    }

    public void atras(View view){
        if(max<=0){
            Toast.makeText(this, "Llegaste al limite inferior", Toast.LENGTH_SHORT).show();
            return;
        }else{
            metodoAdapter(max);
        }
    }



    private final String LOG_TAG = "MARVEL";

    private static char[] HEXCodes = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    private void jsonMarvel(String url, final ArrayAdapter<String> adapter){
        adapter.clear();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    JSONArray jsonArray = data.getJSONArray("results");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        adapter.add(jsonObject.getString("name"));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        mQueue.add(request);
    }

    private String getMarvelString(int offset){
        String ts = Long.toString(System.currentTimeMillis() / 1000);
        String apikey = "0cd5d5ff9345949a365cf8f33f16db5d";
        String hash = md5(ts + "b3619745bea3f3d4374e1f17c93ce92dfce21635" + "0cd5d5ff9345949a365cf8f33f16db5d");
        ArrayList<String> arrayList = new ArrayList<>();


            /*
                Conexión con el getway de marvel
            */
        final String CHARACTER_BASE_URL =
                "http://gateway.marvel.com/v1/public/characters";

            /*
                Configuración de la petición
            */
        String characterJsonStr = null;
        final String TIMESTAMP = "ts";
        final String API_KEY = "apikey";
        final String HASH = "hash";
        final String ORDER = "orderBy";

        Uri builtUri;
        builtUri = Uri.parse(CHARACTER_BASE_URL+"?").buildUpon()
                .appendQueryParameter(TIMESTAMP, ts)
                .appendQueryParameter(API_KEY, apikey)
                .appendQueryParameter(HASH, hash)
                .appendQueryParameter(ORDER, "name")
                .appendQueryParameter("limit", "100")
                .appendQueryParameter("offset", offset+"")
                .build();

        return builtUri.toString();
    }

    public static String md5(String s) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            String hash = new String(hexEncode(digest.digest()));
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    /*
        Investiga y reporta qué hace esta aplicación
    */
    public static String hexEncode(byte[] bytes) {
        char[] result = new char[bytes.length*2];
        int b;
        for (int i = 0, j = 0; i < bytes.length; i++) {
            b = bytes[i] & 0xff;
            result[j++] = HEXCodes[b >> 4];
            result[j++] = HEXCodes[b & 0xf];
        }
        return new String(result);
    }



}
