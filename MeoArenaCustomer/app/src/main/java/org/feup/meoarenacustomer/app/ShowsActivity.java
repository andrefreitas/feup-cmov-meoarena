package org.feup.meoarenacustomer.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ShowsActivity extends Activity {

    API api;
    Button showButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shows);
        setTitle("Espetáculos");

        api = new API();
        listShows();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shows, menu);
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

    public void listShows() {
        api.getShows(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                String[][] allShows = new String[response.length()][];
                for (int i = 0; i < response.length(); i++) {

                    JSONObject obj = null;
                    try {
                        String[] show = new String[6];
                        obj = response.getJSONObject(i);
                        show[0] = obj.getString("name");
                        show[1] = obj.getString("price");
                        show[2] = obj.getString("date");
                        show[3] = obj.getString("seats");
                        show[4] = obj.getString("id");
                        show[5] = obj.getString("available");
                        allShows[i] = show;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                populateListView(allShows);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), R.string.no_shows, Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void populateListView(String[][] content) {
        ListView listView = (ListView) findViewById(R.id.view_shows);
        ShowsAdapter adapter = new ShowsAdapter(content, this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                String name = ((TextView) view.findViewById(R.id.show_name)).getText().toString();
                String price = ((TextView) view.findViewById(R.id.show_price)).getText().toString();
                String date = ((TextView) view.findViewById(R.id.show_date)).getText().toString();
                String seats = ((TextView) view.findViewById(R.id.show_seats)).getText().toString();
                String available = ((TextView) view.findViewById(R.id.show_available)).getText().toString();
                String showID = ((TextView) view.findViewById(R.id.show_id)).getText().toString();

                if (price.equals("Esgotado")) {
                    Toast.makeText(getApplicationContext(), R.string.no_tickets, Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getBaseContext(), ShowActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("price", price.split(" ")[0]);
                    intent.putExtra("date", date);
                    intent.putExtra("seats", seats);
                    intent.putExtra("available", available);
                    intent.putExtra("showID", showID);
                    startActivity(intent);
                }
            }
        });
    }

}
