package com.example.fixit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Recuperar_Senha_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        mAuth = FirebaseAuth.getInstance();

        EditText email = (EditText) findViewById(R.id.recover_password_email_edittxt);

        MaterialButton enviarbtn = (MaterialButton) findViewById(R.id.recover_password_sendbtn);

        enviarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(Recuperar_Senha_Activity.this);
                progressDialog.setMessage("Validando as Informações");
                progressDialog.show();

                String email_string = email.getText().toString();

                if (TextUtils.isEmpty(email_string)) {
                    email.setError("Preencha com seu email");
                    progressDialog.dismiss();
                    return;
                } else if (!isValidEmailAddress(email_string)) {
                    email.setError("Insira um email válido");
                    progressDialog.dismiss();
                    return;
                }

                enviarEmail(email_string);
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
    }

    private void enviarEmail(String email_string) {

        mAuth.sendPasswordResetEmail(email_string).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Recuperar_Senha_Activity.this, "Email enviado!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e.getMessage().contains("There is no user")){
                    Toast.makeText(Recuperar_Senha_Activity.this, "Email não cadastrado!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Recuperar_Senha_Activity.this, "Ocorreu um Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        progressDialog.dismiss();
    }

    public static boolean isValidEmailAddress(String email) {
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(email);
        return matcher.find();
    }
}