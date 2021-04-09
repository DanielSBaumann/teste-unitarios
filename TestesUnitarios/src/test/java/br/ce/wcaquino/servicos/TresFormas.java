package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class TresFormas {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * Elegante
     */
    @Test(expected = FilmeSemEstoqueException.class)
    public void filmeSemEstoque() throws Exception {

        //  cenario
        LocacaoService service = new LocacaoService();

        Usuario usuario = new Usuario("Daniel Baumann");
        Filme filme = new Filme("It a coisa", 0, 7.5);

        //  ação
        Locacao locacao = service.alugarFilme(usuario, filme);
    }

    /**
     * Robusta
     */
    @Test
    public void filmeSemEstoque_2() {

        //  cenario
        LocacaoService service = new LocacaoService();

        Usuario usuario = new Usuario("Daniel Baumann");
        Filme filme = new Filme("It a coisa", 0, 7.5);

        try {
            //  ação
            Locacao locacao = service.alugarFilme(usuario, filme);
            fail("Deveria ter lancado uma excessão");
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Filme sem estoque"));
        }
    }

    /**
     * Nova
     */
    @Test
    public void filmeSemEstoque_3() throws Exception {

        //  cenario
        LocacaoService service = new LocacaoService();

        Usuario usuario = new Usuario("Daniel Baumann");
        Filme filme = new Filme("It a coisa", 0, 7.5);

        exception.expect(Exception.class);
        exception.expectMessage("Filme sem estoque");

        //  ação
        Locacao locacao = service.alugarFilme(usuario, filme);
    }
}
