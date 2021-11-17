package models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Proposta implements Parcelable {

    String descricao;
    String valor;
    String status;
    String id;
    String servico_id;
    String email;
    String email_servico;
    String servico_problema;

    public Proposta() {
    }

    protected Proposta(Parcel in) {
        descricao = in.readString();
        valor = in.readString();
        status = in.readString();
        id = in.readString();
        servico_id = in.readString();
        email = in.readString();
        email_servico = in.readString();
        servico_problema = in.readString();
    }

    public static final Creator<Proposta> CREATOR = new Creator<Proposta>() {
        @Override
        public Proposta createFromParcel(Parcel in) {
            return new Proposta(in);
        }

        @Override
        public Proposta[] newArray(int size) {
            return new Proposta[size];
        }
    };

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServico_id() {
        return servico_id;
    }

    public void setServico_id(String servico_id) {
        this.servico_id = servico_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServico_problema() {
        return servico_problema;
    }

    public void setServico_problema(String servico_problema) {
        this.servico_problema = servico_problema;
    }

    public String getEmail_servico() {
        return email_servico;
    }

    public void setEmail_servico(String email_servico) {
        this.email_servico = email_servico;
    }

    public List<Proposta> retrievePropostaData(Map<String, Object> propostas, String email) {

        Proposta propostaData = new Proposta();
        ArrayList<Proposta> array = null;
        List<Proposta> list = new ArrayList<>();

        try {
            for (Map.Entry<String, Object> entry : propostas.entrySet()) {

                Map singleProposta = (Map) entry.getValue();
                if (singleProposta.get("email").toString().equals(email) || singleProposta.get("email_servico").toString().equals(email)){
                    propostaData = new Proposta();
                    propostaData.descricao = singleProposta.get("descricao").toString();
                    propostaData.valor = singleProposta.get("valor").toString();
                    propostaData.status = singleProposta.get("status").toString();
                    propostaData.id = singleProposta.get("id").toString();
                    propostaData.servico_id = singleProposta.get("servico_id").toString();
                    propostaData.email = singleProposta.get("email").toString();
                    propostaData.email_servico = singleProposta.get("email_servico").toString();
                    propostaData.servico_problema = singleProposta.get("servico_problema").toString();
                    list.add(propostaData);
                }
            }
        } catch (Exception ex){

        }

        return list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(descricao);
        dest.writeString(valor);
        dest.writeString(status);
        dest.writeString(id);
        dest.writeString(servico_id);
        dest.writeString(email);
        dest.writeString(email_servico);
        dest.writeString(servico_problema);
    }
}
