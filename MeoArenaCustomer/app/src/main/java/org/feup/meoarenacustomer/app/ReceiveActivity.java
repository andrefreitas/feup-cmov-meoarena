package org.feup.meoarenacustomer.app;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;
import android.widget.Toast;


public class ReceiveActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_tickets);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
        //finish();
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];

        // Origin
        NdefRecord origin_record = msg.getRecords()[0];
        String origin = new String(origin_record.getPayload());

        if (origin.equals("ticket")) {
            // Data
            NdefRecord record = msg.getRecords()[1];
            String payload = new String(record.getPayload());

            receiveTickets(payload);
        }

        else if (origin.equals("order")) {
            // Data
            NdefRecord record = msg.getRecords()[1];
            String payload_or = new String(record.getPayload());

            receiveOrders(payload_or);
        }
    }

    public void receiveTickets(String payload){
        Intent i = new Intent(this, ReceiveActivity.class);

        String[] message = payload.split(" ");
        if (message[0].equals("True")) {
            Intent in = new Intent(this, TicketsActivity.class);
            in.putExtra("origin", "ticket");
            in.putExtra("positions", message[1]);
            in.putExtra("tickets", message[2]);
            startActivity(in);
        }
        else {
            Toast.makeText(getApplicationContext(), "Bilhetes invalidos", Toast.LENGTH_SHORT).show();
        }
    }

    public void receiveOrders(String payload) {
        Intent i = new Intent(this, ReceiveActivity.class);

        String[] message = payload.split(" ");
        if (message[0].equals("True")) {
            Intent in = new Intent(this, OrdersActivity.class);
            in.putExtra("origin", "order");
            in.putExtra("orderID", message[1]);
            in.putExtra("vouchers", message[2]);
            in.putExtra("products", message[3]);
            in.putExtra("price", message[4]);
            in.putExtra("position", message[5]);
            in.putExtra("vouchersName", message[6]);
            startActivity(in);
        }
        else {
            Toast.makeText(getApplicationContext(), "Encomenda invalida", Toast.LENGTH_SHORT).show();
        }
    }
}
