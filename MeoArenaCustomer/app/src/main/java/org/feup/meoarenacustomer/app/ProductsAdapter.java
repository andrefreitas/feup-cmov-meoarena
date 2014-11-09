package org.feup.meoarenacustomer.app;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;

public class ProductsAdapter extends BaseAdapter {
    String[][] content;
    Context ctx;
    LayoutInflater inflater;
    public HashMap<String,String> checked = new HashMap<String,String>();
    TextView total_price;

    public ProductsAdapter(String[][] content, Context ctx, TextView total_price) {
        this.content = content;
        this.ctx = ctx;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.total_price = total_price;
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
    public View getView(int i, View view, final ViewGroup viewGroup) {
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
                content[position][4] = quantity.getText().toString();

                Iterator<String> it = getCheckedItems().values().iterator();
                BigDecimal total = BigDecimal.ZERO;
                for (int i = 0; i < getCheckedItems().size(); i++) {
                    Integer position = Integer.parseInt(it.next());
                    String quantity_s = getItem(position)[4];
                    Integer quantity_p;
                    if (quantity_s.equals("")) {
                        quantity_p = 0;
                    }
                    else {
                        quantity_p = Integer.parseInt(getItem(position)[4]);
                    }
                    BigDecimal price = new BigDecimal(getItem(position)[2]);
                    total = total.add(price.multiply(new BigDecimal(quantity_p)));
                }

                total_price.setText(total.toString() + " EUR");
            }
        });

        final EditText quantity = (EditText) view.findViewById(R.id.product_quantity);
        quantity.setText("0");
        quantity.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                content[position][4] = quantity.getText().toString();

                Iterator<String> it = getCheckedItems().values().iterator();
                BigDecimal total = BigDecimal.ZERO;
                for (int i = 0; i < getCheckedItems().size(); i++) {
                    Integer position = Integer.parseInt(it.next());
                    String quantity = getItem(position)[4];
                    Integer quantity_p;
                    if (quantity.equals("")) {
                        quantity_p = 0;
                    }
                    else {
                        quantity_p = Integer.parseInt(getItem(position)[4]);
                    }

                    BigDecimal price = new BigDecimal(getItem(position)[2]);
                    total = total.add(price.multiply(new BigDecimal(quantity_p)));
                }

                total_price.setText(total.toString() + " EUR");
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

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
