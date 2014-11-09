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

        NdefRecord record = msg.getRecords()[0];
        String payload = new String(record.getPayload());

        Intent i = new Intent(this, ReceiveActivity.class);
        TextView text = (TextView) findViewById(R.id.validation);
        text.setText(payload);

        String[] message = payload.split(" ");
        if (message[0].equals("True")) {
            Intent in = new Intent(this, TicketsActivity.class);
            in.putExtra("positions", message[1]);
            startActivity(in);
        }
        Toast.makeText(getApplicationContext(), payload, Toast.LENGTH_SHORT).show();

    }
}
