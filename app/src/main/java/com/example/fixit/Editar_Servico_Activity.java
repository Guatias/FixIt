package com.example.fixit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fixit.ui.home.HomeFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import models.Proposta;
import models.Servico;

public class Editar_Servico_Activity extends AppCompatActivity {

    private UserHelperClass user;
    private UserHelperClass user_task;
    private DatabaseReference reference_delete;
    public static Activity es;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_servico);
        es = this;

        ImageView editar_serv_icon = (ImageView) findViewById(R.id.edit_serv_icon);
        TextView editar_serv_tv = (TextView) findViewById(R.id.edit_serv_tv);
        EditText editar_criado_por = (EditText) findViewById(R.id.edit_created_by);
        EditText editar_serv_problema = (EditText) findViewById(R.id.edit_serv_problem);
        EditText editar_serv_descricao = (EditText) findViewById(R.id.edit_serv_description);

        MaterialButton savebtn = (MaterialButton) findViewById(R.id.edit_serv_save_button);
        MaterialButton deletebtn = (MaterialButton) findViewById(R.id.edit_serv_delete_button);
        MaterialButton createbtn = (MaterialButton) findViewById(R.id.edit_serv_create_button);

        Bundle extras = getIntent().getExtras();

        Servico servico = getIntent().getParcelableExtra("servico");
        user = getIntent().getParcelableExtra("user");
        user_task = getIntent().getParcelableExtra("user_task");

        if (user.getTipo_conta().equals("Profissional")) {
            editar_criado_por.setText("Criado por: " + user_task.getNome() + " " + user_task.getSobrenome());
            editar_criado_por.setEnabled(false);
            editar_serv_descricao.setEnabled(false);
            editar_serv_problema.setEnabled(false);
            savebtn.setVisibility(View.INVISIBLE);
            deletebtn.setVisibility(View.INVISIBLE);
        } else {
            createbtn.setVisibility(View.INVISIBLE);
            editar_criado_por.setVisibility(View.INVISIBLE);
            ((RelativeLayout.LayoutParams) editar_serv_problema.getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.edit_serv_tv);
        }

        if (servico.getTipo().equals("Eletrico")) {
            editar_serv_icon.setImageResource(R.drawable.idea);
            editar_serv_tv.setText("Serviço Elétrico");
        } else {
            editar_serv_icon.setImageResource(R.drawable.wrench);
            editar_serv_tv.setText("Serviço Mecânico");
        }

        editar_serv_problema.setText(servico.getProblema());
        editar_serv_descricao.setText(servico.getDescricao());

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("servicos");
                    reference.child(servico.getId()).removeValue();

                    try {
                        reference_delete = FirebaseDatabase.getInstance().getReference().child("propostas");
                        reference_delete.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                deletarPropostas((Map<String, Object>) dataSnapshot.getValue(), servico.getId(), reference_delete);
                                Cliente_Activity.ca.finish();
                                Intent cliente_activity = new Intent(getApplicationContext(), Cliente_Activity.class);
                                cliente_activity.putExtra("user", user);
                                startActivity(cliente_activity);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } catch (Exception ex) {
                        Toast.makeText(Editar_Servico_Activity.this, "Ocorreu um erro: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ex) {
                    Toast.makeText(Editar_Servico_Activity.this, "Ocorreu um erro: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String problema_string = editar_serv_problema.getText().toString();
                String descricao_string = editar_serv_descricao.getText().toString();

                servico.setProblema(problema_string);
                servico.setDescricao(descricao_string);
                servico.setEmail(user.getEmail());

                try {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("servicos");

                    myRef.child(servico.getId()).setValue(servico);

                    Cliente_Activity.ca.finish();
                    Intent cliente_activity = new Intent(getApplicationContext(), Cliente_Activity.class);
                    cliente_activity.putExtra("user", user);
                    startActivity(cliente_activity);
                    finish();

                } catch (Exception ex) {
                    Toast.makeText(Editar_Servico_Activity.this, "Ocorreu um erro: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent proposta_activity = new Intent(getApplicationContext(), Nova_Proposta_Activity.class);
                proposta_activity.putExtra("user", user);
                proposta_activity.putExtra("user_task", user_task);
                proposta_activity.putExtra("servico", servico);
                startActivity(proposta_activity);
            }
        });

    }
    public void deletarPropostas(Map<String, Object> propostas, String id_servico, DatabaseReference reference) {

        Proposta propostaData = new Proposta();

        try {
            for (Map.Entry<String, Object> entry : propostas.entrySet()) {

                Map singleProposta = (Map) entry.getValue();
                if (singleProposta.get("problema_id").toString().equals(id_servico)){
                    reference.child(singleProposta.get("id").toString()).removeValue();
                }
            }
        } catch (Exception ex){

        }
    }

}