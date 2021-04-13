package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Locacao;
import buildermaster.BuilderMaster;

public class Principal {

    public static void main(String[] args) {
        new BuilderMaster().gerarCodigoClasse(Locacao.class);
    }
}
