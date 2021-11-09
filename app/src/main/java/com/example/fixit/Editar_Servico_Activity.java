package com.example.fixit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fixit.ui.home.HomeFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Servico;

public class Editar_Servico_Activity extends AppCompatActivity {

    private UserHelperClass user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_servico);

        ImageView editar_serv_icon = (ImageView) findViewById(R.id.edit_serv_icon);
        TextView editar_serv_tv = (TextView) findViewById(R.id.edit_serv_tv);
        EditText editar_serv_problema = (EditText) findViewById(R.id.edit_serv_problem);
        EditText editar_serv_descricao = (EditText) findViewById(R.id.edit_serv_description);

        MaterialButton savebtn = (MaterialButton) findViewById(R.id.edit_serv_save_button);
        MaterialButton deletebtn = (MaterialButton) findViewById(R.id.edit_serv_delete_button);

        Bundle extras = getIntent().getExtras();

        Servico servico = getIntent().getParcelableExtra("servico");
        user = getIntent().getParcelableExtra("user");

        if (servico.getTipo().equals("Eletrico")){
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
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("tasks");
                reference.child(servico.getId()).removeValue();
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
                    DatabaseReference myRef = database.getReference("tasks");

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

    }
}