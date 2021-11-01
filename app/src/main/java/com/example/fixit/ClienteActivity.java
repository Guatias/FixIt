package com.example.fixit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.fixit.ui.home.ServicoAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixit.databinding.ActivityClienteBinding;

import java.util.ArrayList;

import models.Servicos;

public class ClienteActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityClienteBinding binding;
    private ServicoAdapter servicoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserHelperClass user = getIntent().getParcelableExtra("user");

        MenuItem sairopt = (MenuItem) findViewById(R.id.action_settings);

        binding = ActivityClienteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarCliente.toolbar);
        /*binding.appBarCliente.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_cliente);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View navView =  navigationView.inflateHeaderView(R.layout.nav_header_cliente);
        TextView nome_menu = (TextView)navView.findViewById(R.id.nameMenu);
        TextView email_menu = (TextView)navView.findViewById(R.id.emailMenu);
        nome_menu.setText(user.getNome());
        email_menu.setText(user.getEmail());

        servicoAdapter = new ServicoAdapter(new ArrayList<>(Servicos.fakeServicos()));
        RecyclerView rv = (RecyclerView) findViewById(R.id.recicler_view_tasks);
        rv.setAdapter(servicoAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cliente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                returnMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void returnMenu() {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_cliente);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}