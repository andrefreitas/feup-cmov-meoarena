package org.feup.meoarenacustomer.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Ana  Gomes on 06/11/2014.
 */
public class TransactionsAdapter extends BaseAdapter {
    String[][] content;
    Context ctx;
    LayoutInflater inflater;

    public TransactionsAdapter(String[][] content, Context ctx) {
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
            view = inflater.inflate(R.layout.row_transaction, viewGroup, false);
        }

        TextView name = (TextView) view.findViewById(R.id.transaction_description);
        name.setText(content[i][1]);

        TextView seats = (TextView) view.findViewById(R.id.transaction_date);
        seats.setText(content[i][3]);

        TextView date = (TextView) view.findViewById(R.id.transaction_price);
        date.setText(content[i][2] + " EUR");

        return view;
    }
}
