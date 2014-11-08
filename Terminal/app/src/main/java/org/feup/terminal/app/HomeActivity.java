package org.feup.terminal.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class HomeActivity extends Activity implements View.OnClickListener{

    private NfcApp app;
    private TextView replyMsg;
    private EditText sendMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (NfcApp) getApplication();
        replyMsg = (TextView) findViewById(R.id.reply);
        replyMsg.setText(app.reply);
        sendMsg = (EditText) findViewById(R.id.msg);
        findViewById(R.id.button1).setOnClickListener(this);
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, NfcSend.class);
        intent.putExtra("message", sendMsg.getText().toString().getBytes());
        intent.putExtra("tag", "application/nfc.feup.apm.message.type1");
        startActivity(intent);
    }

    public void onResume() {
        super.onResume();
        replyMsg.setText(app.reply);
    }
}
