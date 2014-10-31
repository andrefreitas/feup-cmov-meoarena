package org.feup.meoarenacustomer.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


public class ShowActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent intent = getIntent();
        setTitle(intent.getStringExtra("name"));

        fillScreen();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show, menu);
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

    public void fillScreen() {
        Intent intent = getIntent();
        intent.getStringExtra("price");
        intent.getStringExtra("date");
        intent.getStringExtra("seats");

        TextView tickets_number = (TextView)findViewById(R.id.tickets_number);
        tickets_number.setText("21");

        TextView date = (TextView)findViewById(R.id.date_onViewShow);
        date.setText(intent.getStringExtra("date"));

        NumberPicker numberpicker = (NumberPicker)findViewById(R.id.number_buy);
        numberpicker.setMaxValue(10);
        numberpicker.setMinValue(1);
        numberpicker.setValue(1);
        TextView total = (TextView)findViewById(R.id.price);
        int value = numberpicker.getValue() * intent.getIntExtra("price", 0);
        total.setText(value + " EUR");

    }
}
