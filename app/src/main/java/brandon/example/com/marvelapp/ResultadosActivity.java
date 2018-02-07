package brandon.example.com.marvelapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import brandon.example.com.marvelapp.adapters.MarvelAdapter;
import brandon.example.com.marvelapp.pojo.MarveDude;

public class ResultadosActivity extends Activity {
    private String id;
    private RequestQueue mQueue;
    private TextView name, description;
    private ImageLoader imageLoader;
    NetworkImageView networkImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);
        name = (TextView) findViewById(R.id.textView);
        description = (TextView) findViewById(R.id.textView2);
        networkImageView = (NetworkImageView)findViewById(R.id.imageView3);


        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        jsonMarvel(getMarvelString());
    }




    //NEW
    private final String LOG_TAG = "MARVEL";

    private static char[] HEXCodes = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    private void jsonMarvel(String url){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject data = response.getJSONObject("data");
                    JSONArray jsonArray = data.getJSONArray("results");
                    Log.e("MYLOG",jsonArray.length()+"");
                    for(int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject thumbnail = jsonObject.getJSONObject("thumbnail");
                        String string = thumbnail.getString("path")+"/portrait_uncanny"+"."+thumbnail.getString("extension");
                        MarveDude marveDude = new MarveDude();
                        marveDude.id = id;
                        marveDude.description = jsonObject.getString("description");
                        marveDude.name = jsonObject.getString("name");
                        marveDude.url = string;

                        Log.e("MYLOG",marveDude.name+"");
                        name.setText(marveDude.name);
                        if(marveDude.description.equalsIgnoreCase("")){
                            description.setText("No tiene descripción");
                        }else{
                            description.setText(marveDude.description);
                        }


                        RequestQueue requestQueue = VolleySingleton.getInstance(ResultadosActivity.this).getRequestQueue();
                        ImageLoader imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
                            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(10);
                            @Override
                            public Bitmap getBitmap(String url) {
                                return cache.get(url);
                            }

                            @Override
                            public void putBitmap(String url, Bitmap bitmap) {
                                cache.put(url,bitmap);
                            }
                        });

                        networkImageView.setImageUrl(marveDude.url,imageLoader);

                    }

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

    private String getMarvelString(){
        String ts = Long.toString(System.currentTimeMillis() / 1000);
        String apikey = "0cd5d5ff9345949a365cf8f33f16db5d";
        String hash = md5(ts + "b3619745bea3f3d4374e1f17c93ce92dfce21635" + "0cd5d5ff9345949a365cf8f33f16db5d");
        ArrayList<String> arrayList = new ArrayList<>();


            /*
                Conexión con el getway de marvel
            */
        final String CHARACTER_BASE_URL =
                "http://gateway.marvel.com/v1/public/characters/"+id;

            /*
                Configuración de la petición
            */
        String characterJsonStr = null;
        final String TIMESTAMP = "ts";
        final String API_KEY = "apikey";
        final String HASH = "hash";
        final String ORDER = "orderBy";
        final String CHARACTER = "characterId";

        Uri builtUri;
        builtUri = Uri.parse(CHARACTER_BASE_URL+"?").buildUpon()
                .appendQueryParameter(TIMESTAMP, ts)
                .appendQueryParameter(API_KEY, apikey)
                .appendQueryParameter(HASH, hash)
                .appendQueryParameter(CHARACTER, id)
                .build();

        return builtUri.toString();
    }

    public static String md5(String s) {
        try {
            MessageDigest digest =java.security.MessageDigest.getInstance("MD5");
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

    public void imprimir(){

    }

}
