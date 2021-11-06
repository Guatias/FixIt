package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Servico {

    private String problema;
    private String descricao;
    private String tipo;

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

    public List<Servico> retrieveServicoData(Map<String, Object> users, String email) {

        Servico servicoData = new Servico();
        ArrayList<Servico> array = null;
        List<Servico> list = new ArrayList<>();

        try {
            for (Map.Entry<String, Object> entry : users.entrySet()) {

                Map singleUser = (Map) entry.getValue();
                if (singleUser.get("email").toString().equals(email)){
                    servicoData = new Servico();
                    servicoData.problema = singleUser.get("Problema").toString();
                    servicoData.descricao = singleUser.get("Descricao").toString();
                    servicoData.tipo = singleUser.get("Tipo").toString();
                    list.add(servicoData);
                }
            }
        } catch (Exception ex){

        }

        return list;

    }

    public static class ServicoBuilder {

        private String problema;
        private String descricao;
        private String tipo;

        public ServicoBuilder setProblema(String problema) {
            this.problema = problema;
            return this;
        }

        public ServicoBuilder setDescricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public ServicoBuilder setTipo(String tipo) {
            this.tipo = tipo;
            return this;
        }

        private ServicoBuilder(){}

        public static ServicoBuilder builder(){
            return new ServicoBuilder();
        }

        public Servico build() {
            Servico servico = new Servico();
            servico.problema = problema;
            servico.descricao = descricao;
            servico.tipo = tipo;
            return servico;
        }

    }

}
