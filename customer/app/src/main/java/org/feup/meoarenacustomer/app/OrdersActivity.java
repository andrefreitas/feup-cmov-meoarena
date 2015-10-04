package org.feup.meoarenacustomer.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class OrdersActivity extends ListActivity {

    API api;
    Storage db;
    ListView listview;
    String[][] orders;
    int item;
    String position_i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        api = new API();
        db = new Storage(this);

        getValidation();
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
        if (id == R.id.action_logout) {
            db.dropTables();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onListItemClick(ListView parent, View v,int position,long id){
        item = position;
    }

    public void getValidation() {
        Intent intent = getIntent();
        String origin = intent.getStringExtra("origin");
        if (origin != null) {
            if (origin.equals("order")) {
                String orderID = intent.getStringExtra("orderID");
                String vouchers = intent.getStringExtra("vouchers");
                String products = intent.getStringExtra("products");
                String price = intent.getStringExtra("price");
                position_i = intent.getStringExtra("position");
                String vouchersName = intent.getStringExtra("vouchersName");

                saveVouchers(db.get("id"));

                // Add position to disable
                String pos = db.get("positions");
                if (pos == null || pos.equals("")) {
                    db.put("positions", position_i);
                } else {
                    db.updateKey("positions", pos + "," + position_i);
                }

                // Ask for pin
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Encomenda validada");
                String message="";
                String p = "";
                String[] products_list = products.split(",");

                for (int i=0; i < products_list.length; i++) {
                    if (products_list[i].equals("coffee")) {
                        p += " Cafe";
                    }
                    else if (products_list[i].equals("juice")) {
                        p += " Sumo";
                    }
                    else if (products_list[i].equals("sandwich")) {
                        p += " Sandes";
                    }
                    else if (products_list[i].equals("popcorn")) {
                        p += " Pipocas";
                    }

                    if (i != products_list.length-1) {
                        p += ",";
                    }
                }

                if (vouchersName.equals("nonames")) {
                    message = "Foi validada a sua encomenda com o id " + orderID + ", os produtos:" + p
                             + " pelo preco de " + price + " EUR";
                } else {
                    String v = "";
                    String[] v_list = vouchersName.split(",");

                    for (int i=0; i < v_list.length; i++) {
                        if (v_list[i].equals("all")) {
                            v += " 5% cafetaria";
                        }
                        else if (v_list[i].equals("coffee")) {
                            v += " Cafe gratis";
                        }
                        else if (v_list[i].equals("popcorn")) {
                            v += " Pipocas gratis";
                        }

                        if (i != v_list.length-1) {
                            v += ",";
                        }

                    }


                    message = "Foi validada a sua encomenda com o id " + orderID + ", os produtos:" + p
                            + " e os vouchers: " + v + " pelo preco de " + price + " EUR";
                }
                alert.setMessage(message);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        startMenu();
                    }
                });
                alert.show();

                //Delete voucher
                if (!vouchers.equals("novouchers")) {
                    String[] vouchers_list = vouchers.split(",");
                    for (int j=0; j < vouchers_list.length; j++) {
                        db.deleteVoucher(vouchers_list[j]);
                    }
                }
            }
        }
    }

    public void startMenu(){
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
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

                startValidation(pin, customerID, vouchers, products, quantity, price, item);
            }
        });
    }

    public void startValidation(String pin, String customerID, String vouchers, String products, String quantity, String price, int position) {
        Intent intent = new Intent(this, ValidationActivity.class);
        intent.putExtra("origin", "order");
        intent.putExtra("pin", pin);
        intent.putExtra("customerID", customerID);
        intent.putExtra("vouchers", vouchers);
        intent.putExtra("products", products);
        intent.putExtra("quantity", quantity);
        intent.putExtra("price", price);
        intent.putExtra("position", String.valueOf(position));
        startActivity(intent);
    }

    public void listOrders() {
        orders = db.getOrders(db.get("id"));
        listview = getListView();
        listview.setChoiceMode(listview.CHOICE_MODE_SINGLE);
        listview.setTextFilterEnabled(true);

        final String[] items = new String[orders.length];

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_checked, items){
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null)
                {
                    View v = getLayoutInflater().inflate(android.R.layout.simple_list_item_checked, null);
                    final CheckedTextView ctv = (CheckedTextView)v.findViewById(android.R.id.text1);
                    ctv.setText(items[position]);

                    String[] positions = db.get("positions").split(",");

                    if (positions != null && !positions[0].equals("")) {
                        for (int i = 0; i<positions.length; i++) {
                            if (Integer.parseInt(positions[i]) == position) {
                                ctv.setEnabled(false);
                            }
                        }
                    }

                    if (position_i != null) {
                        int p = Integer.parseInt(position_i);
                        if (position == p) {
                            ctv.setEnabled(false);
                        }
                    }

                    return v;
                }

                return convertView;
            }
        };

        setListAdapter(adapter);
    }

    private void saveVouchers(String customerID) {
        api.getVouchers(customerID, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                db.dropVouchers();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject obj = null;
                    try {
                        obj = response.getJSONObject(i);
                        db.saveVoucher(obj.getString("id"), db.get("id"), obj.getString("product"),
                                obj.getString("discount"), obj.getString("status"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getApplicationContext(), R.string.save_vouchers, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), R.string.no_vouchers, Toast.LENGTH_SHORT).show();
            }

        });
    }

}
