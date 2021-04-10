package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    private LocacaoService service;

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

    private static Filme filme1 = new Filme("Filme 1", 2, 4d);
    private static Filme filme2 = new Filme("Filme 2", 2, 4d);
    private static Filme filme3 = new Filme("Filme 3", 2, 4d);
    private static Filme filme4 = new Filme("Filme 4", 2, 4d);
    private static Filme filme5 = new Filme("Filme 5", 2, 4d);
    private static Filme filme6 = new Filme("Filme 6", 2, 4d);
    private static Filme filme7 = new Filme("Filme 7", 2, 4d);

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

    @Test
    public void print() {
        System.out.println(valorLocacao);
    }
}
