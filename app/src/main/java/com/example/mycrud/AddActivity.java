package com.example.mycrud;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import com.example.mycrud.interfaces.ApiService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddActivity extends AppCompatActivity {
    EditText editTextName, editTextEmail, editTextPassword, editTextPasswordConfirme;
    Button buttonAdd;
    SQLiteDatabase bancoDados;
    TextView textView;
    private static final String BASE_URL = "http://10.0.2.2:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editPassword);
        editTextPasswordConfirme = findViewById(R.id.editPasswordConfirme);
        buttonAdd = findViewById(R.id.buttonCadastrar);
        textView = findViewById(R.id.loginClick);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realizarChamadaAPI();
                abrirTelaLogin();
            }
        });
    }

    private void realizarChamadaAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<Double> chamada = apiService.add(Double.valueOf(5).toString(), Double.valueOf(5).toString());

        chamada.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.isSuccessful()) {
                    Double resultado = response.body();
                    Log.d(TAG, "Resultado da adição: " + resultado);
                } else {
                    // Exiba detalhes sobre o erro
                    Log.e(TAG, "Erro na chamada da API. Código de erro: " + response.code());
                    try {
                        Log.e(TAG, "Erro na chamada da API. Mensagem de erro: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                // Exiba detalhes sobre a falha
                Log.e(TAG, "Falha na comunicação com a API", t);
            }
        });
    }

    public void abrirTelaLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void cadastrar() {
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "INSERT INTO cadastros (nome,email,senha) VALUES (?,?,?)";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindString(1, editTextName.getText().toString());
            stmt.bindString(2, editTextEmail.getText().toString());
            stmt.bindString(3, editTextPassword.getText().toString());
            stmt.executeInsert();
            System.out.println("cadastrou");
            bancoDados.close();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
