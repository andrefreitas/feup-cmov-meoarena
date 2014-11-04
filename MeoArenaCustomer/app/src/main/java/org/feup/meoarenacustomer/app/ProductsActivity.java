package org.feup.meoarenacustomer.app;

import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


public class ProductsActivity extends Activity {

    API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        setTitle("Cafetaria");

        api = new API();
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
        if (id == R.id.action_settings) {
            return true;
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
                        String[] product = new String[4];
                        obj = response.getJSONObject(i);
                        product[0] = obj.getString("description");
                        product[1] = obj.getString("name");
                        product[2] = obj.getString("price");
                        allProducts[i] = product;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                populateListView(allProducts);
                Toast.makeText(getApplicationContext(), "Chegou à API corretamente", Toast.LENGTH_SHORT).show();
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
        ListView listView = (ListView) findViewById(R.id.view_products);
        final ProductsAdapter adapter = new ProductsAdapter(allProducts, this);
        listView.setAdapter(adapter);

        Button buy_products = (Button) findViewById(R.id.buy_products);

        //Buy product
        buy_products.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 ProductsAdapter bAdapter = adapter;
                 Iterator<String> it = bAdapter.getCheckedItems().values().iterator();
                 Integer total = 0;
                 for (int i=0;i<bAdapter.getCheckedItems().size();i++){
                     Integer position = Integer.parseInt(it.next());
                     String description = bAdapter.getItem(position)[0];
                     String name = bAdapter.getItem(position)[1];
                     Double price = Double.parseDouble(bAdapter.getItem(position)[2]);
                     Toast.makeText(getApplicationContext(),  bAdapter.getItem(position)[3].toString(), Toast.LENGTH_SHORT).show();
                 }
             }
        });


             }
         }
