package com.example.fixit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public class activity_register_2 extends AppCompatActivity {

    private String[] tipo = new String[]{"Cliente", "Profissional"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, tipo);


        TextView cpf = (TextView) findViewById(R.id.cpfedittxt);
        TextView celular = (TextView) findViewById(R.id.phoneedittxt);
        TextView datanasc = (TextView) findViewById(R.id.birthdateedittxt);
        Spinner tipo_conta = (Spinner) findViewById(R.id.accounttypespinner);
        tipo_conta.setAdapter(adapter);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mtw = new MaskTextWatcher(cpf, smf);
        cpf.addTextChangedListener(mtw);

        SimpleMaskFormatter smf2 = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher mtw2 = new MaskTextWatcher(celular, smf2);
        celular.addTextChangedListener(mtw2);

        SimpleMaskFormatter smf3 = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtw3 = new MaskTextWatcher(datanasc, smf3);
        datanasc.addTextChangedListener(mtw3);

    }
}