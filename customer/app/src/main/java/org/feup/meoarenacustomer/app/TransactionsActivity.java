package org.feup.meoarenacustomer.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TransactionsActivity extends Activity {

    API api;
    Storage db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        api = new API();
        db = new Storage(this);

        String customerID = db.get("id");
        getTransactions(customerID);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.transactions, menu);
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

    public void getTransactions(String customerID) {
        api.getTransactions(customerID, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                String[][] allTransactions = new String[response.length()][];
                for (int i = 0; i < response.length(); i++) {
                    JSONObject obj = null;
                    try {
                        String[] transaction = new String[4];
                        obj = response.getJSONObject(i);
                        transaction[0] = obj.getString("id");
                        transaction[1] = obj.getString("description");
                        transaction[2] = obj.getString("amount");
                        transaction[3] = obj.getString("date");
                        allTransactions[i] = transaction;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                listTransactions(allTransactions);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void listTransactions(String[][] transactions) {
        ListView listView = (ListView) findViewById(R.id.view_transactions);
        TransactionsAdapter adapter = new TransactionsAdapter(transactions, this);
        listView.setAdapter(adapter);
    }
}
