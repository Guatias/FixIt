package com.example.fixit.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixit.R;

import java.util.ArrayList;

import models.Servico;

public class ServicoAdapter extends RecyclerView.Adapter<ServicoAdapter.ServicoViewHolder> {

    private final ArrayList<Servico> servicos;
    private ReciclerViewClickListener listener;

    public ServicoAdapter(ArrayList<Servico> servicos, ReciclerViewClickListener listener) {
        this.servicos = servicos;
        this.listener = listener;
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

    class ServicoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtproblema;
        TextView txtdescricao;
        ImageView servico_icon;

        public ServicoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtproblema = itemView.findViewById(R.id.txt_problem);
            txtdescricao = itemView.findViewById(R.id.txt_description);
            servico_icon = itemView.findViewById(R.id.serv_icon);
            itemView.setOnClickListener(this);
        }

        public void bind(Servico servico) {
            if (servico.getTipo().equals("Eletrico")){
                servico_icon.setImageResource(R.drawable.idea);
            } else {
                servico_icon.setImageResource(R.drawable.wrench);
            }
            txtproblema.setText(servico.getProblema());
            txtdescricao.setText(servico.getDescricao());
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    public interface ReciclerViewClickListener{
        void onClick(View v, int position);
    }
}
