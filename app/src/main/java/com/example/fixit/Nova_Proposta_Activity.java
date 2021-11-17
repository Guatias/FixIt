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

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import models.Proposta;
import models.Servico;

public class Nova_Proposta_Activity extends AppCompatActivity {

    private UserHelperClass user;
    private UserHelperClass user_task;
    private Servico servico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_proposta);

        ImageView prop_serv_icon = (ImageView) findViewById(R.id.prop_serv_icon);
        TextView prop_serv_tv = (TextView) findViewById(R.id.prop_serv_tv);
        EditText prop_criado_por = (EditText) findViewById(R.id.prop_created_by);
        EditText prop_serv_problema = (EditText) findViewById(R.id.prop_serv_problem);
        EditText prop_serv_descricao = (EditText) findViewById(R.id.prop_serv_description);
        EditText prop_serv_valor = (EditText) findViewById(R.id.prop_serv_value);

        MaterialButton enviarbtn = (MaterialButton) findViewById(R.id.prop_serv_send_button);

        Bundle extras = getIntent().getExtras();

        servico = getIntent().getParcelableExtra("servico");
        user = getIntent().getParcelableExtra("user");
        user_task = getIntent().getParcelableExtra("user_task");

        prop_criado_por.setText("Proposta para: " + user_task.getNome() + " " + user_task.getSobrenome());
        prop_serv_problema.setText("Problema: " + servico.getProblema());
        prop_criado_por.setEnabled(false);
        prop_serv_problema.setEnabled(false);

        if (servico.getTipo().equals("Eletrico")) {
            prop_serv_icon.setImageResource(R.drawable.idea);
            prop_serv_tv.setText("Serviço Elétrico");
        } else {
            prop_serv_icon.setImageResource(R.drawable.wrench);
            prop_serv_tv.setText("Serviço Mecânico");
        }

        prop_serv_descricao.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    prop_serv_descricao.setHint("");
                else
                    prop_serv_descricao.setHint("Proposta");
            }
        });

        prop_serv_valor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    prop_serv_valor.setHint("");
                } else {
                    if (prop_serv_valor.getText().toString().equals("0.00")) prop_serv_valor.setText("");
                    prop_serv_valor.setHint("Valor da Proposta (R$)");
                }
            }
        });

        enviarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String prop_serv_descricao_string = prop_serv_descricao.getText().toString();
                String prop_serv_valor_string = prop_serv_valor.getText().toString();

                if (TextUtils.isEmpty(prop_serv_descricao_string)) {
                    prop_serv_descricao.setError("Preencha com sua proposta");
                    return;
                }

                if (TextUtils.isEmpty(prop_serv_valor_string)) {
                    prop_serv_valor.setError("Insira o valor da proposta");
                    return;
                } else if(prop_serv_valor_string.equals("0.00")){
                    prop_serv_valor.setError("Insira um valor válido");
                    return;
                }

                Proposta nova_proposta = new Proposta();
                nova_proposta.setDescricao(prop_serv_descricao_string);
                nova_proposta.setValor(prop_serv_valor_string);
                nova_proposta.setStatus("Pendente");
                nova_proposta.setId(UUID.randomUUID().toString());
                nova_proposta.setServico_id(servico.getId());
                nova_proposta.setEmail(user.getEmail());
                nova_proposta.setEmail_servico(user_task.getEmail());
                nova_proposta.setServico_problema(servico.getProblema());

                try {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("propostas");

                    myRef.child(nova_proposta.getId()).setValue(nova_proposta);

                } catch (Exception ex) {
                    Toast.makeText(Nova_Proposta_Activity.this, "Ocorreu um erro: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

                Cliente_Activity.ca.finish();
                Editar_Servico_Activity.es.finish();
                Intent cliente_activity = new Intent(getApplicationContext(), Cliente_Activity.class);
                cliente_activity.putExtra("user", user);
                startActivity(cliente_activity);
                finish();

            }
        });
    }
}