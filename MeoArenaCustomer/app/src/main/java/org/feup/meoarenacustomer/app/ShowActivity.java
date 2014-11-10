package org.feup.meoarenacustomer.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ShowActivity extends Activity implements NumberPicker.OnValueChangeListener {

    API api;
    Storage db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        db = new Storage(this);
        api = new API();
        Intent intent = getIntent();
        setTitle(intent.getStringExtra("name"));

        fillScreen();
        buyTickets();
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

        NumberPicker numberpicker = (NumberPicker)findViewById(R.id.number_to_buy);
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

    public void buyTickets() {
        Button button = (Button) findViewById(R.id.buyTickets);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {
                askPin();
            }
        });
    }

    public void askPin() {
        // Ask for pin
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Pin");
        alert.setMessage("Por favor insira o pin para confirmar a operação.");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent intent = getIntent();
                String showID = intent.getStringExtra("showID");
                String customerID = db.get("id");
                String pin = input.getText().toString();
                NumberPicker np = (NumberPicker) findViewById(R.id.number_to_buy);
                callAPI(showID, customerID, pin, np.getValue());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(getApplicationContext(), "Operação Cancelada", Toast.LENGTH_SHORT).show();
            }
        });

        alert.show();
    }

    public void callAPI(final String showID, final String customerID, String pin, final Integer tickets_number) {
        api.buyTickets(customerID, showID, tickets_number,  pin, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Toast.makeText(getApplicationContext(), R.string.success_buy_tickets, Toast.LENGTH_SHORT).show();
                String customerID = db.get("id");
                String[][] allTickets = new String[response.length()][];
                for (int i = 0; i < response.length(); i++) {
                    JSONObject obj = null;
                    try {
                        String[] ticket = new String[7];
                        obj = response.getJSONObject(i);
                        ticket[0] = obj.getString("id");
                        ticket[1] = customerID;
                        ticket[2] = obj.getString("showID");
                        ticket[3] = obj.getString("seat");
                        ticket[4] = obj.getString("status");
                        ticket[5] = obj.getString("date_show");
                        ticket[6] = obj.getString("name");
                        allTickets[i] = ticket;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                saveVouchers(customerID);
                saveTickets(allTickets);
                Intent intent = new Intent(ShowActivity.this, HomeActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(getApplicationContext(), R.string.invalid_buy_tickets, Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void saveTickets(String[][] allTickets) {
        for (int i = 0; i < allTickets.length; i++) {
            // Order is: id, customerid, showid, seat, status, date
            db.saveTicket(allTickets[i][0], allTickets[i][1], allTickets[i][2], allTickets[i][3], allTickets[i][4], allTickets[i][5], allTickets[i][6]);
        }
        Toast.makeText(getApplicationContext(), R.string.save_tickets, Toast.LENGTH_SHORT).show();
    }

    private void saveVouchers(String customerID) {
        api.getVouchers(customerID, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    JSONObject obj = null;
                    try {
                        obj = response.getJSONObject(i);
                        db.saveVoucher(obj.getString("id"), db.get("id"), obj.getString("product"),
                                obj.getString("discount"), obj.getString("status"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getApplicationContext(), R.string.save_vouchers, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), R.string.no_vouchers, Toast.LENGTH_SHORT).show();
            }

        });
    }

}
