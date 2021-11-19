package com.example.fixit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register_Activity_1 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private boolean allow_continue = true;
    public static Activity ra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ra = this;
        mAuth = FirebaseAuth.getInstance();

        TextView nome = (TextView) findViewById(R.id.firstnameedittxt);
        TextView sobrenome = (TextView) findViewById(R.id.lastnameedittxt);
        TextView email = (TextView) findViewById(R.id.emailedittxt);
        TextView senha = (TextView) findViewById(R.id.passwordedittxt);
        TextView confirmar_senha = (TextView) findViewById(R.id.confirmpasswordedittxt);

        MaterialButton proxima_pagina = (MaterialButton) findViewById(R.id.nextpagebtn);

        proxima_pagina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog progressDialog = new ProgressDialog(Register_Activity_1.this);
                progressDialog.setMessage("Validando as Informações");
                progressDialog.show();

                String nome_string = nome.getText().toString();
                String sobrenome_string = sobrenome.getText().toString();
                String email_string = email.getText().toString();
                String senha_string = senha.getText().toString();
                String confirmar_senha_string = confirmar_senha.getText().toString();

                if (TextUtils.isEmpty(nome_string)) {
                    nome.setError("Preencha com seu nome");
                    progressDialog.dismiss();
                    return;
                }

                if (TextUtils.isEmpty(sobrenome_string)) {
                    sobrenome.setError("Preencha com seu sobrenome");
                    progressDialog.dismiss();
                    return;
                }

                if (TextUtils.isEmpty(email_string)) {
                    email.setError("Preencha com seu email");
                    progressDialog.dismiss();
                    return;
                } else if (!isValidEmailAddress(email_string)) {
                    email.setError("Insira um email válido");
                    progressDialog.dismiss();
                    return;
                }

                if (TextUtils.isEmpty(senha_string)) {
                    senha.setError("Crie uma senha");
                    progressDialog.dismiss();
                    return;
                }

                if (senha_string.length() < 6) {
                    senha.setError("Senha deve conter no minimo 6 caracteres");
                    progressDialog.dismiss();
                    return;
                }

                if (TextUtils.isEmpty(confirmar_senha_string)) {
                    confirmar_senha.setError("Confirme sua senha");
                    progressDialog.dismiss();
                    return;
                }

                if (!confirmar_senha_string.equals(senha_string)) {
                    confirmar_senha.setError("Senhas não coincidem");
                    progressDialog.dismiss();
                    return;
                }

                try {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("usuarios");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (checkEmail((Map<String, Object>) dataSnapshot.getValue(), email_string)) {
                                Toast.makeText(Register_Activity_1.this, "Email ja Cadastrado", Toast.LENGTH_SHORT).show();
                                allow_continue = false;
                            } else {
                                allow_continue = true;
                            }

                            if (allow_continue) {

                                UserHelperClass user = new UserHelperClass();
                                user.setNome(nome_string);
                                user.setSobrenome(sobrenome_string);
                                user.setEmail(email_string);

                                Intent register2 = new Intent(getApplicationContext(), Register_Activity_2.class);
                                register2.putExtra("user", user);
                                register2.putExtra("senha", senha_string);
                                startActivity(register2);
                            }
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } catch (Exception ex) {
                    Toast.makeText(Register_Activity_1.this, "Ocorreu um erro: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        nome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    nome.setHint("");
                else
                    nome.setHint("Nome");
            }
        });

        sobrenome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    sobrenome.setHint("");
                else
                    sobrenome.setHint("Sobrenome");
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    email.setHint("");
                else
                    email.setHint("Email");
            }
        });

        senha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    senha.setHint("");
                else
                    senha.setHint("Senha");
            }
        });

        confirmar_senha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    confirmar_senha.setHint("");
                else
                    confirmar_senha.setHint("Confirmar Senha");
            }
        });
    }

    private boolean checkEmail(Map<String, Object> users, String email) {
        try {
            for (Map.Entry<String, Object> entry : users.entrySet()) {

                Map singleUser = (Map) entry.getValue();
                if (singleUser.get("email").toString().equals(email)) return true;
            }
            return false;
        } catch (Exception ex) {

        }
        return false;
    }

    public static boolean isValidEmailAddress(String email) {
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(email);
        return matcher.find();
    }

}