package org.feup.meoarenacustomer.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class ShowsAdapter extends BaseAdapter {
    String[][] content;
    Context ctx;
    LayoutInflater inflater;

    public ShowsAdapter(String[][] content, Context ctx) {
        this.content = content;
        this.ctx = ctx;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return content.length;
    }

    @Override
    public Object getItem(int i) {
        return content[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view==null) {
            view = inflater.inflate(R.layout.row_show, viewGroup, false);
        }

        TextView name = (TextView) view.findViewById(R.id.show_name);
        name.setText(content[i][0]);

        TextView date = (TextView) view.findViewById(R.id.show_date);
        date.setText(content[i][2]);

        if(Integer.parseInt(content[i][5]) > 0) {
            TextView price = (TextView) view.findViewById(R.id.show_price);
            price.setText(content[i][1] + " €");
        }
        else {
            TextView price = (TextView) view.findViewById(R.id.show_price);
            price.setText("Esgotado");
        }

        TextView seats = (TextView) view.findViewById(R.id.show_seats);
        seats.setText(content[i][3]);

        TextView available = (TextView) view.findViewById(R.id.show_available);
        available.setText(content[i][5]);

        TextView showID = (TextView) view.findViewById(R.id.show_id);
        showID.setText(content[i][4]);

        return view;
    }
}
