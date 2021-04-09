package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import org.junit.Test;

import java.util.Date;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class AssertThat {

    @Test
    public void test() {
        //  cenario
        LocacaoService service = new LocacaoService();

        Usuario usuario = new Usuario("Daniel Baumann");
        Filme filme = new Filme("It a coisa", 6, 7.5);

        //  ação
        Locacao locacao = null;
        try {
            locacao = service.alugarFilme(usuario, filme);

            //  verificação
            assertThat(
                    locacao.getValor(),
                    is(equalTo(7.5)));

            assertThat(
                    locacao.getValor(),
                    is(not(8.0)));

            assertThat(
                    isMesmaData(
                            locacao.getDataLocacao(),
                            new Date()),
                    is(true));

            assertThat(
                    isMesmaData(
                            locacao.getDataRetorno(),
                            obterDataComDiferencaDias(1)),
                    is(true));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

















