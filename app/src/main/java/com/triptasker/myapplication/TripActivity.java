package com.triptasker.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TripActivity extends AppCompatActivity {
    private TextView welcome, headerTitle, tvNomeViagem;
    private ImageView navigationIcon;
    private Button btnAdicionarViagem, btnCriarViagem, btnLogout;
    private EditText etNomeViagem;
    private SharedPreferences preferences;
    private AsyncHttpClient client;
    private RecyclerView recyclerViewTrips;
    private TripAdapter tripAdapter;
    private List<Trip> tripList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        welcome = findViewById(R.id.welcome_title);
        headerTitle = findViewById(R.id.header_title);
        navigationIcon = findViewById(R.id.icon_navigation);
        navigationIcon.setImageResource(R.drawable.baseline_arrow_back_ios_24);
        btnAdicionarViagem = findViewById(R.id.btnAdicionarViagem);
        etNomeViagem = findViewById(R.id.etNomeViagem);
        tvNomeViagem = findViewById(R.id.tvNomeViagem);
        btnCriarViagem = findViewById(R.id.btnCriarViagem);
        btnLogout = findViewById(R.id.btnLogout);

        recyclerViewTrips = findViewById(R.id.recyclerViewTrips);
        recyclerViewTrips.setLayoutManager(new LinearLayoutManager(this));

        HeaderUtils.setupBackButton(this);

        Intent userIntent = getIntent();
        String user = userIntent.getStringExtra("usuario");

        welcome.setText("Olá " + user);

        tripAdapter = new TripAdapter(tripList, trip -> {
            Intent intent = new Intent(TripActivity.this, TasksActivity.class);
            intent.putExtra("tripId", trip.getId());
            intent.putExtra("tripName", trip.getTitle());
            startActivity(intent);
        });
        recyclerViewTrips.setAdapter(tripAdapter);

        btnAdicionarViagem.setOnClickListener(v -> {
            btnAdicionarViagem.setVisibility(View.GONE);
            etNomeViagem.setVisibility(View.VISIBLE);
            btnCriarViagem.setVisibility(View.VISIBLE);
        });

        btnCriarViagem.setOnClickListener(v -> {
            String nomeViagem = etNomeViagem.getText().toString().trim();
            if (!nomeViagem.isEmpty()) {
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("Action", "create");
                params.put("Title", nomeViagem);
                client.post("http://10.0.2.2:45457/ApiTrip.aspx", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(TripActivity.this, "Viagem criada com sucesso!", Toast.LENGTH_SHORT).show();
                        etNomeViagem.setText("teste");
                        loadTrips();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        String response = new String(responseBody);
                        Toast.makeText(TripActivity.this, "Erro: " + response, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Por favor, insira o nome da viagem", Toast.LENGTH_SHORT).show();
            }
        });

        navigationIcon.setOnClickListener(v -> finish());

        preferences = getSharedPreferences("Shared", Context.MODE_PRIVATE);
        client = new AsyncHttpClient();

        loadTrips();
    }

    private void loadTrips() {
        client.get("http://10.0.2.2:45457/ApiTrip.aspx", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);

                try {
                    Gson gson = new Gson();
                    Type tripListType = new TypeToken<List<Trip>>() {}.getType();
                    tripList = gson.fromJson(response, tripListType);
                    Log.d("TripActivity", "Viagens carregadas: " + tripList.size());

                    tripAdapter.updateTrips(tripList);

                } catch (JsonSyntaxException e) {
                    Toast.makeText(TripActivity.this, "Erro ao processar dados do servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("TripActivity", "Failed to load trips", error);
                Toast.makeText(TripActivity.this, "Erro ao carregar viagens", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteTrip(int tripId) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("Action", "delete");
        params.put("TripId", tripId);
        client.post("http://10.0.2.2:45457/ApiTrip.aspx", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(TripActivity.this, "Viagem excluída com sucesso!", Toast.LENGTH_SHORT).show();
                loadTrips();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = new String(responseBody);
                Toast.makeText(TripActivity.this, "Erro: " + response, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void logoutClick(View view) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user", "");
        editor.putBoolean("session", false);
        editor.apply();

        finishAffinity();
    }
}

