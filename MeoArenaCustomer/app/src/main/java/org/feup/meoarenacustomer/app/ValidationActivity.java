package org.feup.meoarenacustomer.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class ValidationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

        setup();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.validation, menu);
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

    public void setup() {
        Intent intent = getIntent();
        String origin = intent.getStringExtra("origin");
        Button receiveTickets = (Button) findViewById(R.id.receive_tickets);
        Button sendTickets = (Button) findViewById(R.id.send_tickets);
        if (origin.equals("ticket")) {
            receiveTickets.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "Antes da chamada ao receive tickets", Toast.LENGTH_SHORT).show();
                    receiveTickets();
                }
            });

            sendTickets.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendTickets();
                }
            });
        }
    }

    public void receiveTickets() {
        Toast.makeText(getApplicationContext(), "Antes do intent tickets", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, ReceiveTicketsValidActivity.class);
        startActivity(i);
        Toast.makeText(getApplicationContext(), "Depois do intent", Toast.LENGTH_SHORT).show();
    }

    public void sendTickets() {
        Intent i = new Intent(this, SendTicketsActivity.class);
        i.putExtra("tickets", getIntent().getStringExtra("tickets"));
        i.putExtra("customerID", getIntent().getStringExtra("customerID"));
        i.putExtra("positions", getIntent().getStringExtra("positions"));
        startActivity(i);
    }
}
