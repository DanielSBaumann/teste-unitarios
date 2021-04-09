package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.util.Date;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.*;

public class VariosTestes {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Test
    public void test() throws Exception {

        //  cenario
        LocacaoService service = new LocacaoService();

        Usuario usuario = new Usuario("Daniel Baumann");
        Filme filme = new Filme("It a coisa", 6, 7.5);

        //  ação
        Locacao locacao = service.alugarFilme(usuario, filme);

            //  verificação
            error
                    .checkThat(
                            locacao
                                    .getValor(),
                            is(equalTo(7.5)));

            error
                    .checkThat(locacao
                                    .getValor(),
                            is(not(8.0)));

            error
                    .checkThat(isMesmaData(
                            locacao.getDataLocacao(),
                            new Date()),
                            is(true));

            error
                    .checkThat(isMesmaData(
                            locacao.getDataRetorno(),
                            obterDataComDiferencaDias(1)),
                            is(true));

    }
}
