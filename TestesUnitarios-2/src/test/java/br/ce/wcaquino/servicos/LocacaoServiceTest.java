package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class LocacaoServiceTest {

    private LocacaoService service;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        service = new LocacaoService();
    }

    /**
     * Elegante
     */
    @Test(expected = FilmeSemEstoqueException.class)
    public void filme_sem_estoque() throws Exception {

        //  cenario

        Usuario usuario = new Usuario("Daniel Baumann");
        List<Filme> filmes = asList(new Filme("It a coisa", 0, 7.5));

        //  ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //  verificação
        error
                .checkThat(
                        locacao
                                .getValor(),
                        is(equalTo(7.5)));

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

    /**
     * Robusta
     */
    @Test
    public void teste_usuario_vazio() throws FilmeSemEstoqueException {

        //  cenario
        LocacaoService service = new LocacaoService();

        //Usuario usuario = new Usuario("Daniel Baumann");
        List<Filme> filmes = asList(new Filme("It a coisa", 1, 7.5));

        //  ação
        try {
            Locacao locacao = service.alugarFilme(null, filmes);
            fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuario vazio"));
        }
    }

    /**
     * Nova
     */
    @Test
    public void teste_filme_vazio() throws FilmeSemEstoqueException, LocadoraException {

        //  cenario
        LocacaoService service = new LocacaoService();

        Usuario usuario = new Usuario("Daniel Baumann");

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        //  ação
        service.alugarFilme(usuario, null);
        System.out.println("Forma nova");
    }
}
