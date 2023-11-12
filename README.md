Tutorial: Integrando Retrofit no Android para Chamadas a uma API Spring Boot
Neste tutorial, vamos guiar você através do processo de integrar o Retrofit em um aplicativo Android para fazer chamadas a uma API Spring Boot. O objetivo é realizar operações simples, como uma adição, para ilustrar o processo de comunicação entre o aplicativo Android e a API Spring Boot.

Pré-requisitos:

Uma API Spring Boot com um endpoint /add configurado para realizar uma adição simples.
Android Studio instalado no seu ambiente de desenvolvimento.
Passo 1: Configurar a API Spring Boot
Certifique-se de que sua API Spring Boot está configurada corretamente e está em execução.

Passo 2: Adicionar Dependências no Android Studio
Abra o arquivo build.gradle (Módulo: app) do seu projeto Android e adicione as seguintes dependências do Retrofit:

gradle
Copy code
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
Sincronize o projeto para aplicar as alterações.

Passo 3: Configurar o Manifest
Abra o arquivo AndroidManifest.xml e adicione a permissão da Internet e a configuração de segurança de rede:

xml
Copy code
<uses-permission android:name="android.permission.INTERNET" />

<application
    ...
    android:networkSecurityConfig="@xml/network_security_config">
    ...
</application>
Passo 4: Configurar o Modelo de Dados
Crie uma classe para representar os dados que você receberá da API. Para este exemplo simples, um modelo de dados pode não ser necessário.

Passo 5: Criar a Interface da API com Retrofit
Crie uma interface chamada ApiService que descreva as operações que você deseja realizar na API. Neste caso, uma operação de adição:

java
Copy code
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/add")
    Call<Double> add(@Query("num1") String num1, @Query("num2") String num2);
}
Passo 6: Configurar a Activity para Realizar Chamadas à API
Atualize sua AddActivity para usar Retrofit para chamar a API:

java
Copy code
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mycrud.interfaces.ApiService;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddActivity extends AppCompatActivity {
    EditText editTextName, editTextEmail, editTextPassword, editTextPasswordConfirm;
    Button buttonAdd;
    TextView textView;
    private static final String BASE_URL = "http://10.0.2.2:8080";
    private static final String TAG = "AddActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editPassword);
        editTextPasswordConfirm = findViewById(R.id.editPasswordConfirm);
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

        // Substitua os valores abaixo pelos dados desejados
        Call<Double> chamada = apiService.add("5", "5");

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
        // Implemente a lógica de cadastro aqui
    }
}
Este tutorial fornece uma base sólida para integrar o Retrofit em seu projeto Android e realizar chamadas a uma API Spring Boot. Personalize conforme necessário para atender às necessidades específicas do seu aplicativo.