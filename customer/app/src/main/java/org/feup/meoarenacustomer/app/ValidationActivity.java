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
        return super.onOptionsItemSelected(item);
    }

    public void setup() {
        Intent intent = getIntent();
        String origin = intent.getStringExtra("origin");
        Button receive = (Button) findViewById(R.id.receive);
        Button send = (Button) findViewById(R.id.send);
        if (origin.equals("ticket")) {
            receive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    receiveTickets();
                }
            });
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendTickets();
                }
            });
        }
        else if (origin.equals("order")){
            receive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    receiveOrders();
                }
            });
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendOrders();
                }
            });
        }
    }

    public void receiveOrders() {
        Intent i = new Intent(this, ReceiveActivity.class);
        startActivity(i);
    }

    public void sendOrders() {
        Intent i = new Intent(this, SendActivity.class);
        i.putExtra("origin", "order");
        i.putExtra("pin", getIntent().getStringExtra("pin"));
        i.putExtra("customerID", getIntent().getStringExtra("customerID"));
        i.putExtra("products", getIntent().getStringExtra("products"));
        i.putExtra("vouchers", getIntent().getStringExtra("vouchers"));
        i.putExtra("quantity", getIntent().getStringExtra("quantity"));
        i.putExtra("price", getIntent().getStringExtra("price"));
        i.putExtra("position", getIntent().getStringExtra("position"));
        startActivity(i);
    }

    public void receiveTickets() {
        Intent i = new Intent(this, ReceiveActivity.class);
        startActivity(i);
    }

    public void sendTickets() {
        Intent i = new Intent(this, SendActivity.class);
        i.putExtra("origin", "ticket");
        i.putExtra("tickets", getIntent().getStringExtra("tickets"));
        i.putExtra("customerID", getIntent().getStringExtra("customerID"));
        i.putExtra("positions", getIntent().getStringExtra("positions"));
        startActivity(i);
    }
}
