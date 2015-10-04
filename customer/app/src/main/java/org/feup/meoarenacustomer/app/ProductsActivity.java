package org.feup.meoarenacustomer.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;


public class ProductsActivity extends Activity {

    API api;
    Storage db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        setTitle("Cafetaria");

        api = new API();
        db =  new Storage(this);
        listProducts();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.products, menu);
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

    private void listProducts() {
        api.getProducts(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                String[][] allProducts = new String[response.length()][];
                for (int i = 0; i < response.length(); i++) {

                    JSONObject obj = null;
                    try {
                        String[] product = new String[5];
                        obj = response.getJSONObject(i);
                        product[0] = obj.getString("description");
                        product[1] = obj.getString("name");
                        product[2] = obj.getString("price");
                        product[3] = obj.getString("id");
                        allProducts[i] = product;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                populateListView(allProducts);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), R.string.no_products, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void populateListView(final String[][] allProducts) {
        TextView total_price = (TextView) findViewById(R.id.total_products_price);
        ListView listView = (ListView) findViewById(R.id.view_products);
        final ProductsAdapter adapter = new ProductsAdapter(allProducts, this, total_price);
        listView.setAdapter(adapter);

        Button buy_products = (Button) findViewById(R.id.buy_products);

        //Buy product
        buy_products.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
            String customerID = db.get("id");
            // Get total price
            TextView total_price = (TextView) findViewById(R.id.total_products_price);
            if (total_price.getText().toString().equals("0 EUR") || total_price.getText().toString().equals("0.0 EUR") ) {
                Toast.makeText(getApplicationContext(), R.string.no_orders, Toast.LENGTH_SHORT).show();
            }
            else {
                // Get information to send
                ProductsAdapter bAdapter = adapter;
                Iterator<String> it = bAdapter.getCheckedItems().values().iterator();
                String products_list = "";
                String products_id = "";
                String quantity_list = "";
                // Get both products and quantity list to send and save
                int size = bAdapter.getCheckedItems().size();
                for (int i=0; i<size; i++){
                    ArrayList<String> item = new ArrayList<String>();
                    Integer position = Integer.parseInt(it.next());
                    String name = bAdapter.getItem(position)[1];
                    String quantity = bAdapter.getItem(position)[4];
                    String id = bAdapter.getItem(position)[3];

                    String[] p = bAdapter.getItem(position);
                    //Only possible: juice, coffee, sandwich and popcorn
                    if (i == size - 1) {
                        products_list += name;
                        quantity_list += quantity;
                        products_id += id;
                    } else {
                        products_list += name + ",";
                        quantity_list += quantity + ",";
                        products_id += id + ",";
                    }
                }

                if (db.getVouchers(customerID) == null || db.getVouchers(customerID).length == 0) {
                    askPin(total_price.getText().toString(), products_list, quantity_list, products_id, customerID, "unused");
                }
                else {
                    Intent intent = new Intent(ProductsActivity.this, ProductsOrder.class);
                    intent.putExtra("price", total_price.getText().toString());
                    intent.putExtra("products", products_list);
                    intent.putExtra("quantity", quantity_list);
                    intent.putExtra("products_id", products_id);
                    startActivity(intent);
                }

            }
             }
        });
    }

    public void askPin(String price, final String products, final String quantity,
                       final String products_id, final String customerID,  final String status) {
        // Ask for pin
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Pin");
        alert.setMessage("Por favor insira o pin para confirmar a operação.");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);
        final String f_price = price.split(" ")[0];
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final String pin = input.getText().toString();
                api.checkValidPin(customerID, pin, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        db.saveOrder(customerID, pin, "", products, products_id, quantity, f_price, status);
                        successOrder();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getApplicationContext(), R.string.wrong_pin, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(getApplicationContext(), "Operação Cancelada", Toast.LENGTH_SHORT).show();
            }
        });

        alert.show();
    }

    public void successOrder() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), R.string.success_orders, Toast.LENGTH_SHORT).show();
    }

 }
