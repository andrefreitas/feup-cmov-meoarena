package org.feup.meoarenacustomer.app;

import java.nio.charset.Charset;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class SendActivity extends Activity implements NfcAdapter.OnNdefPushCompleteCallback {
    NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_send_tickets);

        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(getApplicationContext(), "NFC is not available on this device.", Toast.LENGTH_LONG).show();
            finish();
        }


        Bundle extras = getIntent().getExtras();
        if (extras.getString("origin").equals("ticket")) {
            sendTicketMessage();
        }

        if (extras.getString("origin").equals("order")) {
           sendOrderMessage();
        }
    }

    public void sendTicketMessage() {
        Bundle extras = getIntent().getExtras();
        String tag = "application/nfc.feup.apm.message.type1";

        byte[] message =  extras.getString("tickets").getBytes();
        byte[] customerID = extras.getString("customerID").getBytes();
        byte[] positions = extras.getString("positions").getBytes();
        NdefMessage msg = new NdefMessage(new NdefRecord[] { createMimeRecord(tag, message),
                createMimeRecord(tag, customerID),
                createMimeRecord(tag, positions)});

        // Register a NDEF message to be sent in a beam operation (P2P)
        mNfcAdapter.setNdefPushMessage(msg, this);
        mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
    }

    public void sendOrderMessage(){

    }

    public void onNdefPushComplete(NfcEvent arg0) {
        runOnUiThread(new Runnable() {
            public void run() {
            Toast.makeText(getApplicationContext(), "Message sent.", Toast.LENGTH_LONG).show();
            finish();
            }
        });
    }

    public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("ISO-8859-1"));
        NdefRecord mimeRecord = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
        return mimeRecord;
    }
}

