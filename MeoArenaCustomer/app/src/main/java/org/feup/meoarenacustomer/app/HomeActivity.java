package org.feup.meoarenacustomer.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class HomeActivity extends Activity {

    Storage db;
    ImageButton showsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new Storage(this);
        showPin();
        storeId();

        //On click category
        getShows();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showPin(){
        Intent intent = getIntent();
        String pin = intent.getStringExtra("pin");
        if(pin == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("O seu PIN é " +  pin)
                .setCancelable(false)
                .setTitle("Registo feito com sucesso!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void storeId(){
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        if(id!=null && !id.equals("")) {
            db.remove("id");
            db.put("id", id);
        };
    }

    public void getShows() {
        showsButton = (ImageButton) findViewById(R.id.list_shows);

        showsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ShowsActivity.class);
                startActivity(intent);
            }
        });
    }
}
