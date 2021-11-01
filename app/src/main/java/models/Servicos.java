package models;

import java.util.Arrays;
import java.util.List;

public class Servicos {

    public static List<Servico> fakeServicos() {
        return Arrays.asList(
                Servico.ServicoBuilder.builder()
                        .setProblema("Chuveiro")
                        .setDescricao("A resistência do meu chuveiro queimou e preciso de ajuda para trocar, o tipo do meu chuveiro é ...")
                        .setTipo("Elétrico")
                        .build(),

                Servico.ServicoBuilder.builder()
                        .setProblema("Troneira")
                        .setDescricao("O encanamento da minha torneira esta entupido e preciso de ajuda para desentupir, o tipo da minha torneira é ...")
                        .setTipo("Mecânico")
                        .build(),

                Servico.ServicoBuilder.builder()
                        .setProblema("Vaso Sanitário")
                        .setDescricao("O encanamento do meu vaso sanitário esta entupido e preciso de ajuda para desentupir, o tipo da minha vaso é ...")
                        .setTipo("Elétrico")
                        .build(),

                Servico.ServicoBuilder.builder()
                        .setProblema("Lâmpada")
                        .setDescricao("Uma lâmpada de minha casa queimou e preciso de ajuda para trocar, eu ja tenho uma lâmpada reserva ....")
                        .setTipo("Elétrico")
                        .build(),

                Servico.ServicoBuilder.builder()
                        .setProblema("Chuveiro")
                        .setDescricao("A resistência do meu chuveiro queimou e preciso de ajuda para trocar, o tipo do meu chuveiro é ....")
                        .setTipo("Elétrico")
                        .build(),

                Servico.ServicoBuilder.builder()
                        .setProblema("Troneira")
                        .setDescricao("O encanamento da minha torneira esta entupido e preciso de ajuda para desentupir, o tipo da minha torneira é ...")
                        .setTipo("Mecânico")
                        .build()
        );
    }

}
