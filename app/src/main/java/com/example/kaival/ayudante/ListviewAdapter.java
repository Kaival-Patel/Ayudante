package com.example.kaival.ayudante;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListviewAdapter extends ArrayAdapter<Recipes> {
    public ImageLoader imageLoader = ImageLoader.getInstance();

    public ListviewAdapter(Context context, int resource, List<Recipes> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.list_item, null);
        }
        Recipes recipes = getItem(position);
        ImageView iv = (ImageView) v.findViewById(R.id.imageviewlv);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView subtitle = (TextView) v.findViewById(R.id.subtitle);
        Picasso.get().load(recipes.getImageurl()).resize(80,70).into(iv);
       // imageLoader.displayImage(recipes.getUrl(),iv);
        title.setText(recipes.getTitle());
        subtitle.setText(recipes.getSubtitle());
        return v;


    }
}