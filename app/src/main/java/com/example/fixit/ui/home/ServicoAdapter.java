package com.example.fixit.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixit.R;

import java.util.ArrayList;

import models.Servico;

public class ServicoAdapter extends RecyclerView.Adapter<ServicoAdapter.ServicoViewHolder> {

    private final ArrayList<Servico> servicos;

    public ServicoAdapter(ArrayList<Servico> servicos) {
        this.servicos = servicos;
    }

    @NonNull
    @Override
    public ServicoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.servico_item, parent, false);
        return new ServicoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicoViewHolder holder, int position) {
        Servico servico = servicos.get(position);
        holder.bind(servico);
    }

    @Override
    public int getItemCount() {
        return servicos.size();
    }

    class ServicoViewHolder extends RecyclerView.ViewHolder {

        TextView txtproblema;

        public ServicoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtproblema = itemView.findViewById(R.id.txt_problem);
        }

        public void bind(Servico servico) {
            txtproblema.setText(servico.getProblema());
        }
    }
}
