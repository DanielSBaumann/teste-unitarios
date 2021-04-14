package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDAO;
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
import java.util.List;

import static br.ce.wcaquino.builder.FilmeBuilder.umFilme;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    private LocacaoService service;

    private SPCService spc;

    private LocacaoDAO dao;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Before
    public void setup() {
        service = new LocacaoService();
        dao = mock(LocacaoDAO.class);
        service.setLocacaoDAO(dao);
        spc = mock(SPCService.class);
        service.setSPCService(spc);
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

}
