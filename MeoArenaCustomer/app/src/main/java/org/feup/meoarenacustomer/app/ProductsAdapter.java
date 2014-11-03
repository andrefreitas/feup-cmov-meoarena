package org.feup.meoarenacustomer.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProductsAdapter extends BaseAdapter {
    String[][] content;
    Context ctx;
    LayoutInflater inflater;

    public ProductsAdapter(String[][] content, Context ctx) {
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
            view = inflater.inflate(R.layout.row_product, viewGroup, false);
        }

        TextView name = (TextView) view.findViewById(R.id.product);
        name.setText(content[i][0]);

        TextView price = (TextView) view.findViewById(R.id.product_price);
        price.setText(content[i][2] + " €");

        TextView seats = (TextView) view.findViewById(R.id.product_name);
        seats.setText(content[i][1]);

        return view;
    }
}
