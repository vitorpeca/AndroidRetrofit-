package com.example.mycrud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    Integer id;
    EditText editTextName, editTextIdade;
    Button buttonEdit;
    SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextIdade = (EditText) findViewById(R.id.editTextEmail);
        buttonEdit = (Button) findViewById(R.id.buttonCadastrar);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editar();
            }
        });

        carregarDados();
    }

    public void carregarDados(){
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            Cursor meuCursor = bancoDados.rawQuery("SELECT id, nome, idade FROM pessoa WHERE id = " + id.toString() , null);
            meuCursor.moveToFirst();
            editTextName.setText(meuCursor.getString(1));
            editTextIdade.setText(meuCursor.getString(2));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void editar(){
        try{
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "UPDATE pessoa SET nome=?,idade=? WHERE ID=?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindString(1,editTextName.getText().toString());
            stmt.bindLong(2, Integer.parseInt(editTextIdade.getText().toString()) );
            stmt.bindLong(3, id );
            stmt.executeUpdateDelete();
            bancoDados.close();
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}