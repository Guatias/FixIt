package com.example.fixit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_Activity_2 extends AppCompatActivity {

    private String[] tipo = new String[]{"Cliente", "Profissional", "Tipo da Conta"};
    private FirebaseAuth mAuth;
    private String email;
    private String senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        mAuth = FirebaseAuth.getInstance();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, tipo) {
            @Override
            public int getCount() {
                // to show hint "Select Gender" and don't able to select
                return tipo.length - 1;
            }
        };

        TextView cpf = (TextView) findViewById(R.id.cpfedittxt);
        TextView celular = (TextView) findViewById(R.id.phoneedittxt);
        TextView datanasc = (TextView) findViewById(R.id.birthdateedittxt);
        Spinner tipo_conta = (Spinner) findViewById(R.id.accounttypespinner);
        tipo_conta.setAdapter(adapter);
        tipo_conta.setSelection(tipo.length - 1);

        MaterialButton conf_cadastro = (MaterialButton) findViewById(R.id.confirmbtn);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mtw = new MaskTextWatcher(cpf, smf);
        cpf.addTextChangedListener(mtw);

        SimpleMaskFormatter smf2 = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher mtw2 = new MaskTextWatcher(celular, smf2);
        celular.addTextChangedListener(mtw2);

        SimpleMaskFormatter smf3 = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtw3 = new MaskTextWatcher(datanasc, smf3);
        datanasc.addTextChangedListener(mtw3);

        conf_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cpf_string = cpf.getText().toString();
                String celular_string = celular.getText().toString();
                String datanasc_string = datanasc.getText().toString();

                cpf_string = cpf_string.replace(".", "");
                cpf_string = cpf_string.replace("-", "");

                if (TextUtils.isEmpty(cpf_string)) {
                    cpf.setError("Preencha com seu CPF");
                    return;
                } else if (cpf_string.length() != 11) {
                    cpf.setError("CPF Inválido");
                }

                if (TextUtils.isEmpty(celular_string)) {
                    celular.setError("Preencha com o numero de seu celular");
                    return;
                } else if (celular_string.length() != 14) {
                    celular.setError("Número Inválido");
                }

                if (TextUtils.isEmpty(datanasc_string)) {
                    datanasc.setError("Preencha com sua data de nascimento");
                    return;
                } else if (datanasc_string.length() != 10) {
                    datanasc.setError("Data Inválida");
                }

                if (tipo_conta.getSelectedItem().toString() == "Tipo da Conta") {
                    Toast.makeText(Register_Activity_2.this, "Selecione o tipo da conta", Toast.LENGTH_SHORT).show();
                    return;
                }

                String tipo_conta_string = tipo_conta.getSelectedItem().toString();

                UserHelperClass user = getIntent().getParcelableExtra("user");

                user.setCelular(celular_string);
                user.setData_nasc(datanasc_string);
                user.setTipo_conta(tipo_conta_string);

                try {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("usuarios");

                    myRef.child(cpf_string).setValue(user);

                } catch (Exception ex) {
                    Toast.makeText(Register_Activity_2.this, "Ocorreu um erro: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

                email = user.getEmail();
                senha = getIntent().getExtras().getString("senha");

                mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(Register_Activity_2.this, "Usuário Criado com Sucesso", Toast.LENGTH_SHORT).show();
                            Register_Activity_1.ra.finish();
                            Login_Activity.la.finish();
                            finish();
                            Intent main_activity = new Intent(getApplicationContext(), Login_Activity.class);
                            startActivity(main_activity);
                        } else {
                            Toast.makeText(Register_Activity_2.this, "Ocorreu um erro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}