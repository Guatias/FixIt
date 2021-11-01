package models;

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
