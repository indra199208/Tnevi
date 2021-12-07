package com.ivan.tnevi.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tnevi.R;
import com.ivan.tnevi.Utils.FileUtils;

import java.util.ArrayList;

/**
 * Created by Akshay Raj on 06/02/18.
 * akshay@snowcorp.org
 * www.snowcorp.org
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Uri> arrayList;
    private final int limit = 4;


    public MyAdapter(Context context, ArrayList<Uri> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        if (mInflater != null) {
            convertView = mInflater.inflate(R.layout.list_items, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView imagePath = convertView.findViewById(R.id.imagePath);
        ImageView btnDelet = convertView.findViewById(R.id.btnDelet);

        btnDelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrayList.remove(position);
                notifyDataSetChanged();

            }
        });

        imagePath.setText(FileUtils.getPath(context, arrayList.get(position)));

        Glide.with(context)
                .load(arrayList.get(position))
                .into(imageView);

        return convertView;
    }
}
