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

public class ReceiveTickets extends Activity {
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
            final String tickets = new String(msg.getRecords()[0].getPayload());
            String customerID = new String(msg.getRecords()[1].getPayload());
            final String positions = new String(msg.getRecords()[2].getPayload());

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
    }

    public void startIntent() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }



}
