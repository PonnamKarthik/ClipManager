package net.karthikponnam.clipmanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.karthikponnam.clipmanager.R;

import java.util.ArrayList;

/**
 * Created by ponna on 17-05-2017.
 */

public class ListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> data;
    private  ArrayList<String> date;

    public ListAdapter (Context context, ArrayList<String> data, ArrayList<String> date) {
        this.context = context;
        this.data = data;
        this.date = date;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.list_item, parent, false);
        }

        TextView dataTextView = (TextView)
                convertView.findViewById(R.id.data_list);
        TextView datetextView = (TextView)
                convertView.findViewById(R.id.date_list);
        dataTextView.setText(data.get(position));
        datetextView.setText(date.get(position));

        return convertView;
    }
}
