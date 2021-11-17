package com.example.fixit.ui.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixit.Cliente_Activity;
import com.example.fixit.Editar_Proposta_Activity;
import com.example.fixit.Editar_Servico_Activity;
import com.example.fixit.R;
import com.example.fixit.UserHelperClass;
import com.example.fixit.databinding.FragmentGalleryBinding;
import com.example.fixit.ui.home.ServicoAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Proposta;
import models.PropostaAdapter;
import models.Servico;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    private PropostaAdapter propostaAdapter;
    private List<Proposta> list = new ArrayList<>();
    private PropostaAdapter.ReciclerViewClickListener listener;
    private UserHelperClass user;
    private UserHelperClass user_task;
    private Servico servico;
    private String email;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        TextView textView;

        user = ((Cliente_Activity) getActivity()).getUser();

        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        criarLista();

        return root;
    }

    private void criarLista() {
        TextView textView;
        textView = binding.textGallery;

        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("propostas");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Proposta proposta = new Proposta();
                    list = proposta.retrievePropostaData((Map<String, Object>) dataSnapshot.getValue(), user.getEmail());
                    if (user.getTipo_conta().equals("Cliente")) {
                        if (list.isEmpty())
                            textView.setText("Não há nenhuma proposta cadastrada no momento. \nRetorne mais tarde!");
                    } else {
                        if (list.isEmpty())
                            textView.setText("Não há nenhuma proposta cadastrada no momento. \nSelecione um serviço na aba 'Serviços' e crie uma proposta!");
                    }
                    setOnClickListener();
                    propostaAdapter = new PropostaAdapter(new ArrayList<>(list), listener);
                    RecyclerView rv = binding.reciclerViewPropostas;
                    rv.setAdapter(propostaAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception ex) {
            ((Cliente_Activity) getActivity()).showToast("Ocorreu um erro: " + ex.getMessage());
        }
    }

    private void setOnClickListener() {
        listener = new PropostaAdapter.ReciclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {

                if (user.getTipo_conta().equals("Cliente")) {
                    email = list.get(position).getEmail();
                } else {
                    email = list.get(position).getEmail_servico();
                }


                try {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("usuarios");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            user_task = new UserHelperClass();
                            user_task.retrieveUserData((Map<String, Object>) dataSnapshot.getValue(), email);
                            getServicoData(list.get(position), position);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } catch (Exception ex) {
                    ((Cliente_Activity) getActivity()).showToast("Ocorreu um erro: " + ex.getMessage());
                }
            }
        };
    }

    private Servico getServicoData(Proposta proposta, int position) {

        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("servicos").child(proposta.getServico_id());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    servico = dataSnapshot.getValue(Servico.class);
                    Intent editar_proposta = new Intent(((Cliente_Activity) getActivity()).getApplicationContext(), Editar_Proposta_Activity.class);
                    editar_proposta.putExtra("proposta", list.get(position));
                    editar_proposta.putExtra("user", user);
                    editar_proposta.putExtra("user_task", user_task);
                    editar_proposta.putExtra("servico", servico);
                    startActivity(editar_proposta);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception ex) {
            ((Cliente_Activity) getActivity()).showToast("Ocorreu um erro: " + ex.getMessage());
        }

        return servico;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        criarLista();

    }
}