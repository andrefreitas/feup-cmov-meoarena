package org.feup.terminal.app;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Window;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReceiveActivity extends Activity {
  NfcApp app;
  API api;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        app = (NfcApp) getApplication();
        api = new API();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }

        finish();
    }
  
    @Override
    public void onNewIntent(Intent intent) {
    setIntent(intent);
  }

    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];

        if (msg.getRecords().length >0) {
            String origin = new String(msg.getRecords()[0].getPayload());

            if (origin.equals("ticket")) {
                String tickets = new String(msg.getRecords()[1].getPayload());
                String customerID = new String(msg.getRecords()[2].getPayload());
                String positions = new String(msg.getRecords()[3].getPayload());

                receiveTickets(tickets, customerID, positions);
            }

            else if (origin.equals("order")) {
                String pin = new String(msg.getRecords()[1].getPayload());
                String customerID = new String(msg.getRecords()[2].getPayload());
                String products = new String(msg.getRecords()[3].getPayload());
                String vouchers = new String(msg.getRecords()[4].getPayload());
                String quantity = new String(msg.getRecords()[5].getPayload());
                String price = new String(msg.getRecords()[6].getPayload());
                String position = new String(msg.getRecords()[7].getPayload());

                receiveOrders(pin, customerID, products, vouchers, quantity, price, position);
            }
        }
    }

    public void receiveTickets(final String tickets, String customerID, final String positions) {
        api.validateTickets(customerID, tickets, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                app.reply = "Bilhetes validos!";
                app.tickets = tickets;
                app.positions = positions;
                startIntent();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                app.reply = "Bilhetes nao validos.";
                app.tickets = tickets;
                app.positions = positions;
                startIntent();
            }
        });
    }

    public void receiveOrders(String pin, String customerID, String products, String vouchers, String quantity, String price, final String position) {

        api.validateOrders(pin, customerID, vouchers, products, quantity, price, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    String orderID = response.getString("id");
                    String products = "";
                    String vouchers = "";
                    String vouchersName = "";
                    JSONArray products_list = response.getJSONArray("products");
                    for (int i = 0; i < products_list.length(); i++) {
                        JSONObject p = products_list.getJSONObject(i);
                        if (i == products_list.length()-1) {
                            products += p.getString("name");
                        } else {
                            products += p.getString("name") + ",";
                        }
                    }
                    JSONArray vouchers_list = response.getJSONArray("vouchers");
                    for (int i = 0; i < vouchers_list.length(); i++) {
                        JSONObject p = vouchers_list.getJSONObject(i);
                        if (i == vouchers_list.length()-1) {
                            vouchers += p.getJSONObject("_id").getString("$oid");
                            vouchersName += p.getString("product");
                        } else {
                            vouchers += p.getJSONObject("_id").getString("$oid") + ",";
                            vouchersName += p.getString("product") + ",";
                        }
                    }
                    app.orderID = orderID;
                    app.products = products;
                    if (vouchers.equals("")) {
                        app.vouchers = "novouchers";
                    } else {
                        app.vouchers = vouchers;
                    }
                    app.price = response.getString("price");
                    app.position = position;
                    if (vouchersName.equals("")) {
                        app.vouchersName = "nonames";
                    } else {
                        app.vouchersName = vouchersName;
                    }
                    app.reply = "Encomenda validada!";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), R.string.success_order, Toast.LENGTH_SHORT).show();
                startIntent();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                startIntent();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                app.reply = "Encomenda nao validada!";
                Toast.makeText(getApplicationContext(), R.string.error_order, Toast.LENGTH_SHORT).show();
                startIntent();
            }
        });
    }

    public void startIntent() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }



}
