package org.feup.terminal.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Options menu
        startValidateTickets();
        startValidateOrders();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
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

    public void startValidateTickets() {
        ImageButton validateTickets = (ImageButton) findViewById(R.id.validateTickets);
        validateTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startHome();
            }
        });
    }

    public void startHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void startValidateOrders() {
        ImageButton validateTickets = (ImageButton) findViewById(R.id.validateOrders);
        validateTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startHome();
            }
        });
    }
}
