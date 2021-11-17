package models;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixit.R;
import com.example.fixit.ui.home.ServicoAdapter;

import java.util.ArrayList;

public class PropostaAdapter  extends RecyclerView.Adapter<PropostaAdapter.PropostaViewHolder> {

    private final ArrayList<Proposta> propostas;
    private PropostaAdapter.ReciclerViewClickListener listener;

    public PropostaAdapter(ArrayList<Proposta> propostas, PropostaAdapter.ReciclerViewClickListener listener) {
        this.propostas = propostas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PropostaAdapter.PropostaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proposta_item, parent, false);
        return new PropostaAdapter.PropostaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropostaAdapter.PropostaViewHolder holder, int position) {
        Proposta proposta = propostas.get(position);
        holder.bind(proposta);
    }

    @Override
    public int getItemCount() {
        return propostas.size();
    }

    class PropostaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtpropserv;
        TextView txtpropdescricao;
        ImageView prop_icon;

        public PropostaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtpropserv = itemView.findViewById(R.id.txt_prop_serv);
            txtpropdescricao = itemView.findViewById(R.id.txt_prop_description);
            prop_icon = itemView.findViewById(R.id.prop_icon);
            itemView.setOnClickListener(this);
        }

        public void bind(Proposta proposta) {
            if (proposta.getStatus().equals("Pendente")){
                prop_icon.setImageResource(R.drawable.ic_pendente);
                prop_icon.setBackgroundTintList(ContextCompat.getColorStateList(itemView.getContext(), R.color.alert));
            } else if (proposta.getStatus().equals("Aprovado")){
                prop_icon.setImageResource(R.drawable.ic_aprovado);
                prop_icon.setBackgroundTintList(ContextCompat.getColorStateList(itemView.getContext(), R.color.approved));
            } else {
                prop_icon.setImageResource(R.drawable.ic_rejeitado);
                prop_icon.setBackgroundTintList(ContextCompat.getColorStateList(itemView.getContext(), R.color.reject));
            }
            txtpropserv.setText("Serviço: " + proposta.getServico_problema());
            txtpropdescricao.setText(proposta.getDescricao());
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
