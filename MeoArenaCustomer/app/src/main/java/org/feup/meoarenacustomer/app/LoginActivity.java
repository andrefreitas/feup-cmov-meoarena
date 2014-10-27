package org.feup.meoarenacustomer.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import org.json.*;
import com.loopj.android.http.*;
import org.apache.http.Header;

public class LoginActivity extends Activity {
    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;
    Button registerButton;
    String email;
    String password;
    API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        api = new API();
        login();
        register();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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

    public void login(){
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();

                if (!Helpers.isValidEmail(email)) {
                    Toast.makeText(getApplicationContext(), R.string.invalid_email, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Helpers.isValidPassword(password)) {
                    Toast.makeText(getApplicationContext(), R.string.invalid_password, Toast.LENGTH_SHORT).show();
                    return;
                }


                api.login(email, password, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                        try {
                            String id = response.getString("id");
                            Log.d("LoginActivity",  "Login success with customer id  " + id);
                            Toast.makeText(getApplicationContext(), "ID: " + id, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("LoginActivity", "Failed to get JSON Object String 'id' ");
                            Toast.makeText(getApplicationContext(), "Failed to get JSON Object String 'id' ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
                        Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), R.string.wrong_login, Toast.LENGTH_SHORT).show();
                    }

                });


            }
        });

    }

    public void register(){
        registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
    }

}
