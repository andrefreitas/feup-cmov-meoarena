package org.feup.meoarenacustomer.app;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class VouchersActivity extends ListActivity {

    Storage db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchers);

        db = new Storage(this);
        listVouchers();
    }

    private void listVouchers() {
        String customerID = db.get("id");
        Integer length = db.getVouchers(customerID).length;
        String[][] vouchers = new String[length][];
        System.arraycopy(db.getVouchers(customerID), 0, vouchers, 0, length );
        String[] items = new String[vouchers.length];
        for (int i=0; i < vouchers.length; i++) {
            String product = "";
            if (vouchers[i][1].equals("popcorn")){
                product = "Pipocas grátis";
            } else if (vouchers[i][1].equals("coffee")) {
                product = "Café grátis";
            } else if (vouchers[i][1].equals("all")) {
                product = "5% desconto em toda a cafetaria";
            }

            items[i] = product;
        }

        ListView listview= getListView();
        listview.setTextFilterEnabled(true);

        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vouchers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            db.dropTables();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
