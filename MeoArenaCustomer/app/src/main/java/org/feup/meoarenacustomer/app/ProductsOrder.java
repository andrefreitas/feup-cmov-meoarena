package org.feup.meoarenacustomer.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;


public class ProductsOrder extends ListActivity {

    Storage db;
    API api;
    String[] items;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_order);

        db = new Storage(this);
        api = new API();

        //populate items array with vouchers info
        setTotalPrice();
        listVouchers();
        payOrder();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.products_order, menu);
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
        CheckedTextView item = (CheckedTextView) v;
        Toast.makeText(this, items[position] + " checked : " +
                item.isChecked(), Toast.LENGTH_SHORT).show();
    }

    public void payOrder() {
        Button pay_order = (Button) findViewById(R.id.pay_order);
        pay_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SparseBooleanArray sparseBooleanArray = listview.getCheckedItemPositions();
                if (sparseBooleanArray.size() > 3) {
                    Toast.makeText(getApplicationContext(), R.string.limit_vouchers_used, Toast.LENGTH_SHORT).show();
                } else {
                    askPin();
                }

            }
        });
    }

    public void listVouchers() {
        String customerID = db.get("id");
        if (db.getVouchers(customerID) == null || db.getVouchers(customerID).length == 0) {
            Toast.makeText(getApplicationContext(), R.string.no_vouchers, Toast.LENGTH_SHORT).show();
        } else {
            Integer length = db.getVouchers(customerID).length;
            String[][] vouchers = new String[length][];
            System.arraycopy(db.getVouchers(customerID), 0, vouchers, 0, length);
            items = new String[vouchers.length];
            for (int i = 0; i < vouchers.length; i++) {
                String product = "";
                if (vouchers[i][1].equals("popcorn")) {
                    product = "Pipocas grátis";
                } else if (vouchers[i][1].equals("coffee")) {
                    product = "Café grátis";
                } else if (vouchers[i][1].equals("all")) {
                    product = "5% desconto em toda a cafetaria";
                }

                items[i] = product;
            }

            listview = getListView();
            listview.setChoiceMode(listview.CHOICE_MODE_MULTIPLE);
            listview.setTextFilterEnabled(true);

            setListAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_checked, items));
        }

    }

    public void askPin() {
        // Ask for pin
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Pin");
        alert.setMessage("Por favor insira o pin para confirmar a operação.");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent intent = getIntent();
                String showID = intent.getStringExtra("showID");
                String customerID = db.get("id");
                String pin = input.getText().toString();
                NumberPicker np = (NumberPicker) findViewById(R.id.number_to_buy);
                //callAPI(showID, customerID, pin, np.getValue());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(getApplicationContext(), "Operação Cancelada", Toast.LENGTH_SHORT).show();
            }
        });

        alert.show();
    }

    public void setTotalPrice() {
        TextView total_price = ((TextView) findViewById(R.id.products_order_price));
        Intent intent = getIntent();
        total_price.setText("Total: " + intent.getStringExtra("price"));
    }
}
