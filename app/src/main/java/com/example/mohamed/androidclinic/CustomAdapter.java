package com.example.mohamed.androidclinic;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mohamed on 28/03/2016.
 */

public class CustomAdapter extends ArrayAdapter<Clinic> {


    Context context;

    private static LayoutInflater inflater=null;

    public CustomAdapter(Context c, ArrayList<Clinic> clinics) {
        super(c, 0 , clinics);
        this.context=c;
    }


    private class ViewHolder {
        TextView textView;
    }


    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;

        Clinic rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.clinic_item, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.item_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            convertView.setTag(holder);
        }

        holder.textView.setText(rowItem.name);

        return convertView;
    }

}


