package com.example.fixit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register_activity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        TextView nome = (TextView) findViewById(R.id.firstnameedittxt);
        TextView sobrenome = (TextView) findViewById(R.id.lastnameedittxt);
        TextView email = (TextView) findViewById(R.id.emailedittxt);
        TextView senha = (TextView) findViewById(R.id.passwordedittxt);
        TextView confirmar_senha = (TextView) findViewById(R.id.confirmpasswordedittxt);

        MaterialButton confirm_register = (MaterialButton) findViewById(R.id.confirmregisterbtn);

        confirm_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nome_string = nome.getText().toString();
                String sobrenome_string = sobrenome.getText().toString();
                String email_string = email.getText().toString();
                String senha_string = senha.getText().toString();
                String confirmar_senha_string = confirmar_senha.getText().toString();

                if (TextUtils.isEmpty(nome_string)) {
                    nome.setError("Preencha com seu nome");
                    return;
                }

                if (TextUtils.isEmpty(sobrenome_string)) {
                    sobrenome.setError("Preencha com seu sobrenome");
                    return;
                }

                if (TextUtils.isEmpty(email_string)) {
                    email.setError("Preencha com seu email");
                    return;
                }

                if (TextUtils.isEmpty(senha_string)) {
                    senha.setError("Crie uma senha");
                    return;
                }

                if(senha_string.length() < 6){
                    senha.setError("Senha deve conter no minimo 6 caracteres");
                    return;
                }

                if (TextUtils.isEmpty(confirmar_senha_string)) {
                    confirmar_senha.setError("Confirme sua senha");
                    return;
                }

                if (!confirmar_senha_string.equals(senha_string)) {
                    confirmar_senha.setError("Senhas não coincidem");
                    return;
                }

                try {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("usuarios");

                    UserHelperClass helperClass = new UserHelperClass(nome_string,sobrenome_string);

                    myRef.setValue(helperClass);

                } catch (Exception ex) {
                    Toast.makeText(register_activity.this, "Ocorreu um erro: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

                /*mAuth.createUserWithEmailAndPassword(email_string, senha_string).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(register_activity.this, "Usuário Criado com Sucesso", Toast.LENGTH_SHORT).show();
                            Intent main_activity = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(main_activity);
                        } else {
                            if(task.getException().getMessage().contains("email address is already")){
                                Toast.makeText(register_activity.this, "Usuario não criado: Email já Cadastrado", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(register_activity.this, "Ocorreu um erro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });*/
            }
        });
    }

}