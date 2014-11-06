package org.feup.meoarenacustomer.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


public class TicketsActivity extends Activity {

    API api;
    Storage db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        db = new Storage(this);
        api = new API();

        populateTicketsView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tickets, menu);
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

    public void populateTicketsView() {
        String customerID = db.get("id");
        String[][] tickets = db.getTickets(customerID);
        ListView listView = (ListView) findViewById(R.id.view_tickets);
        TicketsAdapter adapter = new TicketsAdapter(tickets, this);
        listView.setAdapter(adapter);

    }
}
