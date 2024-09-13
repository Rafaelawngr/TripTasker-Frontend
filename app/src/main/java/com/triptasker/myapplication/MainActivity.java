package com.triptasker.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private EditText txtUsuario, txtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_trip);

        txtUsuario = findViewById(R.id.txtUser);
        txtSenha = findViewById(R.id.txtPassword);
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

        params.put("usuario", user);
        params.put("password", pwd);

        client.post("http://192.168.100.1:53626/ApiLogin.aspx", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Intent intent = new Intent(MainActivity.this, TripActivity.class);
                intent.putExtra("usuario", user);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showMessage(error.toString());
            }
        });
    }

    private void showMessage(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}