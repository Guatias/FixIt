package com.example.fixit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

public class Contato_Activity extends AppCompatActivity {

    private UserHelperClass user_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        ShapeableImageView foto_contato = (ShapeableImageView) findViewById(R.id.contact_photo);
        TextView nome_contato = (TextView) findViewById(R.id.contact_name);
        TextView celular_contato = (TextView) findViewById(R.id.contact_phone);
        TextView email_contato = (TextView) findViewById(R.id.contact_email);

        user_task = getIntent().getParcelableExtra("user_task");
        nome_contato.setText(user_task.getNome() + " " + user_task.getSobrenome());
        celular_contato.setText(user_task.getCelular());
        email_contato.setText(user_task.getEmail());

    }
}