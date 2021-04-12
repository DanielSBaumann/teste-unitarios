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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.builder.FilmeBuilder.umFilme;
import static br.ce.wcaquino.matchers.MatchersProprios.*;
import static br.ce.wcaquino.utils.DataUtils.verificarDiaSemana;
import static java.util.Arrays.asList;
import static java.util.Calendar.SATURDAY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeTrue;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    private LocacaoService service;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Before
    public void setup() {
        service = new LocacaoService();
    }

    @Parameterized.Parameter
    public List<Filme> filmes;

    @Parameterized.Parameter(value = 1)
    public Double valorLocacao;

    @Parameterized.Parameter(value = 2)
    public String cenario;

    private static Filme filme1 = umFilme().agora();
    private static Filme filme2 = umFilme().agora();
    private static Filme filme3 = umFilme().agora();
    private static Filme filme4 = umFilme().agora();
    private static Filme filme5 = umFilme().agora();
    private static Filme filme6 = umFilme().agora();
    private static Filme filme7 = umFilme().agora();

    @Parameterized.Parameters(name = "{2}")
    public static Collection<Object[]> getParameters() {
        return asList(new Object[][]{
                {asList(filme1, filme2), 8d, "2 Filmes: Sem desconto"},
                {asList(filme1, filme2, filme3), 11d, "3 Filmes: 25%"},
                {asList(filme1, filme2, filme3, filme4), 13d, "4 Filmes: 50%"},
                {asList(filme1, filme2, filme3, filme4, filme5), 14d, "5 Filmes: 75%"},
                {asList(filme1, filme2, filme3, filme4, filme5, filme6), 14d, "6 Filmes: 100%"},
                {asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18d, "7 Filmes: Sem desconto"}
        });
    }


    @Test
    public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {

        //cenario
        Usuario usuario = new Usuario("Augusto Nunes");

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(valorLocacao));
    }

//    @Test
//    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
//
//        assumeTrue(verificarDiaSemana(new Date(), SATURDAY));
//
//        //cenario
//        Usuario usuario = new Usuario("Usuario 1");
//        List<Filme> filmes = asList(new Filme("Filme 1", 2, 5.0));
//
//        //acao
//        Locacao retorno = service.alugarFilme(usuario, filmes);
//        assertThat(retorno.getDataRetorno(), caiNumaSegunda());
//    }

//    /**
//     * Elegante
//     */
//    @Test(expected = FilmeSemEstoqueException.class)
//    public void naoDeveAlugarFilmeSemEstoque() throws Exception {
//
//        //  cenario
//
//        Usuario usuario = new Usuario("Daniel Baumann");
//        List<Filme> filmes = asList(new Filme("It a coisa", 0, 7.5));
//
//        //  ação
//        Locacao locacao = service.alugarFilme(usuario, filmes);
//
//        //  verificação
//        error
//                .checkThat(
//                        locacao
//                                .getValor(),
//                        is(equalTo(7.5)));
//
//        /**
//         * Refactor
//         */
//        error
//                .checkThat(locacao.getDataLocacao(), hoje());
//
//        /**
//         * Refactor
//         */
//        error
//                .checkThat(locacao.getDataLocacao(), hojeComDiferencaDias(1));
//    }
}
