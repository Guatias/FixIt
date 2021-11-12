package com.example.fixit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import models.Servico;

public class Novo_Servico_Activity extends AppCompatActivity {

    private UserHelperClass user;
    private Servico novo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_servico);

        Bundle extras = getIntent().getExtras();
        String tipo = extras.getString("tipo");

        ImageView novo_serv_icon = (ImageView) findViewById(R.id.new_serv_icon);
        TextView novo_serv_tv = (TextView) findViewById(R.id.new_serv_tv);
        EditText novo_serv_problema = (EditText) findViewById(R.id.new_serv_problem);
        EditText novo_serv_descricao = (EditText) findViewById(R.id.new_serv_description);

        MaterialButton confirmbtn = (MaterialButton) findViewById(R.id.new_serv_confirm_button);
        MaterialButton cancelbtn = (MaterialButton) findViewById(R.id.new_serv_cancel_button);

        if(tipo.equals("Eletrico")){
            novo_serv_icon.setImageResource(R.drawable.idea);
            novo_serv_tv.setText("Novo Serviço Elétrico");
        } else {
            novo_serv_icon.setImageResource(R.drawable.wrench);
            novo_serv_tv.setText("Novo Serviço Mecânico");
        }

        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String problema_string = novo_serv_problema.getText().toString();
                String descricao_string = novo_serv_descricao.getText().toString();

                if (TextUtils.isEmpty(problema_string)) {
                    novo_serv_problema.setError("Preencha com seu problema");
                    return;
                }

                if (TextUtils.isEmpty(descricao_string)) {
                    novo_serv_descricao.setError("Preencha com a descrição de seu problema");
                    return;
                }

                user = getIntent().getParcelableExtra("user");

                novo = new Servico();
                novo.setProblema(problema_string);
                novo.setDescricao(descricao_string);
                novo.setTipo(tipo);
                novo.setEmail(user.getEmail());
                novo.setId(UUID.randomUUID().toString());

                try {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("servicos");

                    myRef.child(novo.getId()).setValue(novo);

                } catch (Exception ex) {
                    Toast.makeText(Novo_Servico_Activity.this, "Ocorreu um erro: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

                Cliente_Activity.ca.finish();
                Intent cliente_activity = new Intent(getApplicationContext(), Cliente_Activity.class);
                cliente_activity.putExtra("user", user);
                startActivity(cliente_activity);
                finish();
            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = getIntent().getParcelableExtra("user");
                Intent cliente_activity = new Intent(getApplicationContext(), Cliente_Activity.class);
                cliente_activity.putExtra("user", user);
                startActivity(cliente_activity);
                finish();
            }
        });

        novo_serv_problema.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    novo_serv_problema.setHint("");
                else
                    novo_serv_problema.setHint("Problema");
            }
        });

        novo_serv_descricao.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    novo_serv_descricao.setHint("");
                else
                    novo_serv_descricao.setHint("Descrição");
            }
        });

    }
}