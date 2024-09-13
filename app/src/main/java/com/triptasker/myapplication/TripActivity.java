package com.triptasker.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

public class TripActivity extends AppCompatActivity {
    private TextView headerTitle;
    private ImageView navigationIcon;
    private Button btnAdicionarViagem, btnCriarViagem;
    private EditText etNomeViagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        headerTitle = findViewById(R.id.header_title);
        navigationIcon = findViewById(R.id.icon_navigation);
        navigationIcon.setImageResource(R.drawable.baseline_arrow_back_ios_24);
        btnAdicionarViagem = findViewById(R.id.btnAdicionarViagem);
        etNomeViagem = findViewById(R.id.etNomeViagem);
        btnCriarViagem = findViewById(R.id.btnCriarViagem);


        navigationIcon.setOnClickListener(v -> {
            finish();
        });

        btnAdicionarViagem.setOnClickListener(v -> {
            btnAdicionarViagem.setVisibility(View.GONE);

            etNomeViagem.setVisibility(View.VISIBLE);
            btnCriarViagem.setVisibility(View.VISIBLE);
        });


        btnCriarViagem.setOnClickListener(v -> {
            String nomeViagem = etNomeViagem.getText().toString().trim();

            if (!nomeViagem.isEmpty()) {

                etNomeViagem.setVisibility(View.GONE);
                btnCriarViagem.setVisibility(View.GONE);

                btnAdicionarViagem.setVisibility(View.VISIBLE);

                etNomeViagem.setText("");

                Toast.makeText(this, "Viagem '" + nomeViagem + "' adicionada!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Por favor, insira o nome da viagem", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onTripClicked(View view) {
        try {
            String tripName = "";
            if (view.findViewById(R.id.tvNomeViagemBalneario) != null) {
                tripName = ((TextView) view.findViewById(R.id.tvNomeViagemBalneario)).getText().toString();
            } else if (view.findViewById(R.id.tvNomeViagemBuenosAires) != null) {
                tripName = ((TextView) view.findViewById(R.id.tvNomeViagemBuenosAires)).getText().toString();
            } else if (view.findViewById(R.id.tvNomeViagemItalia) != null) {
                tripName = ((TextView) view.findViewById(R.id.tvNomeViagemItalia)).getText().toString();
            }
            int tripId = getTripIdByName(tripName);

            Intent intent = new Intent(this, TasksActivity.class);
            intent.putExtra("tripId", tripId);
            intent.putExtra("tripName", tripName);
            startActivity(intent);
        } catch (Exception e) {
            Log.e("TripActivity", "Erro ao clicar na viagem: ", e);
        }
    }

    private int getTripIdByName(String tripName) {
        if (tripName.equals("Balneário Camboriú")) return 1;
        if (tripName.equals("Viagem a Buenos Aires")) return 2;
        if (tripName.equals("Itália 2025")) return 3;
        return -1;
    }
}