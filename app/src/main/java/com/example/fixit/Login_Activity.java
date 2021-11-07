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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Login_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);

        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);
        MaterialButton registerbtn = (MaterialButton) findViewById(R.id.registerbtn);

        //admin and admin

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email_string = username.getText().toString();
                String senha_string = password.getText().toString();

                if (TextUtils.isEmpty(email_string)) {
                    username.setError("Insira seu email");
                    return;
                }

                if (TextUtils.isEmpty(senha_string)) {
                    password.setError("Insira sua senha");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email_string,senha_string).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Toast.makeText(MainActivity.this, "Usuario Autenticado com Sucesso", Toast.LENGTH_SHORT).show();
                            try {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("usuarios");
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        UserHelperClass user = new UserHelperClass();
                                        user.retrieveUserData((Map<String, Object>) dataSnapshot.getValue(), email_string);
                                        if(user.getTipo_conta().equals("Cliente")) {
                                            finish();
                                            Intent cliente_activity = new Intent(getApplicationContext(), Cliente_Activity.class);
                                            cliente_activity.putExtra("user", user);
                                            startActivity(cliente_activity);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } catch (Exception ex){
                                Toast.makeText(Login_Activity.this, "Ocorreu um erro: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Login_Activity.this, "Usuario ou Senha Inválido", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent register_activity = new Intent(getApplicationContext(), Register_Activity_1.class);
                startActivity(register_activity);

            }
        });
    }
}