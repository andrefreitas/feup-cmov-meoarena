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


public class ShowActivity extends Activity implements NumberPicker.OnValueChangeListener {

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

        TextView tickets_number = (TextView)findViewById(R.id.tickets_number);
        tickets_number.setText(intent.getStringExtra("available"));

        TextView date = (TextView)findViewById(R.id.date_onViewShow);
        date.setText(intent.getStringExtra("date"));

        NumberPicker numberpicker = (NumberPicker)findViewById(R.id.number_buy);
        numberpicker.setMaxValue(Integer.parseInt(tickets_number.getText().toString()));
        numberpicker.setMinValue(1);
        numberpicker.setValue(1);
        TextView total = (TextView)findViewById(R.id.price);
        double value = numberpicker.getValue() * Double.parseDouble(intent.getStringExtra("price"));
        total.setText(value + " EUR");
        numberpicker.setOnValueChangedListener(this);

    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i2) {
        Intent intent = getIntent();
        TextView total = (TextView)findViewById(R.id.price);
        double value = numberPicker.getValue() * Double.parseDouble(intent.getStringExtra("price"));
        total.setText(value + " EUR");
    }
}
