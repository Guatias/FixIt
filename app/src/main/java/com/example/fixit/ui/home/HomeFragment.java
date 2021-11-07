package com.example.fixit.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixit.Cliente_Activity;
import com.example.fixit.Novo_Servico_Activity;
import com.example.fixit.UserHelperClass;
import com.example.fixit.databinding.FragmentHomeBinding;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Servico;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private ServicoAdapter servicoAdapter;
    private List<Servico> list = new ArrayList<>();
    UserHelperClass user;
    FloatingActionMenu add_button;
    FloatingActionButton eletric_button;
    FloatingActionButton mechanic_button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Intent i = getActivity().getIntent();

        //user = (UserHelperClass) i.getSerializableExtra("user");
        user = ((Cliente_Activity) getActivity()).getUser();

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("tasks");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Servico servico = new Servico();
                    list = servico.retrieveServicoData((Map<String, Object>) dataSnapshot.getValue(), user.getEmail());
                    servicoAdapter = new ServicoAdapter(new ArrayList<>(list));
                    RecyclerView rv = binding.reciclerViewTasks;
                    rv.setAdapter(servicoAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception ex) {
            ((Cliente_Activity) getActivity()).showToast("Ocorreu um erro: " + ex.getMessage());
        }

        add_button = binding.addButton;
        eletric_button = binding.eletricButton;
        mechanic_button = binding.mechanicButton;

        eletric_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent novo_servico = new Intent(((Cliente_Activity) getActivity()).getApplicationContext(), Novo_Servico_Activity.class);
                novo_servico.putExtra("user", user);
                novo_servico.putExtra("tipo", "Eletrico");
                startActivity(novo_servico);
                ((Cliente_Activity) getActivity()).finish();
            }
        });

        mechanic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent novo_servico = new Intent(((Cliente_Activity) getActivity()).getApplicationContext(), Novo_Servico_Activity.class);
                novo_servico.putExtra("user", user);
                novo_servico.putExtra("tipo", "Mecanico");
                startActivity(novo_servico);
                ((Cliente_Activity) getActivity()).finish();
            }
        });

        // final TextView textView = binding.textHome;
       /* homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}