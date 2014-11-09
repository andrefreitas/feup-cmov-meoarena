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

import java.util.ArrayList;
import java.util.Iterator;


public class TicketsActivity extends Activity {

    API api;
    Storage db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        db = new Storage(this);
        api = new API();

        listTickets();
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

    public void testValidation(TicketsAdapter adapter) {
        Intent intent = getIntent();
        if (intent.getStringExtra("positions") != null) {
            String[] positions = intent.getStringExtra("positions").split(",");
            for (int i = 0; i < positions.length; i++) {
                adapter.disableCheck(Integer.parseInt(positions[i]));
            }
        }
    }

    public void listTickets() {
        String customerID = db.get("id");
        String[][] tickets = db.getTickets(customerID);
        final ListView listView = (ListView) findViewById(R.id.view_tickets);
        final TicketsAdapter adapter = new TicketsAdapter(tickets, this);
        listView.setAdapter(adapter);

        testValidation(adapter);

        Button validate = (Button) findViewById(R.id.validate_tickets);

        validate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ValidationActivity.class);
                String tickets = "";
                String positions = "";
                Iterator<String> it = adapter.getCheckedItems().values().iterator();
                int size = adapter.getCheckedItems().size();

                if (size > 4) {
                    Toast.makeText(getApplicationContext(), R.string.too_much_tickets, Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < size; i++) {
                        ArrayList<String> item = new ArrayList<String>();
                        Integer j = Integer.parseInt(it.next());
                        // Get ticket id
                        String id = adapter.getItem(j)[4];

                        if (i == size - 1) {
                            tickets += id;
                            positions += j;
                        } else {
                            tickets += id + ",";
                            positions += j + ",";
                        }

                    }

                    intent.putExtra("tickets", tickets);
                    intent.putExtra("positions", positions);
                    intent.putExtra("customerID", db.get("id"));
                    intent.putExtra("origin", "ticket");
                    startActivity(intent);
                }
            }
        });

    }
}
