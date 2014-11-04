package org.feup.meoarenacustomer.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class ProductsAdapter extends BaseAdapter {
    String[][] content;
    Context ctx;
    LayoutInflater inflater;
    public HashMap<String,String> checked = new HashMap<String,String>();

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
    public String[] getItem(int i) {
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

        // Checkbox
        CheckBox name = (CheckBox) view.findViewById(R.id.product);
        name.setText(content[i][0]);
        final View v = view;
        final int position = i;
        name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton group, boolean isChecked) {
                setCheckedItem(position);
                EditText quantity = (EditText) v.findViewById(R.id.product_quantity);
                content[position][3] = quantity.getText().toString();
            }
        });

        EditText quantity = (EditText) view.findViewById(R.id.product_quantity);
        quantity.setText("0");

        TextView price = (TextView) view.findViewById(R.id.product_price);
        price.setText(content[i][2] + " €");

        TextView p_name = (TextView) view.findViewById(R.id.product_name);
        p_name.setText(content[i][1]);

        return view;


    }



    public void setCheckedItem(int item) {
        if (checked.containsKey(String.valueOf(item))){
            checked.remove(String.valueOf(item));
        }
        else {
            checked.put(String.valueOf(item), String.valueOf(item));
        }
    }

    public HashMap<String, String> getCheckedItems(){
        return checked;
    }
}
