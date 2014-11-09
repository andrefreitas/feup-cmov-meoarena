package org.feup.terminal.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class HomeActivity extends Activity implements View.OnClickListener{

    private NfcApp app;
    private TextView replyMsg;
    private TextView sendMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (NfcApp) getApplication();
        replyMsg = (TextView) findViewById(R.id.reply);
        replyMsg.setText(app.reply);
        // Escolher o texto a enviar
        sendMsg = (TextView) findViewById(R.id.reply);
        findViewById(R.id.button1).setOnClickListener(this);
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, SendActivity.class);
        String message;



        // Enviar mensagem
        if (sendMsg.getText().toString().equals("Bilhetes validos!")) {
            message = "True "+app.positions + " " +app.tickets;
        } else {
            message = "False ";
        }
        intent.putExtra("message", message);
        intent.putExtra("tag", "application/nfc.feup.apm.message.type1");
        startActivity(intent);
    }

    //public void

    public void onResume() {
        super.onResume();
        replyMsg.setText(app.reply);
    }
}
