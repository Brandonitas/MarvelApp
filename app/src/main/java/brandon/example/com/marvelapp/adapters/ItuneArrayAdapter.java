package brandon.example.com.marvelapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import brandon.example.com.marvelapp.R;
import brandon.example.com.marvelapp.pojo.Itune;

/**
 * Created by brandon on 26/01/18.
 */

public class ItuneArrayAdapter extends ArrayAdapter<Itune> {
    private ArrayList<Itune> arrayList;
    public ItuneArrayAdapter(Context context, int resource, List<Itune> objects) {
        super(context, resource, objects);
        arrayList = (ArrayList<Itune>) objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //regresa un renglon a la vez, posotion, en que posicion de los datos esta
        Itune ituneObj = arrayList.get(position);
        if(convertView == null){//si mi renglon es null
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.itunes_layout,parent,false);

        }
        TextView collectionName = (TextView) convertView.findViewById(R.id.collection);
        TextView trackName = (TextView) convertView.findViewById(R.id.trackName);
        TextView trackPrice = (TextView) convertView.findViewById(R.id.trackPrice);

        collectionName.setText(ituneObj.collectionName);
        trackName.setText(ituneObj.trackName);
        trackPrice.setText(ituneObj.trackPrice+"");

        return convertView;
    }
}
