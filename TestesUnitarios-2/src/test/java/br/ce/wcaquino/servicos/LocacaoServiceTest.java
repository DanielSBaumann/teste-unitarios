package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.*;
import static java.util.Arrays.asList;
import static java.util.Calendar.SATURDAY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

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

    @Test
    public void deveAlugarFilme() throws Exception {

        assumeFalse(verificarDiaSemana(new Date(), SATURDAY));

        //  cenario
        Usuario usuario = new Usuario("Daniel Baumann");
        List<Filme> filmes = asList(new Filme("It a coisa", 4, 7.5));

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
     * Elegante
     */
    @Test(expected = FilmeSemEstoqueException.class)
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {

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
    public void naoDeveALugarFilmeSemUsuario() throws FilmeSemEstoqueException {

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
    public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {

        //  cenario
        LocacaoService service = new LocacaoService();

        Usuario usuario = new Usuario("Daniel Baumann");

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        //  ação
        service.alugarFilme(usuario, null);
        System.out.println("Forma nova");
    }

    @Test
    public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {

        //cenario
        Usuario usuario = new Usuario("Augusto Nunes");
        List<Filme> filmes = asList(
                new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 4, 4.0),
                new Filme("Filme 3", 1, 4.0));

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(11.0));
    }

    @Test
    public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {

        //cenario
        Usuario usuario = new Usuario("Augusto Nunes");
        List<Filme> filmes = asList(
                new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 4, 4.0),
                new Filme("Filme 3", 4, 4.0),
                new Filme("Filme 4", 1, 4.0));

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(13.0));
    }

    @Test
    public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {

        //cenario
        Usuario usuario = new Usuario("Augusto Nunes");
        List<Filme> filmes = asList(
                new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 4, 4.0),
                new Filme("Filme 3", 4, 4.0),
                new Filme("Filme 4", 1, 4.0),
                new Filme("Filme 5", 1, 4.0));

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(14.0));
    }

    @Test
    public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {

        //cenario
        Usuario usuario = new Usuario("Augusto Nunes");
        List<Filme> filmes = asList(
                new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 4, 4.0),
                new Filme("Filme 3", 4, 4.0),
                new Filme("Filme 4", 1, 4.0),
                new Filme("Filme 5", 1, 4.0),
                new Filme("Filme 6", 1, 4.0)
        );

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(14.0));
    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {

        assumeTrue(verificarDiaSemana(new Date(), SATURDAY));

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = asList(new Filme("Filme 1", 2, 5.0));

        //acao
        Locacao retorno = service.alugarFilme(usuario, filmes);

        boolean segunda = verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
        assertTrue(segunda);
    }
}
