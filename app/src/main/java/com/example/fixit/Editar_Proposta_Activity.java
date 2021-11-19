package com.example.fixit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import models.Proposta;
import models.Servico;

public class Editar_Proposta_Activity extends AppCompatActivity {

    private Proposta proposta;
    private Servico servico;
    private UserHelperClass user;
    private UserHelperClass user_proposta;
    private UserHelperClass user_task;
    private DatabaseReference reference_delete;
    public static Activity ep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_proposta);

        ep = this;

        ImageView editar_prop_icon = (ImageView) findViewById(R.id.edit_prop_icon);
        TextView editar_prop_tv = (TextView) findViewById(R.id.edit_prop_tv);
        EditText editar_prop_criado_por = (EditText) findViewById(R.id.edit_prop_created_by);
        EditText editar_prop_problema = (EditText) findViewById(R.id.edit_prop_problem);
        EditText editar_prop_descricao = (EditText) findViewById(R.id.edit_prop_description);
        EditText editar_prop_valor = (EditText) findViewById(R.id.edit_prop_serv_value);

        MaterialButton approvebtn = (MaterialButton) findViewById(R.id.edit_prop_approve_button);
        MaterialButton rejectbtn = (MaterialButton) findViewById(R.id.edit_prop_reject_button);
        MaterialButton savebtn = (MaterialButton) findViewById(R.id.edit_prop_save_button);
        MaterialButton deletebtn = (MaterialButton) findViewById(R.id.edit_prop_delete_button);
        MaterialButton contactbtn = (MaterialButton) findViewById(R.id.prop_contact_button);

        Bundle extras = getIntent().getExtras();

        proposta = getIntent().getParcelableExtra("proposta");
        user = getIntent().getParcelableExtra("user");
        user_task = getIntent().getParcelableExtra("user_task");
        servico = getIntent().getParcelableExtra("servico");

        if (user.getTipo_conta().equals("Profissional")) {
            editar_prop_criado_por.setText("Proposta para: " + user_task.getNome());
            approvebtn.setVisibility(View.INVISIBLE);
            rejectbtn.setVisibility(View.INVISIBLE);
            editar_prop_valor.setText(proposta.getValor());

            if (!proposta.getStatus().equals("Pendente")) {
                savebtn.setVisibility(View.INVISIBLE);
                editar_prop_descricao.setEnabled(false);
                editar_prop_valor.setEnabled(false);
                View deletebtn_view = findViewById(R.id.edit_prop_delete_button);
                RelativeLayout.LayoutParams layoutParams =
                        (RelativeLayout.LayoutParams) deletebtn_view.getLayoutParams();
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                deletebtn_view.setLayoutParams(layoutParams);
            }

        } else {
            editar_prop_criado_por.setText("Criada por: " + user_task.getNome());
            editar_prop_descricao.setEnabled(false);
            editar_prop_valor.setEnabled(false);
            deletebtn.setVisibility(View.INVISIBLE);
            savebtn.setVisibility(View.INVISIBLE);
            editar_prop_valor.setText("Valor da Proposta: R$" + proposta.getValor());

            if (!proposta.getStatus().equals("Pendente")) {
                approvebtn.setVisibility(View.INVISIBLE);
                rejectbtn.setVisibility(View.INVISIBLE);
            }

            if (servico.getProposta_aprovada().equals("sim")) {
                approvebtn.setVisibility(View.INVISIBLE);
                View rejectbtn_view = findViewById(R.id.edit_prop_reject_button);
                RelativeLayout.LayoutParams layoutParams =
                        (RelativeLayout.LayoutParams) rejectbtn_view.getLayoutParams();
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                rejectbtn_view.setLayoutParams(layoutParams);
            }

        }

        if (proposta.getStatus().equals("Pendente")) {
            editar_prop_icon.setImageResource(R.drawable.ic_pendente);
            editar_prop_icon.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.alert));
            editar_prop_tv.setText("Proposta Pendente");
        } else if (proposta.getStatus().equals("Aprovado")) {
            editar_prop_icon.setImageResource(R.drawable.ic_aprovado);
            editar_prop_icon.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.approved));
            editar_prop_tv.setText("Proposta Aprovada");
        } else {
            editar_prop_icon.setImageResource(R.drawable.ic_rejeitado);
            editar_prop_icon.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.reject));
            editar_prop_tv.setText("Proposta Rejeitada");
        }

        editar_prop_criado_por.setEnabled(false);
        editar_prop_problema.setEnabled(false);
        editar_prop_problema.setText("Serviço: " + proposta.getServico_problema());
        editar_prop_descricao.setText(proposta.getDescricao());


        contactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contato_activity = new Intent(getApplicationContext(), Contato_Activity.class);
                contato_activity.putExtra("user_task", user_task);
                startActivity(contato_activity);
                //Toast.makeText(Editar_Proposta_Activity.this, "Contato", Toast.LENGTH_SHORT).show();
            }
        });

        approvebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                proposta.setStatus("Aprovado");

                try {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("servicos").child(proposta.getServico_id());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Servico servico = new Servico();

                            servico = dataSnapshot.getValue(Servico.class);

                            servico.setProposta_aprovada("sim");

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("servicos");

                            myRef.child(servico.getId()).setValue(servico);

                            database = FirebaseDatabase.getInstance();
                            myRef = database.getReference("propostas");

                            myRef.child(proposta.getId()).setValue(proposta);

                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } catch (Exception ex) {
                    Toast.makeText(Editar_Proposta_Activity.this, "Ocorreu um erro: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String editar_prop_descricao_string = editar_prop_descricao.getText().toString();
                String editar_prop_valor_string = editar_prop_valor.getText().toString();

                proposta.setDescricao(editar_prop_descricao_string);
                proposta.setValor(editar_prop_valor_string);

                try {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("propostas");

                    myRef.child(proposta.getId()).setValue(proposta);

                    finish();

                } catch (Exception ex) {
                    Toast.makeText(Editar_Proposta_Activity.this, "Ocorreu um erro: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        rejectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                proposta.setStatus("Rejeitado");

                try {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("propostas");

                    myRef.child(proposta.getId()).setValue(proposta);

                    finish();

                } catch (Exception ex) {
                    Toast.makeText(Editar_Proposta_Activity.this, "Ocorreu um erro: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (proposta.getStatus().equals("Aprovado")) {

                        servico.setProposta_aprovada("não");

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("servicos");

                        myRef.child(servico.getId()).setValue(servico);

                    }

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("propostas");
                    reference.child(proposta.getId()).removeValue();

                    finish();
                } catch (Exception ex) {
                    Toast.makeText(Editar_Proposta_Activity.this, "Ocorreu um erro: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
