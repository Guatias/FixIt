package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Servico {

    private String problema;
    private String descricao;
    private String tipo;
    private String email;

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
                    list.add(servicoData);
                }
            }
        } catch (Exception ex){

        }

        return list;

    }
}
