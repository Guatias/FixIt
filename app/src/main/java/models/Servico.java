package models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.fixit.UserHelperClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Servico implements Parcelable {

    private String problema;
    private String descricao;
    private String tipo;
    private String email;
    private String id;
    private String proposta_aprovada;

    public Servico() {
    }

    public Servico(String problema, String descricao, String tipo, String email, String id, String proposta_aprovada) {
        this.problema = problema;
        this.descricao = descricao;
        this.tipo = tipo;
        this.email = email;
        this.id = id;
        this.proposta_aprovada = proposta_aprovada;
    }

    protected Servico(Parcel in) {
        problema = in.readString();
        descricao = in.readString();
        tipo = in.readString();
        email = in.readString();
        id = in.readString();
        proposta_aprovada = in.readString();
    }

    public static final Creator<Servico> CREATOR = new Creator<Servico>() {
        @Override
        public Servico createFromParcel(Parcel in) {
            return new Servico(in);
        }

        @Override
        public Servico[] newArray(int size) {
            return new Servico[size];
        }
    };

    public String getProblema() {
        return problema;
    }

    public void setProblema(String problema) {
        this.problema = problema;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProposta_aprovada() {
        return proposta_aprovada;
    }

    public void setProposta_aprovada(String proposta_aprovada) {
        this.proposta_aprovada = proposta_aprovada;
    }

    public List<Servico> retrieveServicoData(Map<String, Object> users, String email) {

        Servico servicoData = new Servico();
        ArrayList<Servico> array = null;
        List<Servico> list = new ArrayList<>();

        try {
            for (Map.Entry<String, Object> entry : users.entrySet()) {

                Map singleUser = (Map) entry.getValue();
                if (singleUser.get("email").toString().equals(email)){
                    servicoData = new Servico();
                    servicoData.problema = singleUser.get("problema").toString();
                    servicoData.descricao = singleUser.get("descricao").toString();
                    servicoData.tipo = singleUser.get("tipo").toString();
                    servicoData.id = singleUser.get("id").toString();
                    servicoData.email = singleUser.get("email").toString();
                    servicoData.proposta_aprovada = singleUser.get("proposta_aprovada").toString();
                list.add(servicoData);
                }
            }
        } catch (Exception ex){

        }

        return list;

    }

    public List<Servico> retrieveAllServicoData(Map<String, Object> users) {

        Servico servicoData = new Servico();
        ArrayList<Servico> array = null;
        List<Servico> list = new ArrayList<>();

        try {
            for (Map.Entry<String, Object> entry : users.entrySet()) {

                Map singleUser = (Map) entry.getValue();
                    servicoData = new Servico();
                    servicoData.problema = singleUser.get("problema").toString();
                    servicoData.descricao = singleUser.get("descricao").toString();
                    servicoData.tipo = singleUser.get("tipo").toString();
                    servicoData.id = singleUser.get("id").toString();
                    servicoData.email = singleUser.get("email").toString();
                    servicoData.proposta_aprovada = singleUser.get("proposta_aprovada").toString();
                    list.add(servicoData);
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
        dest.writeString(problema);
        dest.writeString(descricao);
        dest.writeString(tipo);
        dest.writeString(email);
        dest.writeString(id);
        dest.writeString(proposta_aprovada);
    }
}
