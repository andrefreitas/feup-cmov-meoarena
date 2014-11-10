package org.feup.terminal.app;

import java.nio.charset.Charset;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class SendActivity extends Activity implements NfcAdapter.OnNdefPushCompleteCallback {
    NfcApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NfcAdapter mNfcAdapter;
        String tag;
        byte[] message;

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_nfc_send);

        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
        Toast.makeText(getApplicationContext(), "NFC is not available on this device.", Toast.LENGTH_LONG).show();
        finish();
        }

        app = (NfcApp) getApplication();
        Bundle extras = getIntent().getExtras();
        tag = "application/nfc.feup.apm.message.type1";
        message = extras.getString("message").getBytes();
        byte[] origin = extras.getString("origin").getBytes();
        NdefMessage msg = new NdefMessage(new NdefRecord[] {
                createMimeRecord(tag, origin),
                createMimeRecord(tag, message) });

        // Register a NDEF message to be sent in a beam operation (P2P)
        mNfcAdapter.setNdefPushMessage(msg, this);
        mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
        app.reply = "Entered NfcSend";
    }
  
    public void onNdefPushComplete(NfcEvent arg0) {
        app.reply = "";
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
