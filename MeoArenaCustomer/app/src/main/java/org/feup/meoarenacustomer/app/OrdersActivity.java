package org.feup.meoarenacustomer.app;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class OrdersActivity extends ListActivity {

    API api;
    Storage db;
    ListView listview;
    String[][] orders;
    int item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        api = new API();
        db = new Storage(this);

        listOrders();
        validateOrders();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.orders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onListItemClick(ListView parent, View v,int position,long id){
        item = position;
    }

    public void validateOrders() {
        Button validate = (Button) findViewById(R.id.validate_orders);

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pin = orders[item][0];
                String customerID = orders[item][1];
                String vouchers = orders[item][2];
                String products = orders[item][6];
                String quantity = orders[item][4];
                String price = orders[item][5];

                startValidation(pin, customerID, vouchers, products, quantity, price);
            }
        });
    }

    public void startValidation(String pin, String customerID, String vouchers, String products, String quantity, String price) {
        Intent intent = new Intent(this, ValidationActivity.class);
        intent.putExtra("origin", "order");
        intent.putExtra("pin", pin);
        intent.putExtra("customerID", customerID);
        intent.putExtra("vouchers", vouchers);
        intent.putExtra("products", products);
        intent.putExtra("quantity", quantity);
        intent.putExtra("price", price);

        startActivity(intent);
    }

    public void listOrders() {
        orders = db.getOrders(db.get("id"));
        listview = getListView();
        listview.setChoiceMode(listview.CHOICE_MODE_SINGLE);
        listview.setTextFilterEnabled(true);

        String[] items = new String[orders.length];

        for (int i = 0; i < orders.length; i++) {
            String[] products = orders[i][3].split(",");
            String order = "";
            // Replace names by better ones and append
            for (int j = 0; j < products.length; j++) {
                if (products[j].equals("popcorn"))
                    order += "Pipocas";
                else if (products[j].equals("coffee"))
                    order += "Café";
                else if (products[j].equals("juice"))
                    order += "Sumo";
                else if (products[j].equals("sandwich"))
                    order += "Sandes";

                if (j != products.length-1)
                    order += ", ";
            }

            items[i] = order;
        }

        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, items));

    }



}
