package org.feup.meoarenacustomer.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class TicketsAdapter extends BaseAdapter {
    String[][] content;
    Context ctx;
    LayoutInflater inflater;
    public HashMap<String,String> checked = new HashMap<String,String>();
    List<Integer> positions = new ArrayList<Integer>();

    public TicketsAdapter(String[][] content, Context ctx) {
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
            view = inflater.inflate(R.layout.row_ticket, viewGroup, false);
        }

        CheckBox name = (CheckBox) view.findViewById(R.id.ticket_name);
        if (positions.contains(i)) {
            name.setEnabled(false);
        }
        else {
            name.setText(content[i][0]);
            final int position = i;
            name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton group, boolean isChecked) {
                    setCheckedItem(position);
                }
            });
        }


        TextView seats = (TextView) view.findViewById(R.id.ticket_seat);
        seats.setText("Lugar " + content[i][1]);

        TextView date = (TextView) view.findViewById(R.id.ticket_date);
        date.setText(content[i][2]);

        TextView id = (TextView) view.findViewById(R.id.ticket_id);
        id.setText(content[i][4]);

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

    public void disableCheck(int position) {
        positions.add(position);
    }

}
