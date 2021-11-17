package com.example.fixit.ui.home;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixit.Cliente_Activity;
import com.example.fixit.Editar_Servico_Activity;
import com.example.fixit.Novo_Servico_Activity;
import com.example.fixit.R;
import com.example.fixit.UserHelperClass;
import com.example.fixit.databinding.FragmentGalleryBinding;
import com.example.fixit.databinding.FragmentHomeBinding;
import com.example.fixit.ui.gallery.GalleryFragment;
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

import models.Proposta;
import models.Servico;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private FragmentGalleryBinding binding_gallery;
    private ServicoAdapter servicoAdapter;
    private List<Servico> list = new ArrayList<>();
    private ServicoAdapter.ReciclerViewClickListener listener;
    private UserHelperClass user;
    private UserHelperClass user_task;
    private boolean tem_proposta;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FloatingActionMenu add_button;
        FloatingActionButton eletric_button;
        FloatingActionButton mechanic_button;
        TextView textView;

        user = ((Cliente_Activity) getActivity()).getUser();

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textView = binding.textHome;

        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("servicos");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Servico servico = new Servico();
                    if (user.getTipo_conta().equals("Cliente")) {
                        list = servico.retrieveServicoData((Map<String, Object>) dataSnapshot.getValue(), user.getEmail());
                        if (list.isEmpty())
                            textView.setText("Você não possui nenhum serviço cadastrado. \nClique no botão abaixo para criar um novo!");
                    } else {
                        list = servico.retrieveAllServicoData((Map<String, Object>) dataSnapshot.getValue());
                        if (list.isEmpty())
                            textView.setText("Não há nenhum serviço cadastrado no momento. \nRetorne mais tarde!");
                    }
                    setOnClickListener();
                    servicoAdapter = new ServicoAdapter(new ArrayList<>(list), listener);
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

        if (user.getTipo_conta().equals("Profissional")) {
            add_button.setVisibility(View.INVISIBLE);
            eletric_button.setVisibility(View.INVISIBLE);
            mechanic_button.setVisibility(View.INVISIBLE);
        }

        eletric_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent novo_servico = new Intent(((Cliente_Activity) getActivity()).getApplicationContext(), Novo_Servico_Activity.class);
                novo_servico.putExtra("user", user);
                novo_servico.putExtra("tipo", "Eletrico");
                startActivity(novo_servico);
                add_button.close(false);
            }
        });

        mechanic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent novo_servico = new Intent(((Cliente_Activity) getActivity()).getApplicationContext(), Novo_Servico_Activity.class);
                novo_servico.putExtra("user", user);
                novo_servico.putExtra("tipo", "Mecanico");
                startActivity(novo_servico);
                add_button.close(false);
            }
        });

        return root;
    }

    private void setOnClickListener() {
        listener = new ServicoAdapter.ReciclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {

                String email = list.get(position).getEmail();
                String id = list.get(position).getId();

                try {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("usuarios");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            user_task = new UserHelperClass();
                            user_task.retrieveUserData((Map<String, Object>) dataSnapshot.getValue(), email);
                            checarPropostas(id, position);
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

    public void checarPropostas(String id_servico, int position) {

        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("propostas");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Map<String, Object> propostas = (Map<String, Object>) dataSnapshot.getValue();

                    Proposta propostaData = new Proposta();

                    tem_proposta = false;

                    try {

                        for (Map.Entry<String, Object> entry : propostas.entrySet()) {

                            Map singleProposta = (Map) entry.getValue();
                            if (singleProposta.get("servico_id").toString().equals(id_servico) && singleProposta.get("email").equals(user.getEmail())) {
                                tem_proposta = true;
                            }
                        }

                    } catch (Exception ex) {
                    }

                    Intent editar_servico = new Intent(((Cliente_Activity) getActivity()).getApplicationContext(), Editar_Servico_Activity.class);
                    editar_servico.putExtra("servico", list.get(position));
                    editar_servico.putExtra("user", user);
                    editar_servico.putExtra("user_task", user_task);
                    editar_servico.putExtra("tem_proposta", tem_proposta);
                    startActivity(editar_servico);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception ex) {
            ((Cliente_Activity) getActivity()).showToast("Ocorreu um erro: " + ex.getMessage());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}