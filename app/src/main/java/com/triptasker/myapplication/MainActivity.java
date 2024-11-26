package com.triptasker.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private EditText txtUsuario, txtSenha;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUsuario = findViewById(R.id.txtUser);
        txtSenha = findViewById(R.id.txtPassword);

        preferences = getSharedPreferences("Shared", Context.MODE_PRIVATE);
        boolean session = preferences.getBoolean("session", false);

        if (session) {
            String user = preferences.getString("user", "");
            Intent intent = new Intent(MainActivity.this, TripActivity.class);
            intent.putExtra("usuario", user);
            startActivity(intent);
        }
    }

    public void loginClick(View view) {

        String user = txtUsuario.getText().toString();
        String pwd = txtSenha.getText().toString();

        if (user.isEmpty() || pwd.isEmpty()) {
            showMessage("Usuário ou senha não preenchidos");
            return;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username", user);
        params.put("password", pwd);

        client.post("http://10.0.2.2:45455/ApiLogin.aspx", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("user", user);
                editor.putBoolean("session", true);
                editor.apply();

                Intent intent = new Intent(MainActivity.this, TripActivity.class);
                intent.putExtra("usuario", user);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (statusCode == 401) {
                    String result = new String(responseBody);
                    showMessage(result);
                } else {
                    showMessage(error.toString());
                }
            }
        });
    }

    private void showMessage(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
