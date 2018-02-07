package brandon.example.com.marvelapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import brandon.example.com.marvelapp.R;
import brandon.example.com.marvelapp.VolleySingleton;
import brandon.example.com.marvelapp.pojo.MarveDude;

/**
 * Created by brandon on 02/02/18.
 */

public class MarvelAdapter extends ArrayAdapter<MarveDude>{
    private Context context;
    public MarvelAdapter(@NonNull Context context, int resource, @NonNull List<MarveDude> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MarveDude marveDude = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.marvel_layout,parent,false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.collection);
        NetworkImageView networkImageView = (NetworkImageView)convertView.findViewById(R.id.imageView);
        textView.setText(marveDude.name);
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
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
        return convertView;
    }
}
